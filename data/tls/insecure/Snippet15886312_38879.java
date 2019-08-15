public int uploadFile(String sourceFileUri, Context context) {
          String upLoadServerUri = "https://10.112.72.217/upload_test/upload_media_test.php";
          String fileName = sourceFileUri;

          HttpsURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
          if (!sourceFile.isFile()) {
           Log.e("uploadFile", "Source File Does not exist");
           return 0;
          }


              try {

                  TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                InputStream clientTruststoreIs = context.getResources().openRawResource(R.raw.clienttruststore);
                  KeyStore truststore = null;
                  truststore = KeyStore.getInstance("BKS");
                  truststore.load(clientTruststoreIs, "meghna".toCharArray());


                  trustManagerFactory.init(truststore);
                  System.out.println("Loaded server certificates: " + truststore.size());


//                  //final KeyStore keystore = this.loadStore(context.getResources().openRawResource(R.raw.client, "meghna", "BKS");
//                  KeyStore keystore = null;
//                  try {
//                      InputStream clientTruststoreIs2 = context.getResources().openRawResource(R.raw.client);
//                      
//                      keystore = KeyStore.getInstance("BKS");
//                      keystore.load(clientTruststoreIs2, "meghna".toCharArray());
//                      System.out.println("Loaded server certificates: " + keystore.size());
//                  } catch (KeyStoreException e) {
//                      // TODO Auto-generated catch block
//                      e.printStackTrace();
//                  }

                  // open a URL connection to the Servlet

//                  TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
//                  tmf.init(keystore);


                  SSLContext context2 = SSLContext.getInstance("TLS");
                  context2.init(null, trustManagerFactory.getTrustManagers(), null);

                  URL url = new URL(upLoadServerUri);
                  conn = (HttpsURLConnection) url.openConnection();
                  conn.setSSLSocketFactory(context2.getSocketFactory());
                  InputStream in = conn.getInputStream();




                  FileInputStream fileInputStream = new FileInputStream(sourceFile);




               conn.setDoInput(true); // Allow Inputs
               conn.setDoOutput(true); // Allow Outputs
               conn.setUseCaches(false); // Don't use a Cached Copy
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Connection", "Keep-Alive");
               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
               conn.setRequestProperty("uploaded_file", fileName); 
               dos = new DataOutputStream(conn.getOutputStream());

               dos.writeBytes(twoHyphens + boundary + lineEnd); 
               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
               dos.writeBytes(lineEnd);

               bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

               bufferSize = Math.min(bytesAvailable, maxBufferSize);
               buffer = new byte[bufferSize];

               // read file and write it into form...
               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

               while (bytesRead > 0) {
                 dos.write(buffer, 0, bufferSize);
                 bytesAvailable = fileInputStream.available();
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);               
                }

               // send multipart form data necesssary after file data...
               dos.writeBytes(lineEnd);
               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

               // Responses from the server (code and message)
               serverResponseCode = conn.getResponseCode();
               String serverResponseMessage = conn.getResponseMessage();

               Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
               if(serverResponseCode == 200){
                   runOnUiThread(new Runnable() {
                        public void run() {
                            tv.setText("File Upload Completed.");
                            Toast.makeText(Mainactivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });                
               }    

               //close the streams //
               fileInputStream.close();
               dos.flush();
               dos.close();

          } catch (MalformedURLException ex) {  
              dialog.dismiss();  
              ex.printStackTrace();
              Toast.makeText(Mainactivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
          } catch (Exception e) {
              dialog.dismiss();  
              e.printStackTrace();
              //Toast.makeText(Mainactivity.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
              Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
          }
          dialog.dismiss();       
          return serverResponseCode;  
         } }
