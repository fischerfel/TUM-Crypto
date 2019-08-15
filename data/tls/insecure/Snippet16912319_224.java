                URL url = new URL(reportInfo.getURI().toString());
                HttpsURLConnection con = (HttpsURLConnection) url
                        .openConnection();


                /*
                 * Keystore manager
                 */
                KeyManagerFactory keyManagerFactory = KeyManagerFactory
                        .getInstance("SunX509");
                String pKeyPassword = GSBConstants.KEYSTORE_PASSWORD;
                KeyStore keyStore = KeyStore.getInstance("JKS");
                InputStream keyInput = new FileInputStream(new File(
                        cc.getWorkingDir() + File.separator
                                + GSBConstants.KEYSTOREJKS_NAME));
                keyStore.load(keyInput, pKeyPassword.toCharArray());
                keyInput.close();
                keyManagerFactory
                        .init(keyStore, pKeyPassword.toCharArray());


                /*
                 * Trustore manager
                 */
                TrustManagerFactory trustManagerFactory = TrustManagerFactory
                        .getInstance("SunX509");
                KeyStore trustStore = KeyStore.getInstance("JKS");
                InputStream trustInput = new FileInputStream(new File(
                        cc.getWorkingDir() + File.separator
                                + GSBConstants.TRUSTORE_NAME));
                String pTrustPassword = GSBConstants.TRUSTORE_PASSWORD;
                trustStore.load(trustInput, pTrustPassword.toCharArray());
                trustInput.close();
                trustManagerFactory.init(trustStore);

                SSLContext context = SSLContext.getInstance("SSL");
                context.init(keyManagerFactory.getKeyManagers(),
                        trustManagerFactory.getTrustManagers(),
                        new SecureRandom());
                SSLContext.setDefault(context);

                SSLSocketFactory sockFact = context.getSocketFactory();
                con.setSSLSocketFactory(sockFact);


                // Check for errors
                int responseCode = con.getResponseCode();
                InputStream inputStream;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }

                OutputStream output = new FileOutputStream("test.zip");

                // Process the response
                BufferedReader reader;
                String line = null;
                reader = new BufferedReader(new InputStreamReader(
                        inputStream));
                while ((line = reader.readLine()) != null) {
                    output.write(line.getBytes());
                }

                output.close();
                inputStream.close();
