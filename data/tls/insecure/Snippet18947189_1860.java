    System.setProperty("java.security.krb5.conf", workareaFolder+"/"+props.getProperty("kerberos.conf.file"));
    System.setProperty( "java.security.auth.login.config", workareaFolder+"/"+props.getProperty("jass.conf.file"));
    System.setProperty( "javax.security.auth.useSubjectCredsOnly", "false");           

    krb5MechOid    = new Oid("1.2.840.113554.1.2.2");
    spnegoMechOid  = new Oid("1.3.6.1.5.5.2");

      shost= targetSPN.toLowerCase();
      if (shost.startsWith("http/") || shost.startsWith("cifs/") )  {
          shost = shost.substring(5);
      }  
      else  {
          log.debug("Entered invalid SPN.  Must begin with HTTP/ or CIFS/");
          System.exit(-1);
      }
      this.checkSPNHostname(shost);                     

    //login to the KDC using JAAS login module
    this.subject = login(username, password);

    log.debug(this.subject);

    SSLContext sslContext = SSLContext.getInstance("SSL");

    // set up a TrustManager that trusts everything
    sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                        return null;
                }
                public void checkClientTrusted(X509Certificate[] certs,
                                String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs,
                                String authType) {
                }
    } }, new SecureRandom());

    Scheme httpScheme80 = new  Scheme("http", 80,  PlainSocketFactory.getSocketFactory());
    SSLSocketFactory sf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    Scheme httpsScheme = new  Scheme("https", 443, sf);

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpsScheme);
    schemeRegistry.register(httpScheme80);

    // Create Connection Manager instance for use by Httpclient
    cm = new SingleClientConnManager(schemeRegistry);
    HttpParams params = new BasicHttpParams();
    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    httpclient = new DefaultHttpClient(cm,params);
    httpclient.setRedirectStrategy(new DefaultRedirectStrategy());

    File[] listOfFiles = folder.listFiles(new FileFilter() {
                                                public boolean accept(File f) {
                                                    if (f.isFile()) {
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
    String[] url = new String[listOfFiles.length];
    String spTargetFolder = new String();

    totalFilesInReportsFolder = listOfFiles.length;

    for (int i = 0; i < totalFilesInReportsFolder; i++) {
        uploadFileName = listOfFiles[i].getName();
        log.info("\nFile: "+uploadFileName);

        spTargetFolder = this.getSPFolder(uploadFileName);
        spDestinationFolderURL = sharedDocumentsRoot + instanceFolder + "/" + spTargetFolder + "/" + uploadFileName;
        //log.info("Destination URL : " + spDestinationFolderURL);

        httpPut = new HttpPut(new URI(sharedDocumentsRoot + instanceFolder + "/" + spTargetFolder + "/" + uploadFileName));
        httpPut.getParams().setParameter("http.protocol.handle-redirects",true);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(new FileInputStream(listOfFiles[i]), listOfFiles[i].length());
        httpPut.setEntity(inputStreamEntity);
        // Get the service ticket for targetSPN and set it in HttpPut Authorization header
        this.serviceTicket= initiateSecurityContext( targetSPN );
        encodedBytes  = org.apache.commons.codec.binary.Base64.encodeBase64(this.serviceTicket);
        encoded =   new String(encodedBytes);
        httpPut.addHeader("Authorization", "Negotiate " + encoded);

        httpResponse = null;

        try {
             log.info("Uploading File... ");
             log.debug("Executing httpPut request: " + httpPut.getRequestLine());           
             httpResponse = httpclient.execute(httpPut);
             log.debug("After Post - Status code:" +httpResponse.getStatusLine().getStatusCode());
             BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
             StringBuilder s = new StringBuilder();
             String sResponse; 
             while ((sResponse = reader.readLine()) != null) {
                 s = s.append(sResponse);
             }


             if (httpResponse.getStatusLine() != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                 log.info("Response Code received: "+ httpResponse.getStatusLine() +" [200 - CREATE / OVERWRITE]");
                 log.info("File Sucessfully uploaded to Sharepoint location: "+spDestinationFolderURL );
                 uploadSuccessCount++;
             } else {
                 log.error("Error while uploading file to sharepoint");
                 if (httpResponse.getStatusLine() != null && httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED){
                     log.error("Response code: "+httpResponse.getStatusLine().getStatusCode());
                     log.error("Response Text:" + s);
                 }
                 uploadFailCount++;
             }

             log.debug("----------------------------------------");
             log.debug("Response StatusLine: "+ httpResponse.getStatusLine());
             log.debug("----------------------------------------");
             log.debug("Return Code : " + httpResponse.getStatusLine().getStatusCode());
             log.debug("----------------------------------------");

        } catch (Exception exp) {
            log.error("Exception while uploading report \""+uploadFileName+"\" to Share point="+exp);
            exp.printStackTrace();
        }
    }
