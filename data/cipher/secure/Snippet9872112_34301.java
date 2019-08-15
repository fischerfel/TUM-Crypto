Cipher rsaCipher = Cipher.getInstance("RSA");
rsaCipher.init(Cipher.DECRYPT_MODE, key);
byte[] encryptedRijndaelKey = Base64.decodeBase64(base64EncodedSymetricKey); //from the metaData
byte[] rijndaelKeyBytes = rsaCipher.doFinal(encryptedRijndaelKey);
