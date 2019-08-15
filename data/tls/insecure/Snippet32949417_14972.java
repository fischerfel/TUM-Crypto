String auth = username + ":" + password;
URL url = new URL(hostname + path);
TrustManager[] trustAllCerts = new TrustManager[] { new SSLTrustManager() };
HostnameVerifier hostnameVerifier = new SSLHostnameVerifier();

SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());

HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setRequestProperty("Authorization", "Basic " + auth);
conn.connect();
