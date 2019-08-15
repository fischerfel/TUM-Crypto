BigInteger bi = BigInteger.ZERO;
char[] array = input.toCharArray();
for (int i = 0; i < array.length; i++) {
    bi = bi.add(BigInteger.valueOf(i + 1).multiply(
            BigInteger.valueOf(array[i])));
}
final int moduloOperator = 52665; // random constant
final byte[] moduloResult = bi.remainder(
        BigInteger.valueOf(moduloOperator)).toByteArray();
MessageDigest md;
try {
    md = MessageDigest.getInstance("MD5");
} catch (NoSuchAlgorithmException nsae) {
    nsae.printStackTrace();
    return null;
}
md.update(moduloResult);
return new BigInteger(1, md.digest()).toString().substring(0, 7);
