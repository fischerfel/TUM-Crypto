import java.io.{BufferedInputStream, FileInputStream}
import java.security.{SecureRandom, KeyStore}
import java.security.cert.{X509Certificate, CertificateFactory}
import javax.net.ssl.{TrustManagerFactory, KeyManagerFactory, SSLContext}
import spray.io._
import org.apache.camel.util.jsse._

// for SSL support (if enabled in application.conf)
trait MySSLConfig {
  // if there is no SSLContext in scope implicitly the HttpServer uses the default SSLContext,
  // since we want non-default settings in this example we make a custom SSLContext available here
  implicit def sslContext: SSLContext = {
    val keyStoreResource = "/home/ubuntu/key.jks"
    val password = "password"

    val keyStore = KeyStore.getInstance("jks")
    keyStore.load(getClass.getResourceAsStream(keyStoreResource), password.toCharArray)
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(keyStore, password.toCharArray)
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    trustManagerFactory.init(keyStore)
    val context = SSLContext.getInstance("TLS")
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    context
  }
  implicit def sslEngineProvider: ServerSSLEngineProvider = {
    ServerSSLEngineProvider { engine =>
      engine.setEnabledCipherSuites(Array("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256"))
      engine.setEnabledProtocols(Array("SSLv3", "TLSv1.2", "TLSv1", "TLSv1.1"))
      engine
    }
  }
}
