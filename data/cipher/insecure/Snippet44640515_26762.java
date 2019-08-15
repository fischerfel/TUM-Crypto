public void authenticate(byte[] key) throws CardException {
    System.out.println("AUTHENTICATE");
    byte[] encRndB = transmitRaw(new byte[] { 0x1A, 0x00 });
    if((encRndB.length!=9)||(encRndB[0]!=AF)) {
        throw new RuntimeException("Invalid response!");
    }
    encRndB=Arrays.copyOfRange(encRndB, 1, 9);
    System.out.println(" - EncRndB: " + toHex(encRndB));
    byte[] rndB = desDecrypt(key, encRndB);
    System.out.println(" - RndB: " + toHex(rndB));
    byte[] rndBrot = rotateLeft(rndB);
    System.out.println(" - RndBrot: " + toHex(rndBrot));
    byte[] rndA = new byte[8];
    generateRandom(rndA);
    System.out.println(" - RndA: " + toHex(rndA));
    byte[] encRndArotPrime = transmitRaw(ArrayUtils.addAll(new byte[] {AF}, desEncrypt(key, ArrayUtils.addAll(rndA, rndBrot))));
    if((encRndArotPrime.length!=9)||(encRndArotPrime[0]!=0x00)) {
        throw new RuntimeException("Invalid response!");
    }
    encRndArotPrime=Arrays.copyOfRange(encRndArotPrime, 1, 9);
    System.out.println(" - EncRndArot': " + toHex(encRndArotPrime));
    byte[] rndArotPrime = desDecrypt(key, encRndArotPrime);
    System.out.println(" - RndArot': " + toHex(rndArotPrime));
    if(!Arrays.equals(rotateLeft(rndA), rndArotPrime)) {
        throw new RuntimeException("Card authentication failed");
    }
}

protected static SecureRandom rnd = new SecureRandom();
protected static void generateRandom(byte[] rndA) {
    rnd.nextBytes(rndA);
}

protected byte[] desEncrypt(byte[] key, byte[] data) {
    return performDes(Cipher.ENCRYPT_MODE, key, data);
}
protected byte[] desDecrypt(byte[] key, byte[] data) {
    return performDes(Cipher.DECRYPT_MODE, key, data);
}
private byte[] iv = new byte[8];
protected byte[] performDes(int opMode, byte[] key, byte[] data) {
    try {
        Cipher des = Cipher.getInstance("DESede/CBC/NoPadding");
        SecretKeyFactory desKeyFactory = SecretKeyFactory.getInstance("DESede");
        Key desKey = desKeyFactory.generateSecret(new DESedeKeySpec(ArrayUtils.addAll(key, Arrays.copyOf(key, 8))));
        des.init(opMode, desKey, new IvParameterSpec(iv));
        byte[] ret = des.doFinal(data);
        if(opMode==Cipher.ENCRYPT_MODE) {
            iv=Arrays.copyOfRange(ret, ret.length-8, ret.length);
        } else {
            iv=Arrays.copyOfRange(data, data.length-8, data.length);
        }
        return ret;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
        throw new RuntimeException(e);
    }
}

protected static byte[] rotateLeft(byte[] in) {
    return ArrayUtils.add(Arrays.copyOfRange(in, 1, 8), in[0]);
}
