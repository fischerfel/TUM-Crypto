public CryptStuff(String password) throws zillion_exceptions {
    if (password==null) throw new InvalidKeyException("No encryption password is set!");
    key = new SecretKeySpec(password.getBytes("UTF-8"), "AES");
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    ivSpec=new IvParameterSpec(new byte[cipher.getBlockSize()]);
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
}
