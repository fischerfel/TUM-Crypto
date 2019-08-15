        URL url = new URL("Your URL");
        HttpsURLConnection urlConnection =(HttpsURLConnection) url.openConnection();    urlConnection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
        urlConnection.setHostnameVerifier(getHostnameVerifier());
        InputStream is = urlConnection.getInputStream();
        OutputStream os = new FileOutputStream(downloadedFile);
        byte[] data = new byte[1024];
        int count;
        while ((count = is.read(data)) != -1) {
            os.write(data, 0, count);
        }
        os.flush();
        os.close();
        is.close();
