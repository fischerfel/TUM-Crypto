private HttpsURLConnection preparePUTConnection(final String path, int length)
        throws MalformedURLException, IOException, ProtocolException {
    URL url = new URL(path);
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    urlConnection.setHostnameVerifier(DO_NOT_VERIFY);
    urlConnection.setDoOutput(true);
    String userpassword = username + ":" + password.toString();
    String encodedAuthorization = Base64.encodeToString(userpassword.getBytes(), Base64.DEFAULT);
    urlConnection.setRequestProperty("Authorization", "Basic "+
            encodedAuthorization);
    urlConnection.setRequestMethod("PUT");
    urlConnection.setRequestProperty("Content-Length", new Integer(length).toString());
    urlConnection.connect();


    return urlConnection;
}
