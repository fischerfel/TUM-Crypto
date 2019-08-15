public AlgorithmParameterSpec generateIv(int size) throws NoSuchAlgorithmException {
    AlgorithmParameterSpec ivspec;
    byte[] iv = new byte[size];
    new SecureRandom().nextBytes(iv);
    ivspec = new IvParameterSpec(iv);
    return ivspec;
}
