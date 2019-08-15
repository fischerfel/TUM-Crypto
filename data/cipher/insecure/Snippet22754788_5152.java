final Path origFile = Paths.get("C:\\3.txt");
final byte[] contents = Files.readAllBytes(origFile);

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

   byte[] encrypted = cipher.doFinal(contents.toString().getBytes());

   System.out.println("encrypted string: " + encrypted.toString());

   cipher.init(Cipher.DECRYPT_MODE, skeySpec);
   byte[] original =cipher.doFinal(encrypted);

   String originalString = new String(original);
   System.out.println("Original string: " +originalString);

   final Path newFile = Paths.get("C:\\3encrypted.aes");
   Files.write(newFile, encrypted, StandardOpenOption.CREATE);

     }
