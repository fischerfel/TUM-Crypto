java.security.InvalidKeyException: Illegal key size
    at javax.crypto.Cipher.checkCryptoPerm(Cipher.java:1023)
    at javax.crypto.Cipher.implInit(Cipher.java:789)
    at javax.crypto.Cipher.chooseProvider(Cipher.java:848)
    at javax.crypto.Cipher.init(Cipher.java:1347)
    at javax.crypto.Cipher.init(Cipher.java:1281)
    at test.net.zomis.ZomisTest.decryptCipher(ZomisTest.java:112)
@Test
public void decryptCipher() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    String iv = "0df1eff724d50157ab048d9ff214b73c";
    String cryptext = "2743be20314cdc768065b794904a0724e64e339ea6b4f13c510e2d2e8c95dd7409aa0aefd20daae80956dd2978c98d6e914d1d7b5b5be47b491d91e7e4f16f7f30d991ba80a81bafd8f0d7d83755ba0ca66d6b208424529c7111bc9cd6d11786f3f604a0715f";
    String key = "375f22c03371803ca6d36ec42ae1f97541961f7359cf5611bbed399b42c7c0be"; // Hexadecimal String, will be converted to non-hexadecimal String
    String expectedResult = "01020506080b10131c22292d313536393b464c535466696d6e7d7f808a8e9899a2adb1b8babcbebfc1c6c7c8cecfd8e0e4e8ef";

    byte[] kdata = Util.hex2byte(key);

    Assert.assertEquals(32, kdata.length); // 32 bytes = 256-bit key

    String result;

    Cipher cipher;
    cipher = Cipher.getInstance("AES/OFB/NoPadding");
    // Below line is 112, which is causing exception
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kdata, "AES"), new IvParameterSpec(iv.getBytes("UTF-8")));
    byte[] cryptData = Util.hex2byte(cryptext);
    byte[] ciphertext = cipher.doFinal(cryptData);
    result = new String(ciphertext);


    Assert.assertEquals(expectedResult, result);
}

@Test
public void decryptAES() {
    String iv = "0df1eff724d50157ab048d9ff214b73c"; 
    // Problem: Where should I specify the IV ???? Currently it is an unused variable...

    String cryptext = "2743be20314cdc768065b794904a0724e64e339ea6b4f13c510e2d2e8c95dd7409aa0aefd20daae80956dd2978c98d6e914d1d7b5b5be47b491d91e7e4f16f7f30d991ba80a81bafd8f0d7d83755ba0ca66d6b208424529c7111bc9cd6d11786f3f604a0715f";
    String key = "375f22c03371803ca6d36ec42ae1f97541961f7359cf5611bbed399b42c7c0be"; // Hexadecimal String, will be converted to non-hexadecimal String
    String expectedResult = "01020506080b10131c22292d313536393b464c535466696d6e7d7f808a8e9899a2adb1b8babcbebfc1c6c7c8cecfd8e0e4e8ef";

    Assert.assertEquals(64, key.length());

    AES aes = new AES();
    aes.setKey(Util.hex2byte(key));
    byte[] byteCryptedData = Util.hex2byte(cryptext);
    String byteCryptedString = new String(byteCryptedData);

    while (byteCryptedString.length() % 16 != 0) byteCryptedString += " ";


    String result = aes.Decrypt(byteCryptedString);
    Assert.assertEquals(expectedResult, result); // Assertion Failed
}
