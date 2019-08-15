 System.out.println("Please type a message to be encrypted:");
       message = scanner.next();


     //create iv array
     byte[] iv = toByteArray("a11f001ed2dec0de6e6f6e73656e7365");

     Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
     SecretKey key = new SecretKeySpec(decryptedKey, "AES");
     IvParameterSpec ivParSpec = new IvParameterSpec(iv);
     aesCipher.init(Cipher.ENCRYPT_MODE, key, ivParSpec);
     byte[] encryptedMessage = aesCipher.doFinal(message.getBytes("UTF-8"));


     dos.writeInt(encryptedMessage.length);
     dos.write(encryptedMessage);
