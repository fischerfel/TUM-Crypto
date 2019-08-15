public String doHash(String s, String salt) {
    try {
        final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.reset();
        if (salt != null && !salt.isEmpty()) {
            sha512.update(salt.getBytes());
        }
        byte[] data = sha512.digest(s.getBytes());
        StringBuilder hexData = new StringBuilder();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++) {
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));
        }
        return hexData.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
