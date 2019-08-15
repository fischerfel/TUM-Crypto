       // Get the KeyGenerator

       KeyGenerator kgen = KeyGenerator.getInstance("AES");
       kgen.init(128); // 192 and 256 bits may not be available


       // Generate the secret key specs.
       SecretKey skey = kgen.generateKey();
       byte[] raw = skey.getEncoded();

       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");


       // Instantiate the cipher

       Cipher cipher = Cipher.getInstance("AES");

       cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

       byte[] encrypted =
         cipher.doFinal((args.length == 0 ?
          "This is just an example" : args[0]).getBytes());
       System.out.println("encrypted string: " + asHex(encrypted));

       cipher.init(Cipher.DECRYPT_MODE, skeySpec);
       byte[] original =
         cipher.doFinal(encrypted);
       String originalString = new String(original);
       System.out.println("Original string: " +
         originalString + " " + asHex(original));
