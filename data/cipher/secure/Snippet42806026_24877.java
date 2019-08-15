    Cipher cipher = Cipher.getInstance("ECIESwithAES-CBC", getInstanceProvider());
    cipher.init(Cipher.ENCRYPT_MODE,ecPublicKey1,new SecureRandom());
    byte[] var22 = cipher.doFinal(data);
    String s = Hex.toHexString(var22);
    System.out.println(s);
