private static void init(String password) throws Exception {
    PBEKeySpec PBEKeySpecification = new PBEKeySpec(password.toCharArray());
    keyDES = SecretKeyFactory.getInstance(encrypted_algorithm).generateSecret(PBEKeySpecification);
    myCipher = Cipher.getInstance(encrypted_algorithm);
    algorithmSpecification = new PBEParameterSpec(salt, iterationCounter);
}

public static void main(String[] args) throws Exception {
    String input = "zW4%3D1p1%2AjR9E";
    String infoDesencriptada = null;
    try {
        init("abc123ab");
        myCipher.init(2, keyDES, algorithmSpecification);
        BASE64Decoder base64Enc = new BASE64Decoder();
        byte[] arrayBase64Enc = base64Enc.decodeBuffer(input);
        byte[] decryptedBytes = myCipher.doFinal(arrayBase64Enc);
        infoDesencriptada = new String(decryptedBytes, "UTF8");
    } catch (Exception var5) {
        System.out.println("Some exception:" + var5.getMessage());
        var5.printStackTrace();
    }
}
