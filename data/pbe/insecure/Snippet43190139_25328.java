private final static byte[] SALT = { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea,
        (byte) 0xf2 };

PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray(), SALT, 20 , 128);     
    try {
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        PRIVATE_KEY = kf.generateSecret(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException  e) {
        e.printStackTrace();
    }
