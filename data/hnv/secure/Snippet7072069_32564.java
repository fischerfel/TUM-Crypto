public ContainerData submitPhoto(FileInputStream fileInputStream, String sessionKey) {

    try {

        URL url = new URL(API_URL);

        HttpURLConnection conn = null;

        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }


        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        // HttpURLConnection conn = (HttpURLConnection)
        // connectURL.openConnection();

        // Allow Inputs
        conn.setDoInput(true);

        // Allow Outputs
        conn.setDoOutput(true);

        // Don't use a cached copy.
        conn.setUseCaches(false);

        // Use a post method.
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Connection", "Keep-Alive");

        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "file.png" + "\"" + lineEnd);
        dos.writeBytes(lineEnd);

        // create a buffer of maximum size

        int bytesAvailable = fileInputStream.available();
        int maxBufferSize = 1028;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            /*
             * dos.writeBytes(lineEnd); dos.writeBytes(twoHyphens + boundary
             * + twoHyphens + lineEnd);
             */
        }

        // send multipart form data necesssary after file data...
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        dos.flush();
        // Log.d(TAG, "  dos5: " + dos.toString());
        InputStream is = conn.getInputStream();
        // retrieve the response from server
        int ch;

        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }
        String stringResponse = b.toString();
        // Log.d(TAG, "http response for upload" + s);
        dos.close();

        Gson gson = new GsonBuilder().serializeNulls().create();
        responseObject = gson.fromJson(stringResponse,ContainerData.class);

        JSONObject data = new JSONObject(stringResponse);
        String dataResponse = data.getString("data");
        responseObject.setDataString(dataResponse);


    } catch (JSONException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return responseObject;

}
