private Cipher cipher;
private SecretKey secretKey;
private IvParameterSpec ivParameterSpec;

int iterationCount = 1024;
int keyStrength = 128;

private String sampleInputForPassSaltIV = "Abcd1234Abcd1234";

private String encryptInput = "helloAES";
private String encryptedOutput = "";
private String decryptedOutput = "";

public Boolean initializeEncryption() throws Exception {
    String secretKeyAlgorithm = "PBKDF2WithHmacSHA1";

    SecretKeyFactory secretKeyFactory;
    KeySpec keySpec;
    SecretKey secretKeyTemp;

    String passPhrase = sampleInputForPassSaltIV;
    String keySalt = sampleInputForPassSaltIV;

    secretKeyFactory = SecretKeyFactory.getInstance(secretKeyAlgorithm);
    keySpec = new PBEKeySpec(passPhrase.toCharArray(), keySalt.getBytes(), iterationCount, keyStrength);
    secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
    secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");

    byte[] IV = sampleInputForPassSaltIV.getBytes();
    ivParameterSpec = new IvParameterSpec(IV);

    cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

    return true;
}

private void encrypt(String dataToEncrypt) throws Exception {
    if (dataToEncrypt.length() > 0) {
        byte[] UTF8Data;
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        UTF8Data = cipher.doFinal(dataToEncrypt.getBytes());
        encryptedOutput = Base64.encodeToString(UTF8Data, 0);

        Toast toast = Toast.makeText(context, "Encrypted Text : " + encryptedOutput, Toast.LENGTH_LONG);
        toast.show();
    }
}
