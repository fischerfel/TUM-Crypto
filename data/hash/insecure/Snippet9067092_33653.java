public static void main(String[] args) throws Exception {    
    byte[] in = { (byte) 0xff }; // (1)
    byte[] hash = MessageDigest.getInstance("SHA-1").digest(in);
    System.out.println(toHexString(hash));
}

private static String toHexString(byte[] in) {
    StringBuilder out = new StringBuilder(in.length * 2);
    for (byte b: in)
        out.append(String.format("%02X", b & 0xff)); // (2)
    return out.toString();
}
