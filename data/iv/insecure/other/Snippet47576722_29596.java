byte[] payload = ....;
byte[] iv = ....;
byte[] secret = ....; // Now 370 bits
byte[] data = Base64.getDecoder().decode(payload);

Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOfRange(secret, 0, 32), "AES");
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv, 0, cipher.getBlockSize());

cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
byte[] output = cipher.doFinal(data);

System.out.println(new String(output).trim());
