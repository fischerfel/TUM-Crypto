Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, mPubKey);
return cipher.doFinal("Hello World".getBytes()); // here is the problem
