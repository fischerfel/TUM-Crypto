  try {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] buffer = new byte[65536]; 
        InputStream fis = new FileInputStream(downloadFile.getPath());
        int n = 0;
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        fis.close();
        byte[] digestResult = digest.digest();
       log("CheckSum : " + byteArray2Hex(digestResult));
    } catch (Exception e) {
        log("Exception : " + e.getLocalizedMessage());
    }
