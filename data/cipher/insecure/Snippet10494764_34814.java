cipher2 = Cipher.getInstance("AES"); 
secretKeySpec = new SecretKeySpec(decryptedText, "AES");
cipher2.init(Cipher.ENCRYPT_MODE, secretKeySpec);
feedback = "Your answer is wrong".getBytes();
cipher2.doFinal(feedback);
dos.writeInt(feedback.length);
dos.write(feedback);
