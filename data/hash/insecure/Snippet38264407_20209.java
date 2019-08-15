String toSHA1(Bitmap bitmap){
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] byteArray = stream.toByteArray();
    try {
        String sha1 = toSHA1(byteArray);
        stream.close();
        return sha1;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

String toSHA1(byte[] data) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    BigInteger bigInteger = new BigInteger(1, md.digest(data));
    return bigInteger.toString(Character.MAX_RADIX);
}
