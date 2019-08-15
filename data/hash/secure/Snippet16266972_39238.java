String salt = "random232andString";
byte[] bSalt = base64ToByte(salt);
byte[] pass = null;

// pass should be what will be saved to the DB.
pass = getHash("pppppp", bSalt); //user is instance of User class.

private byte[] getHash(String password, byte[] salt) throws NoSuchAlgorithmException,     UnsupportedEncodingException {
MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();
    digest.update(salt);
    byte[] securePassword = digest.digest(password.getBytes("UTF-8"));

    return securePassword;
}

public static byte[] base64ToByte(String data){
       BASE64Decoder decoder = new BASE64Decoder();
       byte[] decoded = null;
       try {
        decoded =  decoder.decodeBuffer(data);
    } catch (IOException e) {
        e.printStackTrace();
    }
       return decoded;
}
