public static String digestSalted(String salt, char[] password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    ArrayList<Byte> list = new ArrayList<Byte>();
    for (int i = 0; i < password.length; i++) {
        //String ch = String.valueOf(password[i]);
        //byte[] b = ch.getBytes();
        //for (int j = 0; j < b.length; j++) {
        //  list.add(b[j]);
        //}
                    list.add((byte)password[i]);
    }
    byte[] saltInBytes = salt.getBytes();
    byte[] toBeHashed = new byte[(saltInBytes.length + list.size())];
    for (int i = 0; i < saltInBytes.length; i++) {
        toBeHashed[i] = saltInBytes[i];
    }
    for (int i = saltInBytes.length; i < list.size() + saltInBytes.length; i++) {
        toBeHashed[i] = list.get(i - saltInBytes.length);
    }

    md.update(toBeHashed);

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
