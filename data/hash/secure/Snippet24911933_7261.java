MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(yourEmail.getBytes());
byte[] encryptionKey = ms.digest();
