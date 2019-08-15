 private boolean copiarArchviosFtp(){

    FTPClient ftpDestino=new FTPClient();

    try{
        TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        ftpDestino.setSSLSocketFactory(sslSocketFactory);
        ftpDestino.setSecurity(FTPClient.SECURITY_FTPS);

        ftpDestino.connect("172.24.1.109",22);
        ftpDestino.login("user","password");
        ftpDestino.changeDirectory("/home/");


        FTPFile[] listFilte=ftpDestino.list();
        for(FTPFile ftpFile:listFilte){
            System.out.println("nameFile: "+ftpFile.getName());
        }

    }
    catch(Exception e){
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return false;
}
