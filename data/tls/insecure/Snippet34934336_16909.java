val sc = SSLContext.getInstance("TLS")
*...configuration...*
val customContext =HttpsContext(sc, sslParameters = Some(params))
Http().setDefaultClientHttpsContext(customHttpsContext)
