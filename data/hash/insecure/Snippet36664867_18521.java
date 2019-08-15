private static String hash(String value) throws NoSuchAlgorithmException {
        byte[] data = value.getBytes(StandardCharsets.US_ASCII);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] result = md.digest(data);
        return Hex.encodeHexString(result).replace("-", "").toLowerCase();
}
