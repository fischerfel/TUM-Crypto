`public void clickLogin() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {


            URL url = new URL ("https://31.21.18.222/room_info/x.txt");
            HttpsURLConnection connection = null;
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);        //Make an empty store
            InputStream fis = new FileInputStream("C:/Documents and Settings/user/Desktop/PK/localhost.crt"); 
            BufferedInputStream bis = new BufferedInputStream(fis);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                java.security.cert.Certificate cert = cf.generateCertificate(bis);
                keyStore.setCertificateEntry("localhost", cert);
            }

            // write code for turning off client verification
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, tmf.getTrustManagers() , null);
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            SSLSocketFactory sslsf = context.getSocketFactory();
            SSLSocket skt = (SSLSocket)sslsf.createSocket("https://31.21.18.222/room_info/x.txt" , 443);
            skt.setUseClientMode(true);
            SSLSession s = skt.getSession(); // handshake implicitly done
            skt.setKeepAlive(true);


            connection = (HttpsURLConnection) url.openConnection();

        // Host name verification off
            connection.setHostnameVerifier(new HostnameVerifier()  
            {        
                public boolean verify(String hostname, SSLSession session)  
                {  
                    return true;  
                }  
            });  `
