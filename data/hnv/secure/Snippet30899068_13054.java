URL url = new URL("HTTPS SERVER URL GOES HERE");
HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
connection.setHostnameVerifier(hv);
