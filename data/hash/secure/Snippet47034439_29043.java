private static long generateIdentifier(final String adrLine, final String postCode) {
    final String resultInput = adrLine + postCode;

    //do not forget about charset you want to work with
    final byte[] inputBytes = resultInput.getBytes(Charset.defaultCharset());
    byte[] outputBytes = null;

    try {
        //feel free to choose the encoding base like MD5, SHA-1, SHA-256
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        outputBytes = digest.digest(inputBytes);
    } catch (NoSuchAlgorithmException e) {
        //do whatever you want, better throw some exception with error message
    }

    long digitResult = -1;
    if (outputBytes != null) {
        digitResult = Long.parseLong(convertByteArrayToHexString(outputBytes).substring(0, 7), 16);
    }

    return digitResult;
}

//this method also may be useful for you if you decide to use the full result
// or you need the appropriate hex representation
private static String convertByteArrayToHexString(byte[] arrayBytes) {
    final StringBuilder stringBuffer = new StringBuilder();
    for (byte arrByte: arrayBytes) {
        stringBuffer.append(Integer.toString((arrByte & 0xff) + 0x100, 16)
                .substring(1));
    }
    return stringBuffer.toString();
}
