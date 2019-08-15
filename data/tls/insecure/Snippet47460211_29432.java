fun useInsecureSSL() {

    // Create a trust manager that does not validate certificate chains
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate>? = null
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
    })

    val sc = SSLContext.getInstance("SSL")
    sc.init(null, trustAllCerts, java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

    // Create all-trusting host name verifier
    val allHostsValid = HostnameVerifier { _, _ -> true }

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
}
