private static byte[] getSalt() throws NoSuchAlgorithmException {
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    secureRandom.nextBytes(salt);
    return salt;
}

public static String getHashWithSalt(String userid) {
    String token = null;
    try {
        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        byte[] salt = getSalt();
        msgDigest.reset();
        msgDigest.update(salt);
        msgDigest.update(userid.getBytes());
        byte byteData[] = msgDigest.digest();
        System.out.println("msgDigest value --> " + byteData.toString());
        token = toHexString(byteData);
        System.out.println("token --> " + token);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } 
    return token;
}

// This method converts the byte Array to Hexa String format
private static String toHexString(byte[] byteData) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
        String hex = Integer.toHexString(0xFF & byteData[i]);
        hexString.append(hex);
    }
    return hexString.toString();
}
