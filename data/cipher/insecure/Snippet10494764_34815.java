int result_len = 0;
result_len = din.readInt();            
byte[] result_Bytes = new byte[result_len];
din.readFully(result_Bytes);
cipher2 = Cipher.getInstance("AES");
cipher2.init(Cipher.DECRYPT_MODE, aesKey);             
byte[] encrypt = cipher2.doFinal(result_Bytes);
