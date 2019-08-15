public void sendPhoto(int selectFolderId, String photoName,
            String photoBinary, String folderName) {

        HttpURLConnection http = null;
        URL url;
        try {
            JSONObject json = new JSONObject();
            json.put("session", mAuthKey);
            json.put("folderid", selectFolderId);
            json.put("photoname", photoName);
            json.put("photobinary", photoBinary);
            Log.e(TAG, "sendPhoto : folderid:" + selectFolderId);
            Log.e(TAG, "sendPhoto : photoname:" + photoName);
            Log.e(TAG, "sendPhoto : photobinary:" + photoBinary);
            String postParameters = json.toString();
            Log.e(TAG, "sendPhoto:" + "postParameters=" + postParameters);
            url = new URL(URLPREFIX + "sendphoto");
            Log.e(TAG, "sendPhoto url :" + url.toString());
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url
                    .openConnection();

            https.setDoOutput(true);

            https.setRequestProperty("Content-Type", "text/plain");
            // https.setChunkedStreamingMode(0); // Use default chunk size
            https.setHostnameVerifier(DO_NOT_VERIFY);
            https.setRequestMethod("POST");
            https.connect();

            // http = https;

            // Write serialized JSON data to output stream.
            OutputStream out = new BufferedOutputStream(https.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, "UTF-8"));
            writer.write(postParameters.toString());

            // Close streams and disconnect.
            writer.close();
            out.close();
            int code = https.getResponseCode();
            Log.e(TAG, "sendPhoto : code:" + code);
            http = https;
            reader = new BufferedReader(new InputStreamReader(
                    https.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");

            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            Log.e(TAG, "sendPhoto :jsonObject :" + jsonObject.toString());
            mSuccess = jsonObject.getInt("Status");
            Log.e(TAG, "sendPhoto : mSuccess: " + mSuccess);

            String status = "";
            if (mSuccess == 1) {
                // // UPDATE IMAGE STATUS IN DB
                // boolean updateStatus = updateImgStatus(photoName, folderName,
                // "1");
                // if (updateStatus) {
                // Log.e(TAG, "sendPhoto : mSuccess : updateStatus:image :"
                // + photoName + " is sychronized");
                // } else {
                // Log.e(TAG, "sendPhoto : mSuccess : updateStatus:image :"
                // + photoName + " is sychronized fails");
                // }
                // Log.e(TAG, "sendPhoto : mSuccess :1");
                status = endSync(selectFolderId, folderName);
            } else if (mSuccess == 0) {

            } else if (mSuccess == -1) {
            } else {
                //
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "sendPhoto : MalformedURLException" + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "sendPhoto : JSONException" + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "sendPhoto : IOException" + e.toString());
            e.printStackTrace();
        } finally {
            try {

                reader.close();
            }

            catch (Exception ex) {
                mSuccess = 0;
            }
        }

    }
