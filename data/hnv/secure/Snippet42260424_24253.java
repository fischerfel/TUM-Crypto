private static final String COMPRESSED_FILE_PREFIX = "yourapp_image_compressed_";
private static final String JPEG_FILE_EXTENSION = ".jpeg";
private static final int FIVE_MINUTE_INIT_TIMEOUT = 300000;

public void uploadImage(Context applicationContext, ArrayList<String> filePathsToUpload) {
        RequestParams requestParams = new RequestParams();

        File imagesCacheDir;

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            imagesCacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "/Android/data/com.example.yourapp/UploadPics");
        } else {
            imagesCacheDir = applicationContext.getCacheDir();
        }

        if (!imagesCacheDir.exists()) {
            if (!imagesCacheDir.mkdirs()) {
                throw new RuntimeException("Image directory could not be created.");
            }
        }

        for (String filePath : filePathsToUpload) {
            File file;

            try {
                FileOutputStream out = new FileOutputStream(new File(imagesCacheDir,
                        COMPRESSED_FILE_PREFIX + filePathsToUpload.size() + 1 + JPEG_FILE_EXTENSION));

                BitmapFactory.decodeFile(filePath).compress(Bitmap.CompressFormat.JPEG, compress ? 90 : 100, out);

                file = new File(imagesCacheDir, COMPRESSED_FILE_PREFIX + filePathsToUpload.size() + 1 + JPEG_FILE_EXTENSION);
                requestParams.put(file.getName(), file);
                out.close();
            } catch (FileNotFoundException fnfe) {
                throw new RuntimeException("Image file not found.");
            } catch (IOException ie) {
                throw new RuntimeException("Error writing image to file.);
            }
        }

        SyncHttpClient client = new SyncHttpClient();
        client.setTimeout(FIVE_MINUTE_INIT_TIMEOUT);

        Map<String, String> headers = VolleyUtils.getBigOvenHeaders();
        headers.putAll(VolleyUtils.getAuthenticationHeader());

        for (String headerKey : headers.keySet()) {
            client.addHeader(headerKey, headers.get(headerKey));
        }

        MySSLSocketFactory socketFactory;

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(socketFactory);
        } catch (Exception e) {
            // Probably should die or something, unless http is okay (if you need https, this is no go)
        }

        client.setTimeout(FIVE_MINUTE_INIT_TIMEOUT);

        client.post(this, "https://www.myapp.com/imageUploader", requestParams, new RotatingBitmapTextHttpResponseHandler(filePathsToUpload, notificationBuilder, onCompletionText));
}
