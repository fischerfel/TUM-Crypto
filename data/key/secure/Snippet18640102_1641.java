SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(key
           .toCharArray()), algorithm);
Cipher cipher = Cipher.getInstance(algorithm);
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] encrypted = cipher.doFinal(message.getBytes());              
return new String(Hex.encodeHex(encrypted));
