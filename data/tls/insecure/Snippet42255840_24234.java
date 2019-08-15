fun getResponse(method: String = "GET",
                protocol: String = "https",
                host: String,
                path: String,
                query: Map<String, String> = hashMapOf<String, String>(),
                headery: MutableMap<String, String> = hashMapOf<String, String>(),
                allowRedirects: Boolean = false,
                postContents: String = "",
                useCache: Boolean = true,
                wait: Long = 0,
                port: Int = -1): RestResponse {
    if (!useCache) {
        Thread.sleep(wait)
    }
    val connection = getConnection(method, protocol, host, path, headery, query, port) as HttpURLConnection
    if (!allowRedirects)
        connection.instanceFollowRedirects = false

        return connection.obtainRestResponse(postContents, useCache).apply { connection.disconnect() }

}

private fun getConnection(method: String, protocol: String, host: String, path: String, headery: MutableMap<String, String>, query: Map<String, String>, port : Int = -1): URLConnection {
    if(protocol=="https") {
        val sc = SSLContext.getInstance("TLS") // bylo: SSL
        sc.init(null, XenoHttpClient.trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { s, sslSession -> true }
    }
    var connection = buildUrl(protocol, host, path, query, port).openConnection() as HttpURLConnection
    connection.requestMethod = method
    if (!headery.containsKey("User-Agent"))
        headery.put("User-Agent", "curl/7.22.0 (i686-pc-linux-gnu) libcurl/7.21.3 OpenSSL/0.9.8o zlib/1.2.3.4 libidn/1.23")

    connection.applyHeaders(headery)
    connection.useCaches = false

    return connection
}

private fun HttpURLConnection.obtainRestResponse(postContents: String = "", useCache: Boolean = true): RestResponse {

    if (postContents.isNotBlank()) {
        this.doOutput = true
        Logger.d("POST contents:$postContents")
        DataOutputStream(this.outputStream).let {
            it.writeBytes(postContents)
            it.flush()
            it.close()
        }
    }

    this.connect()

    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
        val kesz = XenoFile(XenoAmp.cacheDir(),
                XenoUtility.md5(this.url.toString()))

        var cachingReq = false

        var istream = if (this.requestMethod.toUpperCase() != "GET") {
            // nie keszujemy put/delete!
            inputStream
        } else if (useCache && kesz.exists()) {
            // już go mamy, więc z niego czytamy
            Logger.d("Query cached locally:" + this.url.toString())
            XenoFileInputStream(kesz)
        } else {
            // nie mamy w keszu
            cachingReq = useCache
            inputStream
        }

        val tekst = XenoFile.streamToText(istream)

        if (cachingReq) {
            XenoFile.cacheText(this.url.toString(), tekst)
        }

        return RestResponse(responseCode, headersToMap(this.headerFields), tekst)
    } else {
        if(errorStream!=null) {
            val tekst = try {
                XenoFile.streamToText(errorStream)
            } catch (x: Exception) {
                Logger.e(x, "Problem")
                "REST Error: Bad status: $responseCode"
            }
            Logger.d("Error stream:$tekst")
            throw IllegalStateException(tekst)
        } else {
            Logger.d("Problem obtaining response for:${this.url} --> $responseCode,$responseMessage")
            throw IllegalStateException(responseMessage)
        }
    }
}

fun buildUrl(protocol: String, host: String, path: String, query: Map<String, String>, port : Int = -1)
 = if(port!=-1) URL(protocol, host, port, path + query.toQueryString()) else URL(protocol, host, path + query.toQueryString())
