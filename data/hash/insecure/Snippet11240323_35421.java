private static final char[] DIGITS_LOWER = 
   {'0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

private static final char[] DIGITS_UPPER = 
   {'0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

protected static char[] encodeHex(byte[] data, char[] toDigits) {
    int l = data.length;
    char[] out = new char[l << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
        out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
        out[j++] = toDigits[0x0F & data[i]];
    }
    return out;
}

protected static int toDigit(char ch, int index) throws DecoderException {
    int digit = Character.digit(ch, 16);
    if (digit == -1) {
        throw new DecoderException(
                    "Illegal hexadecimal character " 
            + ch + " at index " + index);
    }
    return digit;
}

public static String exampleSha1(String convertme){
    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
    byte[] encodeHex = md.digest(convertme));
    return new String(encodeHex);
}
