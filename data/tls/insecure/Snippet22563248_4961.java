           HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();


         // Load the truststore that includes self-signed cert as a "trusted" entry.
            KeyStore truststore;
            truststore = KeyStore.getInstance("BKS");
            InputStream in = getActivity().getResources().openRawResource(R.raw.mykeystore);
            truststore.load(in, "mysecret".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(truststore);

            // Create custom SSL context that incorporates that truststore
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

     connection.setSSLSocketFactory(sslContext.getSocketFactory());


            connection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream(), 8192);

            // Output stream
            OutputStream output = new FileOutputStream("/sdcard/downloadedfile.pdf");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    private ContextWrapper getActivity() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        pDialog.setProgress(Integer.parseInt(progress[0]));
   }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        dismissDialog(progress_bar_type);

        // Displaying downloaded image into image view
        // Reading image path from sdcard
        String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.pdf";
        // setting downloaded into image view
        my_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }

}
}
