static byte[] bytes = new byte[16];

public static byte[] encrypt(String key, String message) {
    try {
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        System.out.println("Outputting generated IV:");
        for(int i=0; i < bytes.length; i++){
            System.out.println(bytes[i]);
        }

        IvParameterSpec iv = new IvParameterSpec(bytes);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = Base64.encodeBase64(cipher.doFinal(message.getBytes()));
        System.out.println("encrypted string: "
                + Base64.encodeBase64String(encrypted));

        byte[] sendMe = new byte[bytes.length + encrypted.length];
        System.arraycopy(bytes, 0, sendMe, 0, bytes.length);
        System.arraycopy(encrypted, 0, sendMe, 0, encrypted.length);

        return  sendMe;
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}

public static String decrypt(String key, byte[] received) {
    try {

        byte[] initVector = Arrays.copyOfRange(received, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(received, 16, received.length+1);

        System.out.println("Outputting received IV:");
        for(int i = 0; i < initVector.length; i++){
            System.out.println(initVector[i]);
        }

        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

        return new String(original);
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}
