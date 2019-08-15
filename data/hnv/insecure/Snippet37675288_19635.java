HttpClient httpclient = HttpClients.custom()
              .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) //   TODO
            .setSSLSocketFactory(sslsf).addInterceptorFirst(new  ContentRemover()).build();
