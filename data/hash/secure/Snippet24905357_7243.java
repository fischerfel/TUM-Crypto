public String getHash(String message) {
    String algorithm = "SHA384";
    String hex = "";
    try {
        byte[] buffer = message.getBytes();
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(buffer);
        byte[] digest = md.digest();

        for(int i = 0 ; i < digest.length ; i++) {
            int b = digest[i] & 0xff;
            if (Integer.toHexString(b).length() == 1) hex = hex + "0";
            hex  = hex + Integer.toHexString(b);
        }
        return hex;
    } catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }

}
