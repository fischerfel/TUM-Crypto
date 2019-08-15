    public static void test2() throws Exception {

    Security.addProvider(new SunJCE());
    Security.addProvider(new IBMJCE());
    String strKey = "12345678";
    KeyGenerator generator = KeyGenerator.getInstance("DES", "SunJCE");
    // KeyGenerator generator = KeyGenerator.getInstance("DES", "IBMJCE");
    System.out.println("KeyGenerator provider:" + generator.getProvider());
    //
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    secureRandom.setSeed(strKey.getBytes());
    generator.init(secureRandom);
    Key key = generator.generateKey();
    Cipher cipher = Cipher.getInstance("DES", "SunJCE");
    // Cipher cipher = Cipher.getInstance("DES", "IBMJCE");
    System.out.println("Cipher provider:" + cipher.getProvider());
    cipher.init(Cipher.ENCRYPT_MODE, key);
    String strTest = "TESTtest123";
    byte[] byteTest = strTest.getBytes("UTF-8");
    byte[] byteEncry = cipher.doFinal(byteTest);
    System.out.println("strTest:" + strTest);
    System.out.println("encode:" + new BASE64Encoder().encode(byteEncry));


}
