SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    **Oracle JDK8**  

            MQEnvironment.sslFipsRequired = false;  
            MQEnvironment.sslCipherSuite = "TLS_RSA_WITH_AES_128_CBC_SHA256";  
            ALTER CHANNEL(TEST.CH) CHLTYPE(SVRCONN) SSLCIPH(TLS_RSA_WITH_AES_128_CBC_SHA256)
    **IBM-JDK 7.1**  
                    MQEnvironment.sslFipsRequired = false;
                    MQEnvironment.sslCipherSuite = "SSL_RSA_WITH_NULL_SHA256";
    ALTER CHANNEL(TEST.CH) CHLTYPE(SVRCONN) SSLCIPH(TLS_RSA_WITH_NULL_SHA256)
