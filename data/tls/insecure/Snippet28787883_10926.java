import javax.net.ssl._
import java.security.cert.X509Certificate
import scala.io.Source

// Bypasses both client and server validation.
object TrustAll extends X509TrustManager {
  val getAcceptedIssuers = null

  def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String) = {}

  def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String) = {}
}

// Verifies all host names by simply returning true.
object VerifiesAllHostNames extends HostnameVerifier {
  def verify(s: String, sslSession: SSLSession) = true
}

// Main class
object Test extends App {
  // SSL Context initialization and configuration
  val sslContext = SSLContext.getInstance("SSL")
  sslContext.init(null, Array(TrustAll), new java.security.SecureRandom())
  HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory)
  HttpsURLConnection.setDefaultHostnameVerifier(VerifiesAllHostNames)

  // Actual call
  val html = Source.fromURL("https://scans.io/json")
  println(html.mkString)
}
