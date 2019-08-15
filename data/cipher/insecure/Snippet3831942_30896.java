public Cipher getCipher(int opMode) throws IOException {
    Cipher cipher = null;

    try {
        cipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
        throw new MdmFatalInternalErrorException("Getting instance of cipher failed. Should never happen. BUG ", e);
    } catch (NoSuchPaddingException e) {
        throw new MdmFatalInternalErrorException("Getting instance of cipher failed. Should never happen. BUG ", e);
    }
    try {
        cipher.init(opMode, readKey());
    } catch (InvalidKeyException e) {
        throw new MdmInvalidKeyException();
    } catch (ClassNotFoundException e) {
        throw new MdmInvalidKeyException();
    } catch (NumberFormatException e) {
        throw new MdmInvalidKeyException();
    }
    return cipher;
}
