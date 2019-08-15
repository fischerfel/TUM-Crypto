ByteArrayOutputStream baos = new ByteArrayOutputStream();
ImageIO.write(image, "png", baos);
SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance( ... );
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
output = cipher.doFinal(baos.toByteArray());
