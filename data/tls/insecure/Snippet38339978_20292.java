package controllers

import java.nio.file._
import java.security.KeyStore
import javax.net.ssl._

import play.core.ApplicationProvider
import play.server.api._

class CustomSslEngineProvider(appProvider: ApplicationProvider) extends SSLEngineProvider {

  val priorityCipherSuites = List(
    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
    "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA")


  def readPassword(): Array[Char] = System.getProperty("play.server.https.keyStore.password").toCharArray

  def readKeyInputStream(): java.io.InputStream = {
    val keyPath = FileSystems.getDefault.getPath(System.getProperty("play.server.https.keyStore.path"))
    Files.newInputStream(keyPath)
  }

  def readKeyManagers(): Array[KeyManager] = {
    val password = readPassword()
    val keyInputStream = readKeyInputStream()
    try {
      val keyStore = KeyStore.getInstance(KeyStore.getDefaultType)
      keyStore.load(keyInputStream, password)
      val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
      kmf.init(keyStore, password)

      kmf.getKeyManagers
    } finally {
      keyInputStream.close()
    }
  }

  def createSSLContext(): SSLContext = {
    val keyManagers = readKeyManagers()
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagers, Array.empty, null)
    sslContext
  }

  override def createSSLEngine(): SSLEngine = {
    val ctx = createSSLContext()
    val sslEngine = ctx.createSSLEngine
    val cipherSuites = sslEngine.getEnabledCipherSuites.toList
    val orderedCipherSuites =
      priorityCipherSuites.filter(cipherSuites.contains) ::: cipherSuites.filterNot(priorityCipherSuites.contains)
    sslEngine.setEnabledCipherSuites(orderedCipherSuites.toArray)
    val params = sslEngine.getSSLParameters
    params.setUseCipherSuitesOrder(true)
    sslEngine.setSSLParameters(params)
    sslEngine
  }
}
