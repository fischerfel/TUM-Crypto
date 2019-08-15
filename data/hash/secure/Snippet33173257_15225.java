private static String getHashCodeFromString(String algorithm, String str) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance(algorithm);
    md.update(str.getBytes());
    byte byteData[] = md.digest();

    //convert the byte to hex format method 1
    StringBuffer hashCodeBuffer = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
        hashCodeBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }
    return hashCodeBuffer.toString();
}
