public String makeServiceCall(String reqUrl, Context con) {
    String response = null;
    context = con;
    DB = new DatabaseVerwerker(context); //<-- database verbinden

    try {
        URL url = new URL(reqUrl);
        String[] UrlOnderdeel = reqUrl.split("://");
        if(UrlOnderdeel[0].equals("http")) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("key="+DB.GetVar(DB, "authkey", "null"));
            dos.close();
            conn.connect();
            int responseCode = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if(responseCode >= 300 && responseCode < 400) {
                response = makeServiceCall(conn.getHeaderField("Location").toString(), context);
            } else {
                response = convertStreamToString(in);
            }
        }
        if(UrlOnderdeel[0].equals("https")) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new TrustAll()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("key="+DB.GetVar(DB, "authkey", "null"));
            dos.close();
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode >= 300 && responseCode < 400) {
                response = makeServiceCall(conn.getHeaderField("Location").toString(), context);
            } else {
                response = convertStreamToString(conn.getInputStream());
            }
        }
    } catch (MalformedURLException e) {
        Log.e(TAG, "MalformedURLException: " + e.getMessage());
    } catch (ProtocolException e) {
        Log.e(TAG, "ProtocolException: " + e.getMessage());
    } catch (IOException e) {
        Log.e(TAG, "IOException: " + e.getMessage());
    } catch (Exception e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    if(response != null) {
        Log.v("JsonRespons", response.toString());
    }
    return response;
}
