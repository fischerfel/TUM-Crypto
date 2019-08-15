// use first 8 bytes as nonce
Arrays.fill(nonceAndCounter, (byte) 0);
System.arraycopy(nonceBytes, 0, nonceAndCounter, 0, 8);

IvParameterSpec ivSpec = new IvParameterSpec(nonceAndCounter);
Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

cipher.init(cipherMode, secretKey, ivSpec);
