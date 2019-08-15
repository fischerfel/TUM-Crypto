urlConnection.setSSLSocketFactory(factory);     
urlConnection.setHostnameVerifier(new AllowAllHostnameVerifier() );

String dateText = "{\"loopParam\":\"" + String.valueOf(d.getHours()) + ":" +   String.valueOf(d.getMinutes()) + ":" + String.valueOf(d.getSeconds())  + "\"}";
                txtOutput.setText("Sending " +     String.valueOf(dateText.length() ) + " bytes of JSON to /pulse/loop" );

urlConnection.addRequestProperty("Content-type", "application/json");
                urlConnection.setDoOutput(true);
urlConnection.setRequestProperty("Proxy-connection", "Keep-Alive");
urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setReadTimeout(30000);
                urlConnection.setRequestMethod("POST");
                DataOutputStream dataOut = new     DataOutputStream(urlConnection.getOutputStream());
                dataOut.writeBytes(dateText);
                dataOut.flush();

BufferedReader bufIn = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String sResponse;
            StringBuilder s = new StringBuilder();

         //bufIn is null as error as closed urlcConnection
            while ((sResponse = bufIn.readLine()) != null) {
                s = s.append(sResponse);
            }
