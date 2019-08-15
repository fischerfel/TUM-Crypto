          public void checkClientTrusted(
                  X509Certificate[] chain, String authType) throws CertificateException {
              // Always trust - it is an example.
              // You should do something in the real world.
              // You will reach here only if you enabled client certificate auth,
              // as described in SecureChatSslContextFactory.
              System.err.println(
                      "UNKNOWN CLIENT CERTIFICATE: " + chain[0].getSubjectDN());
          }

          public void checkServerTrusted(
                 X509Certificate[] chain, String authType) throws CertificateException {
             // Always trust - it is an example.
              // You should do something in the real world.
              System.err.println(
                      "UNKNOWN SERVER CERTIFICATE: " + chain[0].getSubjectDN());
          }
