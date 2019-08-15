URL request = new URL(url);
HttpsURLConnection urlConnection = (HttpsURLConnection) request.openConnection();
urlConnection.setHostnameVerifier(new StrictHostnameVerifier());
