  private[this] class IgnorantTrustManager extends X509TrustManager {
    def getAcceptedIssuers(): Array[X509Certificate] = new Array[X509Certificate](0)
    def checkClientTrusted(certs: Array[X509Certificate], authType: String) {
    }
    def checkServerTrusted(certs: Array[X509Certificate], authType: String) {
    }
  }
