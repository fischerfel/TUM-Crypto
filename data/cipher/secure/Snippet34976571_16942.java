try
{
    byte[] pack = new byte[4];
    byte[] pack2;
    byte[] pack3;
    pack[0] = 1;
    pack[1] = 2;
    pack[2] = 3;
    pack[3] = 4;

    System.out.println("START!");
    System.out.println("pack");
    for(int i = 0; i < 4; i++)
    {
        System.out.println(pack[i]);
    }
    System.out.println("");

    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(512);
    KeyPair keyPair = keyPairGenerator.genKeyPair();

    Cipher cip = Cipher.getInstance("RSA");
    cip.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
    // I think you said you wanted the last 3 bytes encrypted
    //                        vvvvvvvvvvvvvvvv
    pack2 = cip.doFinal(pack, 1, pack.length-1);
    System.out.println("pack2");
    for(int i = 0; i < 4; i++)
    {
        System.out.println(pack2[i]);
    }
    System.out.println("");

    Cipher cip2 = Cipher.getInstance("RSA");
    cip2.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
    byte[] pack3Tmp= cip2.doFinal(pack2, 0, pack2.length);
    pack3= new byte[pack.length];
    // Copy over the first byte you had mentioned to be 
    // unecrypted
    pack3[0]= pack[0];
    // Use System.arraycopy
    //               Source is pack3Tmp
    //               |         Starting offset is 0
    //               |         |  Destination is pack3
    //               |         |  |      Staring offset in pack3 is 1
    //               |         |  |      |  Copy all of pack3Tmp
    //               |         |  |      |  |
    //               v         v  v      v  v
    System.arraycopy(pack3Tmp, 0, pack3, 1, pack3Tmp.length);
    System.out.println("pack3");
    for(int i = 0; i < 4; i++)
    {
        System.out.println(pack3[i]);
    }
    System.out.println("");

    System.out.println("END!");
}
catch(Exception e)
{
    e.printStackTrace();
}
