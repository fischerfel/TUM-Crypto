Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

byte[] encypted = new byte[cipher.getOutputSize(fileData.length)];
int len = cipher.update(fileData, 0, fileData.length, encypted, 0);
len += cipher.doFinal(encypted, len);
