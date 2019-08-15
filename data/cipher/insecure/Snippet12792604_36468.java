        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        SecretKey sk_1 = kgen.generateKey(); 
        SecretKey sk_2 = kgen.generateKey();
        byte[] raw_1 = sk_1.getEncoded();
        byte[] raw_2 = sk_2.getEncoded();

        spec_1 = new SecretKeySpec(raw_1, "DES"); //key 1
        spec_2 = new SecretKeySpec(raw_2, "DES"); //key 2

        cipher = Cipher.getInstance("DES"); //standard mode is ECB which is block-by-block w/PKCS5Padding
        cipher2 = Cipher.getInstance("DES");


    protected byte[] get3DESEncryption(byte[] plaintext) throws Exception{
        byte[] output = new byte[plaintext.length];
        System.out.println("output len init: " + output.length);
        cipher.init(Cipher.ENCRYPT_MODE, spec_1);
        cipher2.init(Cipher.DECRYPT_MODE, spec_2);

        //first encryption round, key 1 used
        output = cipher.doFinal(plaintext);
        //second "encryption" round, key 2 used but decrypt run
        output = cipher2.doFinal(output);
        //third encryption round, key 1 used
        output = cipher.doFinal(output);

        //return ciphertext
        return output;
    } 
