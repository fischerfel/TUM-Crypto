    SSLContext sc = SSLContext.getInstance("SSL");
    // here you may want to call sc.init() with your key managers and trustmanagers 
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(verifier);
    HttpsURLConnection.setFollowRedirects(false);

    //setting authenticator which will push your credentials to the server when required
    Authenticator.setDefault(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(config.getUserName(), config.getPassword().toCharArray());
        }
    })
