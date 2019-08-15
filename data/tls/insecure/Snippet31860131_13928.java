public static Picasso getInstance(Context context) {
        if (sPicasso == null) {
            InputStream keyStore = context.getResources().openRawResource(R.raw.my_keystore);
            Picasso.Builder builder = new Picasso.Builder(context);
            OkHttpClient okHttpClient = new OkHttpClient();
            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new SsX509TrustManager(keyStore, password)}, null);
                okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
                OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                builder.downloader(okHttpDownloader);
                sPicasso = builder.build();
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("Failure initializing default SSL context", e);
            } catch (KeyManagementException e) {
                throw new IllegalStateException("Failure initializing default SSL context", e);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }

        return sPicasso;
    }
