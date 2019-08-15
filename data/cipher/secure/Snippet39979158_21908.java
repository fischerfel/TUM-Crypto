Cipher c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
c.init(Cipher.ENCRYPT_MODE, secretKey);
