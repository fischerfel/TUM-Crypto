SecretKey deskey = new SecretKeySpec(keybyte, "DESede/ECB/NOPADDING");
//enter code here..
Cipher c1 = Cipher.getInstance("DESede/ECB/NOPADDING");
c1.init(Cipher.ENCRYPT_MODE, deskey);
