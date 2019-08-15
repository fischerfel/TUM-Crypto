SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
byte[] iv = generateRandomIV();
IvParameterSpec ivspec = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
int outputLength = cipher.getOutputSize(data.length); // Prepare output buffer
byte[] output = new byte[outputLength];
int outputOffset = cipher.update(data, 0, data.length, output, 0);// Produce cipher text
outputOffset += cipher.doFinal(output, outputOffset);
