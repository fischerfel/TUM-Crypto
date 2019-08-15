@Override
public byte[] encrypt(byte[] input) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/" + getPadding(), "BC");
    cipher.init(Cipher.ENCRYPT_MODE, getKey());
    byte[] output = getBytesForCipher(cipher, input);
    int ctLength = cipher.update(input, 0, input.length, output, 0);
    updateHash(input);
    cipher.doFinal(getDigest(), 0, getDigest().length, output, ctLength);
    return output;
}

protected byte[] getBytesForCipher(Cipher cipher, byte[] input) {
    return new byte[cipher.getOutputSize(input.length + hash.getDigestLength())];
}

protected void updateHash(byte[] input) {
    hash.update(input);
}


public byte[] decrypt(byte[] input) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/" + getPadding(), "BC");
    cipher.init(Cipher.DECRYPT_MODE, getKey());
    byte[] output = new byte[cipher.getOutputSize(input.length)];
    int ctLength = cipher.update(input, 0, input.length, output, 0);
    cipher.doFinal(output, ctLength);
    return removeHash(output);
}

protected byte[] removeHash(byte[] output) {
    int messageLength = output.length - hash.getDigestLength();
    hash.update(output, 0, output.length - hash.getDigestLength());;
    byte[] realOutput = new byte[messageLength];
    System.arraycopy(output, 0, realOutput, 0, messageLength);
    messageValid = isValid(output);
    return realOutput;
}

private boolean isValid(byte[] output) {
    int messageLength = output.length - hash.getDigestLength();
    byte[] messageHash = new byte[hash.getDigestLength()];
    System.arraycopy(output, messageLength, messageHash, 0, messageHash.length);
    return MessageDigest.isEqual(hash.digest(), messageHash);
}
