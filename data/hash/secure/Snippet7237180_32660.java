private static String login(String username, String passwd) {

    //Challenge handshake authentification
    //1st step - get authentification challenge (random long)
    long challenge = MasterdataServices.getAuthentificationChallenge(username);
    if(challenge == 0 || challenge == -1) {
        return null;
    }

    //get hashed password
    String hashedPassword = getHashedPassword(passwd);

    //2nd step - get crc value
    long crcValue = getCrcValue(hashedPassword, challenge);

    //3rd step - get session token
    String sessionId = MasterdataServices.authenticateByPassword(username, crcValue);
    return sessionId;
}

private static long getCrcValue(String password, long challengeValue) {
    CRC32 crc = new CRC32();
    try {
        crc.update(password.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }       
    return crc.getValue() * challengeValue;
}   

private static String getHashedPassword(String password) {
    MessageDigest messageDigest = null;
    try {
        messageDigest = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
    byte[] encryptedPw = null;
    try {
        encryptedPw = messageDigest.digest(password.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
    StringBuilder hex = new StringBuilder(encryptedPw.length * 2);

    for(byte b : encryptedPw)
    {
        if((b & 0xff) < 0x10) hex.append("0");
        hex.append(Integer.toHexString(b & 0xff));
    }               

    return hex.toString();
}   
