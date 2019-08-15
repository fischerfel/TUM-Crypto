`enter code here`
InputStream caInput =null;
                Certificate ca; 
                SSLContext context = null;

                    try {
                        CertificateFactory cf = CertificateFactory.getInstance("X.509");                         
                        caInput = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getPath()+ "/cert.cer"));
                        ca = cf.generateCertificate(caInput);
                        System.err.println("ca=" + ((X509Certificate) ca).getSubjectDN()); 
                        caInput.close();  
                        String keyStoreType = KeyStore.getDefaultType();
                        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                        keyStore.load(null, null);
                        keyStore.setCertificateEntry("ca", ca); 
                        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();                          
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                        tmf.init(keyStore);  
                        context = SSLContext.getInstance("TLS"); 
                        context.init(null, tmf.getTrustManagers(), null);  

                        URL url = new URL("https://url.url.ph");
                        HttpsURLConnection urlConnection =
                            (HttpsURLConnection)url.openConnection();
                        urlConnection.setSSLSocketFactory(context.getSocketFactory());
                        InputStream in = urlConnection.getInputStream();

                        BufferedReader bnuffIn = new BufferedReader(new InputStreamReader(in));
                        String inputLine;
                        while ((inputLine = bnuffIn.readLine()) != null)
                            System.out.println(inputLine);
                        in.close();

                        String host = "mysmtp.host.com"; 
                        String username = "in.payment@blabla.ph";
                        String password = "password";
                        Properties props = new Properties();
                        props.put("mail.smtps.starttls.enable", true); 
                        props.put("mail.smtps.ssl.enable",true);
                        props.put("mail.smtps.port", 465);
                     //   props.put("mail.smtps.socketFactory.class", "com.example.sendemail.MySSLSocketFactory");
                        props.put("mail.smtps.socketFactory",context.getSocketFactory());
                        props.put("mail.smtps.socketFactory.fallback", false);
`enter code here`Session session = Session.getInstance(props); 
