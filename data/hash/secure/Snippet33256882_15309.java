  public static String calculateSH256(String secret){
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = secret.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            String sig = bytesToHex(digest.digest());
            return sig;
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
         throw new RuntimeException("Cannot calculate signature");  
        }          
    }


    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
