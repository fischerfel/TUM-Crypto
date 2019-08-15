 InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    HttpsURLConnection httpsURLConnection = null;
    try {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[]{new CustomTrustManager()}, new SecureRandom());
                SSLContext.setDefault(ctx);

                httpsURLConnection = (HttpsURLConnection) imageUrl.openConnection();
                httpsURLConnection.setSSLSocketFactory(ctx.getSocketFactory());
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int BUFFER_SIZE = 16 * 1024; // Defining a standard value 8K as Buffer (8192)

        /*
         * Define InputStreams to read from the URLConnection.
         */
        if (httpsURLConnection == null) {
            inputStream = new BufferedInputStream(imageUrl.openConnection().getInputStream(), BUFFER_SIZE);
        } else {
            inputStream = new BufferedInputStream(httpsURLConnection.getInputStream(), BUFFER_SIZE);
        }

        File parentDirectory = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mContext.getExternalFilesDir(null) : mContext.getFilesDir();
        File imageDir = new File(parentDirectory, "Images");
        if (!imageDir.exists()) imageDir.mkdir();
