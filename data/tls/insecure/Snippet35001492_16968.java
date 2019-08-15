package httpsserver

import java.security.{SecureRandom, KeyStore}
import javax.net.ssl.{KeyManagerFactory, SSLContext}

import akka.actor.ActorSystem
import akka.http.scaladsl.{HttpsContext, Http}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object Server extends App {

  val serverContext: HttpsContext = {
    val password = "abcdef".toCharArray
    val context = SSLContext.getInstance("TLS")
    val ks = KeyStore.getInstance("PKCS12")
    ks.load(getClass.getClassLoader.getResourceAsStream("keys/server.p12"), password)
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, password)
    context.init(keyManagerFactory.getKeyManagers, null, new SecureRandom)
    // start up the web server
    HttpsContext(context)
  }

  implicit val system = ActorSystem("server")
  implicit val materializer = ActorMaterializer()
  import system._

  val route = Route(complete("ok"))

  Http().bindAndHandle(route, interface = "0.0.0.0", port = 8081, httpsContext = Some(serverContext))
}
