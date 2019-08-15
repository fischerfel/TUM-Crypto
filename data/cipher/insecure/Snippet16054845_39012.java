public static String encrypt(String PT,String skey) throws Exception, NoSuchPaddingException {
      Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(skey.getBytes("ascii"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] val = hexToBytes(PT);
        byte[] encVal = cipher.doFinal(val);

        return byteToString(encVal);
}
