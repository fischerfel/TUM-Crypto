cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
secretKeySpec = generateKeySpongy(getCMSKey(context));

byte[] ivBytes = {...};
IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
