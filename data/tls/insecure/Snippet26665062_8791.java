            InputStream certificato = getResources().openRawResource(R.raw.keystore);
            KeyStore trustStore = null;
            try {
                String tfmAlgorithm=TrustManagerFactory.getDefaultAlgorithm();
                trustStore = KeyStore.getInstance("BKS");
                trustStore.load(certificato, "123456".toCharArray());
                TrustManagerFactory tmf=TrustManagerFactory.getInstance(tfmAlgorithm);
                tmf.init(trustStore);
                SSLContext slc=SSLContext.getInstance("SSL");
                slc.init(null,tmf.getTrustManagers(),new SecureRandom());
                SSLSocketFactory fs=slc.getSocketFactory();
                Socket socket = fs.createSocket("10.0.0.2", 25000);

                /*InputStream inputstream = socket.getInputStream();
                InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                BufferedReader bufferedreader = new BufferedReader(inputstreamreader);*/

                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                String sendMessage = "brova";
                bw.write(sendMessage + "\n");
                bw.flush();

                System.out.println("Message sent to the server : " + sendMessage);
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return "OK";
        }
