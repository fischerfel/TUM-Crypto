 SecretKeySpec skeySpec = new SecretKeySpec(Constant.RC2_KEY.getBytes("US-ASCII"), "PBEWITHSHAAND128BITRC2-CBC");
 IvParameterSpec iv = new IvParameterSpec(Constant.RC2_IV.getBytes("US-ASCII"));
 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
 cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
 byte[] encrypted = cipher.doFinal(data);
 return encrypted;
