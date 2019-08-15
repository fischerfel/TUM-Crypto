public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

private final WeakReference<ImageView> imageViewReference;
Resources resources = null;


public ImageDownloaderTask(ImageView imageView) {
    imageViewReference = new WeakReference<ImageView>(imageView);
}

@Override
protected Bitmap doInBackground(String... params)
{
    return downloadBitmap(params[0]);
}
@Override
protected void onPostExecute(Bitmap bitmap) {
    if (isCancelled()) {
        bitmap = null;
    }

    if (imageViewReference != null) {
        ImageView imageView = imageViewReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Log.d("Downloading the image: ", "No Image found");
            }
        }

    }
}

//URL connection to download the image
private Bitmap downloadBitmap(String url) {

    HttpURLConnection urlConnection = null;
    HttpsURLConnection urlConnection2 = null;
    try {

        //check to see if the image is coming from a HTTP connection
        //then download via a HTTP connection
        //if not then use a HTTPS connection
        if(url.contains("https"))
        {
            try {
                Log.d("Use HTTPS", url);
                URL urlHTTPS = new URL(url);
                urlConnection2 = (HttpsURLConnection) urlHTTPS.openConnection();

                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = resources.getAssets().open("fusionsystemca.crt");
                Log.d("CA: ", caInput.toString());
                //InputStream caInput = new BufferedInputStream(new FileInputStream(resources.getAssets().open("myca.crt")));
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    caInput.close();
                }

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);
                urlConnection2.setSSLSocketFactory(context.getSocketFactory());

                int statusCode = urlConnection2.getResponseCode();
                Log.d("URL2 Status: " , Integer.toString(statusCode));
                //check if the HTTP status code is equal to 200, which means that it is ok
                if (statusCode != 200) {
                    return null;
                }
                InputStream in = urlConnection2.getInputStream();
                if (in != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    return bitmap;
                }
            }catch (Exception e)
            {
                urlConnection2.disconnect();
                Log.d("ImageDownloader", "Error downloading image from " + url);
            }

        }else
        {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            //check if the HTTP status code is equal to 200, which means that it is ok
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        }

    } catch (Exception e) {
        urlConnection.disconnect();
        Log.d("ImageDownloader", "Error downloading image from " + url);
    } finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if(urlConnection2 != null)
        {
            urlConnection2.disconnect();
        }
    }
    return null;
}

//this is to add the selfsigned cert
