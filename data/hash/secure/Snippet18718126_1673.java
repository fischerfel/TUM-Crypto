public static String digest(char[] in) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("SHA-256");

    ArrayList<Byte> list = new ArrayList<Byte>();
    for(int i = 0; i<in.length; i++){
        byte b = (byte) in[i]
        list.add(b);
    }
    byte[] inputInByte = new byte[list.size()];
    for(int i =0;i<list.size();i++){
        inputInByte[i] = list.get(i);
    }
    md.update(inputInByte);

    byte byteData[] = md.digest();

    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
        String hex = Integer.toHexString(0xff & byteData[i]);
        if (hex.length() == 1) {
            hexString.append('0');
        }
        hexString.append(hex);
    }
    return hexString.toString();
}
