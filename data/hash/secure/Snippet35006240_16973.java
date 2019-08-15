public String getSHA256HashedString(String clearString) {
    String _LOC = "[SB_UtilityBean: getSHA256HashedString]";

    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(clearString.getBytes());

        String _encrypted = bytesToHex(md.digest());
        System.out.println(_LOC + "1.0 " + " Result 1: " + _encrypted);
        System.out.println(_LOC + "1.0 " + " Result 2: " + bytesToHex(md.digest()));

        return _encrypted;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
}

private static String bytesToHex(byte[] bytes) {
    String _LOC = "[SB_UtilityBean: bytesToHex]";

    StringBuffer result = new StringBuffer();
    for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));

    return result.toString();
}
