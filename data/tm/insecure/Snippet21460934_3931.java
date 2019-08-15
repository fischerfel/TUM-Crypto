FTPSClient ftpClient = new FTPSClient("TLS", false);
try {
    TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };

    ftpClient.setTrustManager(trustManager[0]);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(null, null);
    KeyManager km = kmf.getKeyManagers()[0];
    ftpClient.setKeyManager(km);
    ftpClient.setBufferSize(1024 * 1024);
    ftpClient.setConnectTimeout(100000);
    ftpClient.connect(InetAddress.getByName("ipaddress"), 990);
    ftpClient.setSoTimeout(100000);

    if (ftpClient.login("user", "password")) {
        ftpClient.execPBSZ(0);
        ftpClient.execPROT("P");
        ftpClient.changeWorkingDirectory("/");
        // 250 = directory succesfully changed
        if (ftpClient.getReplyString().contains("250")) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            BufferedInputStream buffIn = null;

            for (File picture : pictures) {
                buffIn = new BufferedInputStream(new FileInputStream(picture.getAbsolutePath()));
                boolean result = ftpClient.storeFile(picture.getName(), buffIn);
                try {
                    buffIn.close();
                } catch (Exception e) {
                }
                if (result)
                    picture.delete();
            }
        }
    }

} catch (SocketException e) {
    Log.e("APPTAG", e.getStackTrace().toString());
} catch (UnknownHostException e) {
    Log.e("APPTAG", e.getStackTrace().toString());
} catch (IOException e) {
    Log.e("APPTAG", e.getStackTrace().toString());
} catch (Exception e) {
    Log.e("APPTAG", e.getStackTrace().toString());
} finally {
    try {
        ftpClient.logout();
    } catch (Exception e2) {
    }
    try {
        ftpClient.disconnect();
    } catch (Exception e2) {
    }
}
