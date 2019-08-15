byte[] encodeBytes = null;
encodeBytes = Base64.encode(my_encrypted_string.getBytes(),Base64.DEFAULT);
Cipher c = Cipher.getInstance("ECIES","SC");
c.init(Cipher.DECRYPT_MODE,privateKeyFromFile);
decodeBytes = c.doFinal(encodeBytes);
String deCrypt = new String(decodeBytes,"UTF-8");
txtHiden.setText(deCrypt);
Toast.makeText(activity, deCrypt, Toast.LENGTH_SHORT).show();
