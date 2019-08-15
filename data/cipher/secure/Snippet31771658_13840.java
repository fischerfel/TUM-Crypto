Cipher cipher=Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE,publicServerKey);
byte[] parametersCipher = cipher.doFinal(urlParameters.getBytes("UTF-8"));
String encoded=new String(encoder.encode(parametersCipher)); //encoder= base64 encoder 
encoded=encoded.replace("+", "-");
encoded=encoded.replace("/", "_");
