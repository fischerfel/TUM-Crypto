if (testUrlHttps.getProtocol().toLowerCase().equals("https")) {
   trustAllHosts();
   HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
   https.setHostnameVerifier(DO_NOT_VERYFY);
   urlCon = https;
} else {
   urlCon = (HttpURLConnection) url.openConnection();
}
