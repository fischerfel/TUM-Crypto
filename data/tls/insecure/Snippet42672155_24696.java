log.info("Disabling SSL validation to allow self signed SSL certificates");
final SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, new TrustManager[]{new NonValidatingFactory.NonValidatingTM()},
    new SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> true);
java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
java.lang.System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "true");
