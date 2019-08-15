RandomAccessFile f = new RandomAccessFile("partial-key.dat", "r");
   byte[] pkey = new byte[(int)f.length()];
   f.read(pkey);

   byte[] key = new byte[16];

   RandomAccessFile f1 = new RandomAccessFile("ciphertext2.dat", "r");
   byte[] cipher_text = new byte[(int)f1.length()];
   f.read(cipher_text);


    for(int copy=0; copy<12;copy++){
        key[copy] = pkey[copy];
    }
    for(int br =0 ; br <=4 ; br++){
        byte[] integ = intToByteArray(br);
        for(int ii =0 ; ii<4; ii++){
            key[12+ii] = integ[ii];
        }


    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);

    byte[] text_byte = cipher.doFinal(cipher_text);
