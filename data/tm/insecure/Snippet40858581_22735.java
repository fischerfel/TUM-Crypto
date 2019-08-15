    private class VideoLink extends AsyncTask<Void, Void, Void> {
    String title;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setTitle("JSOUP Test");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            // for avoiding javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name
            System.setProperty("jsse.enableSNIExtension", "false");

            // WARNING: do it only if security isn't important, otherwise you have
            // to follow this advices: http://stackoverflow.com/a/7745706/1363265
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                ;
            }

            // Connect to the web site
            Document doc = Jsoup.connect(TEST_URL).get();
            Elements elements = doc.getElementsByClass("videoText");
            // Get the html document title
            for (Element link : elements) {
                String linkHref = link.attr("href");
                // linkHref contains something like video.html?month=11&year=2016&filename=current.mp4
                // TODO check if linkHref ends with current.mp4
                title = linkHref;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Set title into TextView
        resultTxt.setText(title);
        String resVid = TEST_URL;
        Log.d(TAG, "URL: " + resVid);
        Uri resUri = Uri.parse(resVid);
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    MainActivity.this);
            mediacontroller.setAnchorView(resultVidVw);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(resVid);
            resultVidVw.setMediaController(mediacontroller);
            resultVidVw.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        resultVidVw.requestFocus();
        resultVidVw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                mProgressDialog.dismiss();
                resultVidVw.start();
            }
        });
    }
}
