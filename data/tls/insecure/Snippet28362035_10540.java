SSLSocketFactory buildSSLSocketFactory() throws Exception {
    SSLContext sslcontext = null;

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    KeyStore trustks = KeyStore.getInstance("JKS");
    File trustcert = new File("path to truststore");
    InputStream truststream = new FileInputStream(trustcert);
    trustks.load(truststream, "password".toCharArray());
    truststream.close();
    tmf.init(trustks);

    try {
        sslcontext = SSLContext.getInstance("TLS");

        sslcontext.init(new KeyManager[0],
                 tmf.getTrustManagers() ,
                new SecureRandom());
    } catch (NoSuchAlgorithmException e) {  
        System.out.println("Exception  :"+e);       
    } catch (KeyManagementException e) {
        System.out.println("Exception  :"+e);   
    }

    SSLSocketFactory factory = sslcontext.getSocketFactory();

    return factory; 
}
