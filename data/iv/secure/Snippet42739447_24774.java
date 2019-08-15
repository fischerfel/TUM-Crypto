SecureRandom rng = new SecureRandom();
SecretKey aesKey = new SecretKeySpec(new byte[16], "AES");

String chinese = "桃草夹芥人蕉芥玉芥花荷子衣兰芥花";
byte[] utf8Chinese = chinese.getBytes(UTF_8);
System.out.printf("UTF-8    encoded : %d bytes: %s%n", utf8Chinese.length, Hex.toHexString(utf8Chinese));

{
    Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");

    byte[] ivBytes = new byte[aesCBC.getBlockSize()];
    rng.nextBytes(ivBytes);
    aesCBC.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(ivBytes));

    byte[] cipherTextCBC = aesCBC.doFinal(utf8Chinese);
    byte[] ivAndCipherTextCBC = Arrays.concatenate(ivBytes, cipherTextCBC);

    System.out.printf("UTF-8, CBC    encoded : %d bytes: %s%n", ivAndCipherTextCBC.length, Hex.toHexString(ivAndCipherTextCBC));
}

byte[] utf16Chinese = chinese.getBytes(UTF_16BE);
System.out.printf("UTF-16BE encoded : %d bytes: %s%n", utf16Chinese.length, Hex.toHexString(utf16Chinese));

{
    Cipher aesCTR = Cipher.getInstance("AES/CTR/NoPadding");

    byte[] nonce = new byte[8];
    rng.nextBytes(nonce);
    byte[] initialCounterValue = new byte[8];
    byte[] ivForCTR = Arrays.concatenate(nonce, initialCounterValue);
    aesCTR.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(ivForCTR));

    byte[] cipherTextCTR = aesCTR.doFinal(utf16Chinese);
    byte[] ivAndCipherTextCTR = Arrays.concatenate(ivForCTR, cipherTextCTR);

    System.out.printf("UTF-16BE, CTR encoded : %d bytes: %s%n", ivAndCipherTextCTR.length, Hex.toHexString(ivAndCipherTextCTR));
}
