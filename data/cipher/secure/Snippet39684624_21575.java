Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.ENCRYPT_MODE, sks);

outputStream.write(cipher.getIV()); // store the generated IV

CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);
