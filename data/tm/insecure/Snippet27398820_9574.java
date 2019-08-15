            SSLContext ctx = null;
            TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};
            try {
                ctx = SSLContext.getInstance("SSL");
                ctx.init(null, trustAllCerts, null);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                LOGGER.info("Error loading ssl context {}", e.getMessage());
            }

            SSLContext.setDefault(ctx);
