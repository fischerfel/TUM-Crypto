String s_InitKey = "1612211310164660";
String s_IvSpec = "MySecreteBytes00";

IvParameterSpec iv = new IvParameterSpec(s_IvSpec.getBytes("UTF-8"));
SecretKeySpec key = new SecretKeySpec(s_InitKey.getBytes("UTF-8"), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, key, iv);
