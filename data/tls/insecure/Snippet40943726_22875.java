try{

 URL url = new URL(reqUrl);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            SSLContext sc;

            sc = SSLContext.getInstance("TLS");


  sc.init(null, null, new java.security.SecureRandom());

            conn.setSSLSocketFactory(sc.getSocketFactory());

            String userpass = "username" + ":" + "password";

            String basicAuth = "Basic " +Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);

conn.setRequestProperty("Authorization", basicAuth);


          conn.setRequestMethod("GET");

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } 


      catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
