URL url = new URL("some_url");
HttpURLConnection connection = null;
// check if this is https or just http
if (url.getProtocol().toLowerCase().equals("https")) {
    trustAll();
    HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
    https.setHostnameVerifier(DO_NOT_VERIFY);
    connection = https;
} else {
    connection = (HttpURLConnection) url.openConnection();
}
