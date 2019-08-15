        public static String decrypt(byte[] pin, byte [] desKeyData ) throws Exception {
    //if (ISOConstantsLibrary.DEBUG) System.out.println("original: " + pin + " key: " + ISOUtil.bcd2str(desKeyData, 0, 2 * desKeyData.length, false) );
    String out = "";

    try {           
        SecretKeySpec desKey = new SecretKeySpec(desKeyData, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");//DES/CBC/PKCS5Padding
        byte[] encrypted_password = pin;
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        byte[] decrypted_password = cipher.doFinal(encrypted_password);
        out = new String(decrypted_password);
        //if (ISOConstantsLibrary.DEBUG) System.out.println("Decrypted Password " + out);
    }
    catch (Exception e) {
        e.printStackTrace();
    }

    return out;
}
