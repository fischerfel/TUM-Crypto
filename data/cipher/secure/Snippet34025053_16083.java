cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // or better "AES/GCM/NoPadding"
stream = new CipherInputStream(cipher);
