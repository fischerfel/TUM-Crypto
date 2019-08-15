private Bitmap requestImage(String file) {
    Bitmap bm = null;

    Log.d(TAG, "path: " + file);
    URL url = null;
    HttpURLConnection http = null;

    try {

        url = new URL(file);

        if (url.getProtocol().toLowerCase().equals("https")) {
            NetworkUtils.trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url
                    .openConnection();
            https.setHostnameVerifier(NetworkUtils.DO_NOT_VERIFY);
            http = https;
        } else {
            http = (HttpURLConnection) url.openConnection();
        }
        http.setUseCaches(true);

        ResponseCache.setDefault(new ImageResponseCache(
                ImageAdapter.this.mContext.getCacheDir()));

        http.setRequestProperty(
                "Authorization",
                "Basic "
                        + Base64.encodeToString(
                                (Constants.USER + ":" + Constants.PASSWORD)
                                        .getBytes(), Base64.NO_WRAP));

        http.setConnectTimeout(Constants.TIME_OUT);

        bm = BitmapFactory.decodeStream((InputStream) http.getContent());

    } catch (Exception e) {
        Log.e(TAG, e.getMessage(), e);
    }

    return bm;
}
