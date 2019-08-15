//DECRYPTION
System.out.println(line);
line = line.substring(8);
System.out.println(line);

// generate a key
KeyGenerator keygen = KeyGenerator.getInstance("AES");
keygen.init(128);  // To use 256 bit keys, you need the "unlimited strength" encryption policy files from Sun.
byte[] key = keygen.generateKey().getEncoded();
SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

// build the initialization vector.  This example is all zeros, but it 
// could be any value or generated using a random number generator.
byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
IvParameterSpec ivspec = new IvParameterSpec(iv);

// reinitialize the cipher for decryption
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

// decrypt the message
byte[] decrypted = cipher.doFinal(line.getBytes());
System.out.println("Plaintext: " + new String(decrypted) + "\n");
messageArea.append(name + ": " + decrypted + "\n");
messageArea.setCaretPosition(messageArea.getDocument().getLength());
