Security.insertProviderAt(new BouncyCastleProvider(), 1);
Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA256AndMGF1Padding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
return cipher.doFinal(data);
