public Integer calculateHash(String uuid) {

    try {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(uuid.getBytes());
        byte[] output = digest.digest();

        String hex = hexToString(output);
        Integer i = Integer.parseInt(hex,16);
        return i;           

    } catch (NoSuchAlgorithmException e) {
        System.out.println("SHA1 not implemented in this system");
    }

    return null;
}   

private String hexToString(byte[] output) {
    char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };
    StringBuffer buf = new StringBuffer();
    for (int j = 0; j < output.length; j++) {
        buf.append(hexDigit[(output[j] >> 4) & 0x0f]);
        buf.append(hexDigit[output[j] & 0x0f]);
    }
    return buf.toString();

}
