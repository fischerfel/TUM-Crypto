byte[] iv="someString".getBytes();
SecretKeySpec secretKey=new SecretKeySpec(yourPasswordAsString.getBytes(), "AES");
AlgorithmParameterSpec spec=new IvParameterSpec(iv);
Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
byte[] encrypted=cipher.doFinal(plainText.getBytes());
