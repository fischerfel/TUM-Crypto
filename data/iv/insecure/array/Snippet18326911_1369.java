String message = textField.getText();
// generate a key
KeyGenerator keygen = KeyGenerator.getInstance("AES");
keygen.init(128);  // To use 256 bit keys, you need the "unlimited strength" encryption policy files from Sun.
byte[] key = keygen.generateKey().getEncoded();
SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

// build the initialization vector.  This example is all zeros, but it 
// could be any value or generated using a random number generator.
byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
IvParameterSpec ivspec = new IvParameterSpec(iv);

// initialize the cipher for encrypt mode
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

// encrypt the message
byte[] encrypted = cipher.doFinal(message.getBytes());
System.out.println("Ciphertext: " + encrypted + "\n");
System.out.println(encrypted);
out.println(encrypted);
textField.setText(""); 
