public byte [] encrypt_3DES(final String claveHex, final String datos) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
    byte [] ciphertext = null;
    // Crea la clave
    DESedeKeySpec desKeySpec = new DESedeKeySpec(toByteArray(claveHex));
    SecretKey desKey = new SecretKeySpec(desKeySpec.getKey(), "DESede");
    // Crea un cifrador
    Cipher desCipher = Cipher.getInstance("DESede/CBC/NoPadding");

    // Inicializa el cifrador para encriptar
    desCipher.init(Cipher.ENCRYPT_MODE, desKey, new IvParameterSpec(IV));

    // Se añaden los 0 en bytes necesarios para que sea un múltiplo de 8
    int numeroCerosNecesarios = 8 - (datos.length() % 8);
    if (numeroCerosNecesarios == 8) {
        numeroCerosNecesarios = 0;
    }
    ByteArrayOutputStream array = new ByteArrayOutputStream();
    array.write(datos.getBytes("UTF-8"), 0, datos.length());
    for (int i = 0; i < numeroCerosNecesarios; i++) {
        array.write(0);
    }
    byte [] cleartext = array.toByteArray();
    // Encripta el texto
    ciphertext = desCipher.doFinal(cleartext);
    return ciphertext;
}
