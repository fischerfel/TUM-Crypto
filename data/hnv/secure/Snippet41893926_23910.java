public static String handleData(InputStream inp, String host, String keyStore, String keyPwd, String trustStore, String trustPass, boolean userDirectUrl) throws Exception{

      System.setProperty("javax.net.ssl.keyStore", keyStore);
      System.setProperty("javax.net.ssl.keyStorePassword", keyPwd);
      System.setProperty("javax.net.ssl.trustStore", trustStore);
      System.setProperty("javax.net.ssl.trustStorePassword", trustPass);
      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
      System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");

      URL url = null;
      String method = "POST";
      // open HTTPS connection
      HttpURLConnection connection = null;
      if(!userDirectUrl)
      {
          String destinationHost = host;
          int destinationPort = 443;
          url = new URL("https://" + destinationHost + ':' + destinationPort+ "/ESB/DocumentInterface");
      }
      else
      {
          url = new URL(host);
      }
      if(url.getProtocol().equalsIgnoreCase("https"))
      {
          connection = (HttpsURLConnection) url.openConnection();
          ((HttpsURLConnection) connection).setHostnameVerifier(new MyHostnameVerifier());
          connection.setRequestProperty("Content-Type","text/plain; charset=\"utf8\"");
      }
      else
      {
          connection = (HttpURLConnection) url.openConnection();
          //((HttpsURLConnection) connection).setHostnameVerifier(new MyHostnameVerifier());
          connection.setRequestProperty("Content-Type","text/xml; charset=\"utf8\"");
      }

      connection.setRequestMethod(method);

    // POSTS XML to HTTPS output stream
      if (inp != null) {
          connection.setDoOutput(true);
          OutputStream outs = connection.getOutputStream();
          byte[] buffer = new byte[BUFSIZ];
          int nbRead;
          do {
              nbRead = inp.read(buffer);
              if (nbRead > 0) {
                  outs.write(buffer, 0, nbRead);
              }
          } while (nbRead >= 0);
          outs.close();
      }

      //Check return code, if 200 read xml else throw error
      int returnCode = connection.getResponseCode();
      InputStream connectionIn = null;
      if (returnCode == 200)
          connectionIn = connection.getInputStream();
      else
          connectionIn = connection.getErrorStream();

      // print resulting stream
      BufferedReader buffer = new BufferedReader(new InputStreamReader(
              connectionIn));
      String inputLine;
      StringBuffer buf = new StringBuffer();
      while ((inputLine = buffer.readLine()) != null) {
          buf.append(inputLine);
      }
      buffer.close();
      return buf.toString();
  }
