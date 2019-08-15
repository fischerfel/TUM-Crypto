 public void uploadFile(Context context, File fileName) {
        FTPClient client = new FTPClient();

        try {
            flag = false;
            if (!client.isConnected()) {
             client.setSecurity(FTPClient.SECURITY_FTPES);
            // client.setSecurity(FTPClient.SECURITY_FTP);
                try {
                    client.connect(FTP_HOST, 21);
                    client.setType(FTPClient.TYPE_BINARY);
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    client.setSSLSocketFactory(sc.getSocketFactory());
                    client.login(FTP_USER, FTP_PASS);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.setType(FTPClient.TYPE_BINARY);

                client.changeDirectory(UPLOAD_PATH);
                client.upload(fileName);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            flag = false;
        }
    }
