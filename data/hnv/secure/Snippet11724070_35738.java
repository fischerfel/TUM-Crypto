private static String retrieveStream(String url) throws SocketTimeoutException {

    String streamContent = "";
    HttpsURLConnection urlConnection = null;

    try {

        Log.d("Connection", "Connecting to: " + url);

        Log.d("Connection", "Opening connection");
        urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        Log.d("Connection", "Setting connect timeout");
        urlConnection.setConnectTimeout(5000);
        Log.d("Connection", "Setting read timeout");
        urlConnection.setReadTimeout(5000);
        Log.d("Connection", "Setting Allow All certs (because this is just testing)");
        urlConnection.setHostnameVerifier(new AllowAllHostnameVerifier());

        Log.d("Connection", "Connection Response: " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());

        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            streamContent = readStreamFromConnection(urlConnection);
            Log.d("Connection", "Returned content is: " + streamContent);
        }

    } catch (MalformedURLException e) {
        Log.e("Error", "MalformedURLException thrown in retrieveStream: " + e);
    } catch (IOException e) {
        Log.e("Error", "IOException thrown in retrieveStream for " + url + " : " + e);
    } finally {
        urlConnection.disconnect();
    }

    return streamContent;
}
