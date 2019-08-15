private class UploadBackgroundTask extends AsyncTask<String, Void, String> {

    protected void onPreExecute() {
        dialog = new ProgressDialog(xxx.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

    }

    protected String doInBackground(final String... args) {



        try{
            TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManager, new SecureRandom());
            } catch (Exception e) {
                e.printStackTrace();
            }
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            FTPClient client = new FTPClient();

            client.setSSLSocketFactory(sslSocketFactory);
            client.setSecurity(FTPClient.SECURITY_FTPES);

            client.connect("xxx.xxx.com");
            client.login("xxx", "xxx");

            client.changeDirectory("product_images/import");
            client.setType(FTPClient.TYPE_BINARY);
            //@TODO read file from android system
            System.out.println("temPath -->"+picturepath);
            client.upload(new File(picturepath));


            FTPFile[] list = client.list();
            for(int i=0;i<list.length;i++){
                System.out.println("-->"+list[i]);
            }


            Log.v("SUCCESFULLY UPLOADED", "SUCCESFULLY UPLOADED");
            }catch(Exception ex){
                ex.printStackTrace();
            }


        return null;

    }

    protected void onPostExecute(String list) {
        dialog.dismiss();

    }
}
