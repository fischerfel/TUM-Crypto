RSAPrivateKey sKey = getPrivateKey(keyFile);
Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA512AndMGF1Padding");
cipher.init(Cipher.DECRYPT_MODE, sKey);
