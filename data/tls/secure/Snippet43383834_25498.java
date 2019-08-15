SSLContext sc = SSLContext.getInstance("TLSv1.2"); //$NON-NLS-1$
sc.init(null, null, new java.security.SecureRandom());
HttpsURLConnection con = (HttpsURLConnection) httpsURL.openConnection();
con.setSSLSocketFactory(sc.getSocketFactory());
