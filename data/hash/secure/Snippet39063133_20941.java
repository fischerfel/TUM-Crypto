public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

void hashBigInteger(String s){
    try{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte b[] = hexStringToByteArray(s);
        sha.update(b,0,b.length);
        byte digest[] = sha.digest();
        BigInteger d = new BigInteger(1,digest);

        System.out.println("H "+d.toString(16));
    }catch (NoSuchAlgorithmException e){
        throw new UnsupportedOperationException(e);
    }
}
