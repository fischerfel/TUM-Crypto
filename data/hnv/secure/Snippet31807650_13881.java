    //here i am downloading file from server and writing

    String download_link = "some server location"; 
                URL url = new URL(download_link);
                 HttpsURLConnection c = null;
                if (url.getProtocol().toLowerCase().equals("https")) {
                     trustAllHosts();
                     c = (HttpsURLConnection) url.openConnection();
                     c.setHostnameVerifier(DO_NOT_VERIFY);
                }
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "Mobi.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

you need to add permission in manifest file:    

uses-permission android:name="android.permission.INTERNET"></uses-permission>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
