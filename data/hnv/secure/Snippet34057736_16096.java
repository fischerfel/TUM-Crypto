try {

            URL url;
            // get URL content
            url = new URL(apiURL);
            System.setProperty("http.keepAlive", "false");
            trustAllHosts();

            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Connection", "close");

            conn.setHostnameVerifier(DO_NOT_VERIFY);
            conn.setRequestMethod("GET");

            conn.setRequestProperty(MainActivity.API_TOKEN, MainActivity.ENCRYPTED_TOKEN);

            conn.setConnectTimeout(15000);
            conn.setUseCaches(false);

            in=conn.getInputStream();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while ((line=br.readLine())!= null) {
                builder.append(line);
            }

            result=builder.toString();

            br.close();


        } catch (MalformedURLException e) {
            result=null;
        } catch (java.net.SocketTimeoutException e) {
            result=null;
        } catch (IOException e) {
            result=null;
        } catch (Exception e) {
            result=null;
        }
