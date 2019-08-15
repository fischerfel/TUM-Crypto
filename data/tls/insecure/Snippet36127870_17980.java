private void httpCall(String URL, String json, String session key, int type) {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1");
           sslcontext.init(null,
                    null,
                    null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

            Log.i(REQUEST_TAG, "httpCall=url" + url + "::type" + type);
            Log.i(REQUEST_TAG, "httpCall=json" + json);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mContext != null)
            mQueue = CustomVolleyRequestQueue.getInstance(mContext).getRequestQueue();
        else
            mQueue = CustomVolleyRequestQueue.getInstance(mActivity).getRequestQueue();
        JSONObject mJSONObject;
        final CustomJSONObjectRequest jsonRequest;
        try {
            if ((json != null) && (json.trim().length() > 0)) {
                mJSONObject = new JSONObject(json);
            } else {
                mJSONObject = new JSONObject();
            }
            jsonRequest = new CustomJSONObjectRequest(sessionkey, type, url, mJSONObject, this, this);
            // Wait 20 seconds and don't retry more than once
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(20),
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            jsonRequest.setTag(REQUEST_TAG);
            mQueue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
