object Security {
    private def createCtxSsl(ctx: Context) = {
        val cer = {
            val is = ctx.getAssets.open("mycertificate.crt")
            try
                CertificateFactory.getInstance("X.509").generateCertificate(is)
            finally
                is.close()
        }
        val key = KeyStore.getInstance(KeyStore.getDefaultType)
        key.load(null, null)
        key.setCertificateEntry("ca", cer)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    tmf.init(key)

        val c = SSLContext.getInstance("TLS")
        c.init(null, tmf.getTrustManagers, null)
        c
    }

    def prepare(url: HttpURLConnection)(implicit ctx: Context) {
        url match {
            case https: HttpsURLConnection ⇒
                val cSsl = ctxSsl match {
                    case None ⇒
                        val res = createCtxSsl(ctx)
                        ctxSsl = Some(res)
                        res
                    case Some(c) ⇒ c
                }
                https.setSSLSocketFactory(cSsl.getSocketFactory)
            case _ ⇒
        }
    }

    def noSecurity(url: HttpURLConnection) {
        url match {
            case https: HttpsURLConnection ⇒
                https.setHostnameVerifier(new HostnameVerifier {
                    override def verify(hostname: String, session: SSLSession) = true
                })
            case _ ⇒
        }
    }
}
