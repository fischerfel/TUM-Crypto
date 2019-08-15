public String calcMD5() throws Exception{
        byte[] buffer = new byte[8192];
        MessageDigest md = MessageDigest.getInstance("MD5");

        DigestInputStream dis = new DigestInputStream(new FileInputStream(new File("Path to file")), md);
        try {
            while (dis.read(buffer) != -1);
        }finally{
            dis.close();
        }

        byte[] bytes = md.digest();

        // bytesToHex-method
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
}
