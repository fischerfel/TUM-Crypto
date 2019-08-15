Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE,publicServerKey);
byte[] parametersCipher = cipher.doFinal(parameters.getBytes("UTF-8"));
String encoded=new String(Base64.encode(parametersCipher, Base64.URL_SAFE)); //URL_SAFE from Android Documentation + as - and / as _
String parametersencrypted="data="+encoded;
