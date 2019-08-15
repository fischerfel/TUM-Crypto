cipher = Cipher.getInstance("DESede/CBC/NOPADDING");
cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
int outSize = cipher.getOutputSize(cipherText.length);  
plainText = new byte[outSize];
length = cipher.update(cipherText, plainText, 0);
cipher.doFinal(plainText, length);

//Remove padding
int newLen = plainText.length - (plainText[plainText.length-1] & 0xFF);
byte[] data = new byte[newLen];
System.arraycopy(plainText, 0, data, 0, newLen);
