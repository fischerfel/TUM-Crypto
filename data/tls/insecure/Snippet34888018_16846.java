        SSLContext sc = SSLContext.getInstance("TLSv1");
        System.setProperty("https.protocols","TLSv1");

        TrustManager[] trustAllCerts = {new InsecureTrustManager()};
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
