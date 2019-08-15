try {
            url = new URL("##Edited Out##");
            conn = (HttpsURLConnection) url.openConnection();

            // Create the SSL connection
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());

            // Use this if you need SSL authentication
            String userpass = "##Edited Out##:##Edited Out##";
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json;odata=verbose");

            // set Timeout and method
            conn.setReadTimeout(7000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();

            JSONObject juo = new JSONObject();

            for (int i = 0; i<objects.size(); i++){

                juo.put("Field1", "Data for field 1 here");
                juo.put("Field2", "Data for field 2 here");
                juo.put("Field3", "Data for field 3 here");

                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(juo.toString());
                wr.flush();
                wr.close();

                Log.d("ITEM STRING", juo.toString());

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                Log.d("HTTP BR", "" + sb.toString());

                jObject = XML.toJSONObject(sb.toString());

                Log.d("JSON OBJECT", jObject.toString());

                JSONObject menuObject = jObject.getJSONObject("entry");
                JSONObject contentObject = menuObject.getJSONObject("content");
                JSONObject propertiesObject = contentObject.getJSONObject("m:properties");
                JSONObject productObject = propertiesObject.getJSONObject("d:Product_Id");
                String retProId = productObject.getString("content");

                partIDs.add(retProId);

                Log.d("PART ID", retProId);
            }

            int HttpResult = conn.getResponseCode();
            Log.d("HTTP RESULT", String.valueOf(HttpResult));
            Log.d("HTTP RESPONSE", conn.getResponseMessage());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
