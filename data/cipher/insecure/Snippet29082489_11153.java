byte[] encoded = String.valueOf(fileIn.nextLine()).getBytes();//Key data
key = new SecretKeySpec(encoded, "AES");
dcipher = Cipher.getInstance("AES");
dcipher.init(Cipher.DECRYPT_MODE, key);
