JFileChooser chooser = new JFileChooser();
chooser.showOpenDialog(null);
File uploadingFile = chooser.getSelectedFile();
String hostname = JOptionPane.showInputDialog("Hostname?");
String port = JOptionPane.showInputDialog("Port?");
String username = JOptionPane.showInputDialog("Username?");
String uploadPath = "\\path\\to\\file";

try {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException{}
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{}
        public X509Certificate[] getAcceptedIssuers(){return null;}
    };
    sslContext.init(null, new TrustManager[]{tm}, null);

    FTPSClient ftps = new FTPSClient(sslContext);
    //FTPSClient ftps = new FTPSClient("SSL", false);
    //FTPSClient ftps = new FTPSClient(false);
    //ftps.setAuthValue("SSL");
    ftps.addProtocolCommandListener(new PrintCommandListener(System.out));
    //ftps.setBufferSize((int)(uploadingFile.length()*2));
    ftps.connect(hostname, port);
    //ftps.execAUTH("TLS");
    if(!FTPReply.isPositiveCompletion(ftps.getReplyCode())) {
        ftps.disconnect();
        JOptionPane.showMessageDialog(null, "FTPS server refused connection.", "MyFTPSClient", JOptionPane.ERROR_MESSAGE);
    }
    String password = promptPassword();
    while(!ftps.login(username, password)) {
        JOptionPane.showMessageDialog(null, "Incorrect password.", "MyFTPSClient", JOptionPane.ERROR_MESSAGE);
        password = promptPassword();
    }
    ftps.execPBSZ(0);
    ftps.execPROT("P");
    //ftps.setFileType(FTP.ASCII_FILE_TYPE);
    ftps.enterLocalPassiveMode();
    ftps.changeWorkingDirectory(uploadPath);
    //ftps.setFileType(FTP.BINARY_FILE_TYPE);
    InputStream fileStream = new FileInputStream(uploadingFile);
    if(ftps.storeFile(uploadingFile.getName(), fileStream)){
        JOptionPane.showMessageDialog(null, "File successfully uploaded.", "MyFTPSClient", JOptionPane.INFORMATION_MESSAGE);
    }else{
        JOptionPane.showMessageDialog(null, "File not successfully uploaded.", "MyFTPSClient", JOptionPane.ERROR_MESSAGE);
    }
    fileStream.close();
    ftps.logout();
    ftps.disconnect();
} catch(Exception e) {
    System.out.println("Error caught by MyFTPSClient."); e.printStackTrace();
}
