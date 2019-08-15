    byte[] key = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    byte[] data = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    SecretKeySpec encryptionKey = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);

    long start = System.currentTimeMillis();
    for (int i=0; i<10000000; i++)
    {
        cipher.doFinal(data);
    }
    long end = System.currentTimeMillis();      

    System.out.println("took: "+ (end-start));
