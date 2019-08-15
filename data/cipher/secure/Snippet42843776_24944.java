Cipher aesCTR = Cipher.getInstance("AES/CTR/NoPadding");
SecretKey aesKey = new SecretKeySpec(new byte[16], "AES");
IvParameterSpec lastIV = new IvParameterSpec(Hex.decode("FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF"));
aesCTR.init(Cipher.ENCRYPT_MODE, aesKey, lastIV);
byte[] twoBlocks = aesCTR.doFinal(new byte[2 * aesCTR.getBlockSize()]);
byte[] secondBlock = Arrays.copyOfRange(twoBlocks, 16, 32);
System.out.printf("%s%n", Hex.toHexString(secondBlock));

IvParameterSpec firstIV = new IvParameterSpec(new byte[16]); // all zero IV
aesCTR.init(Cipher.ENCRYPT_MODE, aesKey, firstIV);
byte[] oneBlock = aesCTR.doFinal(new byte[aesCTR.getBlockSize()]);
System.out.printf("%s%n", Hex.toHexString(oneBlock));
