public AlgorithmParameterSpec getIV() {
AlgorithmParameterSpec ivspec;
byte[] iv = new byte[cipher.getBlockSize()];
new SecureRandom().nextBytes(iv);
ivspec = new IvParameterSpec(iv);
}
