object RestWebServiceApp extends App with RouteConcatenation {
  import akka.event.{Logging, LoggingAdapter}
  import akka.http.scaladsl.{ ConnectionContext, HttpsConnectionContext, Http }
  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.model.MediaTypes._
  import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
  import java.io.InputStream
  import java.security.{ SecureRandom, KeyStore }
  import javax.net.ssl.{ SSLContext, TrustManagerFactory, KeyManagerFactory }
  import JsonSupport._

  implicit val system = ActorSystem("OtisRestActorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
  implicit val ec = system.dispatcher

  ...  //setting up all the routes, etc.

  val settings = Settings(system)
  val fileName = settings.Ssl.KeyStoreFileName
  val keyFile: InputStream = getClass.getClassLoader.getResourceAsStream(fileName)
  require(keyFile != null, s"Failed to load key file: ${settings.Ssl.KeyStoreFileName}")
  val extension = if(fileName.lastIndexOf('.')>0) fileName.substring(fileName.lastIndexOf('.')+1) else ""
  val keyStore: KeyStore = extension.toLowerCase match {
    case "jks" =>  KeyStore.getInstance("jks") //Java Key Store; Java default and only works with Java; tested
    case "jcek" =>  KeyStore.getInstance("JCEKS") //Java Cryptography Extension KeyStore; Java 1.4+; not tested
    case "pfx" | "p12" =>  KeyStore.getInstance("PKCS12") // PKCS #12, Common and supported by many languages/frameworks; tested
    case _ => throw new IllegalArgumentException(s"Key has an unknown type extension $extension. Support types are jks, jcek, pfx, p12.")
  }
  val password: Array[Char] = (if(settings.Ssl.KeyStorePassword==null) "" else settings.Ssl.KeyStorePassword).toCharArray
  keyStore.load(keyFile, password)

  //TODO: looks like the "SunX509", "TLS", are defined in the keystore, should we pull them out rather than hard coding?
  val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(keyStore, password)

  val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
  tmf.init(keyStore)

  val sslContext: SSLContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, new SecureRandom)
  val https: HttpsConnectionContext = ConnectionContext.https(sslContext)
  Http().setDefaultServerHttpContext(https)
  Http().bindAndHandle(routes, "localhost", 433, connectionContext = https)
}
