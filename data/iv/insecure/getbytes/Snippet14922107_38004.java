byte[] data = Base64
    .decodeBase64("McBeY73GQ5fawxIunVKpqUupipeRlt9ntyMRzjbPfTI=");
byte[] keyByte = "f931c96c4a4e7e47".getBytes("UTF-8");
byte[] ivByte = "1cc251f602cf49f2".getBytes("UTF-8");

Key key = new SecretKeySpec(keyByte, "AES");
IvParameterSpec iv = new IvParameterSpec(ivByte);
Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
c.init(Cipher.DECRYPT_MODE, key, iv);
byte[] bval = c.doFinal(data);

System.out.println(new String(bval)); // Prints My f awesome test !
