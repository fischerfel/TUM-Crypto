public static void main(String[] args) {
    try{
        // example key generated via: $ openssl genrsa -out tmp-key.pem 2048
        // modulus and exponent extracted via: $ openssl rsa -text -in tmp-key.pem
        // (modulus 514 hex long)
        String pubModulus = "00e45679a14c6cbd4646bbe90b3c820eb19fe8366822ce2e4beed9d158b9e8863bdc4e3f0fbad31144ef193493dff8619a5aaa7504541c5d97c20cd29c87435eb2e4fbbab8e3b02d82353016c91e971e8d497e1699eeb77a833833617369333c4d0d93cd6f1a9e6090fafd4cbf00b1e0fc6478003ed6e762fb921446c84f0f281117e692a5f76e4b75cbb1252436b3268893195d25344cc3e5d5a52560243d62e5ce8a7bd72a89fce5fbf009435901e274c3cca5eab0f2b2057683ed6e3ed851723adbabb4028a7900ddc46d8c894097c07ab071f6af8fc1c520681e0abd7685f4851d360a7c6d425373da806d356a517ae764093e6999d2cc9305f46a7e1744ed";
        String pubExp = "65537"; // most common exponent is 65537 which encodes to AQAB

        PublicKey key = createPublicKey(pubModulus, pubExp);
        byte[] data = encrypt("Secret Message", key);
        System.out.println(""+data.length); // prints "256" in this example code
    } catch(Exception e) {
        e.printStackTrace();
    }
}

public static PublicKey createPublicKey(String stringPublicKey, String exponentString) throws NoSuchAlgorithmException, InvalidKeySpecException {
    try{
        // for key modulus and exponent value as base64 encoded string from key file
        //BigInteger keyInt = new BigInteger(Base64.getDecoder().decode(stringPublicKey.getBytes("UTF-8")));
        //BigInteger exponentInt = new BigInteger(Base64.getDecoder().decode(exponentString.getBytes("UTF-8")));

        // for key modulus and exponent values as hex and decimal string respectively
        BigInteger keyInt = new BigInteger(stringPublicKey,16); // hex base
        BigInteger exponentInt = new BigInteger(exponentString,10); // decimal base

        RSAPublicKeySpec keySpeck = new RSAPublicKeySpec(keyInt, exponentInt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return  keyFactory.generatePublic(keySpeck);
    } catch(Exception e) {
        e.printStackTrace();
    }

    return null;
}

public static byte[] encrypt(String message, PublicKey key) {
    try{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] data = cipher.doFinal(message.getBytes());
        return data;
    } catch(Exception e) {
        e.printStackTrace();
    }

    return new byte[1];
}
