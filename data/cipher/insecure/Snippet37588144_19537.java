byte[] dataBytes = "text to be encrypted".getBytes(StandardCharsets.ISO_8859_1);

Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
SecretKey key = KeyGenerator.getInstance("DES").generateKey();
cipher.init(Cipher.ENCRYPT_MODE, key);

byte[] cipByte = cipher.doFinal(dataBytes);
byte[] encr = Base64.getEncoder().encode(cipByte);

File encryptedFile = new File("/tmp/in.enc");
Files.write(Paths.get(encryptedFile.getAbsolutePath()), encr);

System.out.println("encr length: " + encr.length);
System.out.println("file length: " + encryptedFile.length());
