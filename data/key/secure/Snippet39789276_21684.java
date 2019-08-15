decCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec keySpec = new SecretKeySpec(securityKey , "AES");

//IV + Cipher 
byte [] cipherWithIV = Base64.decodeBase64(responseStr.getBytes()));

//Extract IV
 byte [] iv = new byte [16];
 byte [] cipherWithoutIV = new byte [cipherWithIV.length - 16 ];

//First 16 bytes
for(i < 16; i++) {
    iv [i] = cipherWithIV [i];
}

//Rest of the cipher ie 16 -> cipherWithIV.length
for(i < cipherWithIV.length; i++) {
    cipherWithoutIV [j] = cipherWithIV[i];
    j++;
}

//
IvParameterSpec ivParamSpec = new IvParameterSpec(iv);

//
decCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

//Decrypt cipher without IV
decText = decCipher.doFinal(cipherWithoutIV);

//Convert to string
decString = new String(decText,"UTF8");
