Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING", "SunJCE"); 
cipher.init(Cipher.DECRYPT_MODE, pub); //exception !
