public byte[] encrypt(byte[] key, byte[] pText) throws Exception
{
    System.out.println( DatatypeConverter.printHexBinary(key)); // Outputs: 3FBB589A6A941D01
    System.out.println( DatatypeConverter.printHexBinary(pText)); // Outputs: 92F3BD61F852727E
    Cipher ciph = Cipher.getInstance("DES");
    SecretKey blah = new SecretKeySpec(key, 0, key.length, "DES");

    ciph.init(Cipher.ENCRYPT_MODE,  blah);
    byte[] test = ciph.doFinal(pText); 
    System.out.println( DatatypeConverter.printHexBinary(test)); // Outputs: 4799F8A1C0A427E17E2B19DD22064444

    return test;
}
