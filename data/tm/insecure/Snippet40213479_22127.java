import java.io.FileInputStream
import java.security.cert.X509Certificate
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl._

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.nio.client.{CloseableHttpAsyncClient, HttpAsyncClients}
import org.apache.commons.io.IOUtils
import org.apache.http.ssl.SSLContexts

def httpClientFactory(
  keyStoreFileName: String
): CloseableHttpAsyncClient = {
  val httpClientBuilder = HttpAsyncClients.custom()

  // activating or not the certificate checking
  if (checkCertificate) {
    // import keystore
    val keyStorePassword = jksPassword // the password you used whit the command keytool
    val ks = KeyStore.getInstance(KeyStore.getDefaultType)
    val keyStorePath = getClass.getClassLoader.getResource(keyStoreFileName)
    val inputStream = new FileInputStream(keyStorePath.getPath)
    ks.load(inputStream, keyStorePassword.toArray)
    IOUtils.closeQuietly(inputStream)
    // create trust manager from keystore
    val tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    tmf.init(ks)
    val trustManager = tmf.getTrustManagers
    // associate trust manager with the httpClient
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(Array(), trustManager, null)
    httpClientBuilder.setSSLContext(sslContext)
  } else {
    logger.warn("Warning ! Https connections will be done without checking certificate. Do not use in production.")
    val sslContext = SSLContexts.createDefault()
    sslContext.init(null, Array(new X509TrustManager {
      override def getAcceptedIssuers: Array[X509Certificate] = Array.empty[X509Certificate]
      override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}
      override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}
    }), new SecureRandom())
    httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
      .setSSLContext(sslContext)
  }

  // ending httpClient creation
  httpClientBuilder.build()
}
