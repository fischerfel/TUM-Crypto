        String text = "Hello World";
        String key = "Bar12345kjkj5454hggx1234"; 

        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "DESede");
        Cipher cipher = Cipher.getInstance("DESede");

        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        System.err.println("Using Tripple DES algorithm and with key <"+key+">, <"+text+">  converted into <"+new String(encrypted)+">");

        // decrypt the text
        String key1 = "Bar12345kkkj5454hggx1234"; // 128 bit key
        Key aesKey1 = new SecretKeySpec(key1.getBytes(), "DESede");
        Cipher cipher1 = Cipher.getInstance("DESede");


        cipher1.init(Cipher.DECRYPT_MODE, aesKey1);
        String decrypted = new String(cipher1.doFinal(encrypted));
        System.err.println("Using Tripple DES algorithm and with key <"+key1+">, encrypted text <"+new String(encrypted)+"> decrypted into <"+decrypted+">");
