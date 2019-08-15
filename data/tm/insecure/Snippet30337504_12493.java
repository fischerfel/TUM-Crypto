import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLContext, X509TrustManager, TrustManager}

import akka.actor.ActorRef
import akka.io.IO
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.Future

trait HttpClient {
  /** For the HostConnectorSetup ask operation. */
  implicit val ImplicitPoolSetupTimeout: Timeout = 30 seconds

  val hostName: String
  val hostPort: Int

  implicit val sslContext = {
    /** Create a trust manager that does not validate certificate chains. */
    val permissiveTrustManager: TrustManager = new X509TrustManager() {
      override def checkClientTrusted(chain: Array[X509Certificate], authType: String): Unit = {
      }
      override def checkServerTrusted(chain: Array[X509Certificate], authType: String): Unit = {
      }
      override def getAcceptedIssuers(): Array[X509Certificate] = {
        null
      }
    }

    val initTrustManagers = Array(permissiveTrustManager)
    val ctx = SSLContext.getInstance("TLS")
    ctx.init(null, initTrustManagers, new SecureRandom())
    ctx
  }

  def initClientPool(): Future[ActorRef] = {
    val hostPoolFuture = for {
      Http.HostConnectorInfo(connector, _) <- IO(Http) ? Http.HostConnectorSetup(hostName, port = hostPort,
        sslEncryption = true)
    } yield connector
  }
}
