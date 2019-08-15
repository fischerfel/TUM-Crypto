byte[] data = ":Êº$jhk¨ë‹òºÃ"; // fetched from php server..
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, mKeyspec);
return new String(cipher.doFinal(data));
