public static String encryptString(String src) throws Exception {
    String dst = "";

    SecretKey secret_key = KeyGenerator.getInstance("DES").generateKey();
    AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(initialization_vector);
    encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
    encrypt.init(Cipher.ENCRYPT_MODE, secret_key, alogrithm_specs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CipherOutputStream cout = new CipherOutputStream(baos,encrypt);
    cout.write(src.getBytes());
    cout.flush();               //ByteOutputStream -> Write Encryption Text
    cout.close(); 
    dst = DatatypeConverter.printHexBinary(baos.toByteArray());
    return dst;
}
