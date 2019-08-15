private void getCaptchaImageFromServer() {
        String URL = ServerPath.SERVER_PATH_GET_CAPTCHA_IMAGE + "?accesstoken=" + UserInforLogin.getInstance(this).getToken()
                + "&userid=" + UserInforLogin.getInstance(this).getUserId();
        LogUtils.e("test-request captcha image", URL);

        try {
            URL imageURL = new URL(URL);
            ImageView imageView = (ImageView) findViewById(R.id.imageviewTest);

            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] {new NullX509TrustManager()}, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            HttpsURLConnection https = (HttpsURLConnection) imageURL.openConnection();
            https.connect();
            InputStream input = https.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            imageView.setImageBitmap(myBitmap);
        } catch (Exception e) {
            // TODO: handle exception
            LogUtils.e("test-request captcha image ERROR", "ERROR");
        }

    }
