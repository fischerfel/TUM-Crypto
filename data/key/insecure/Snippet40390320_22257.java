final static byte[] KEY = {13, 92, 9, 7, 111, 84, 19, 3, 20, 76, 67, 47, 12, 2, 32, 99};

final static Base64 ENCODER = new Base64(-1, new byte[0], true);
final static Key S_KEY = new SecretKeySpec(KEY, "AES");

static String encrypt(long aid) {
    byte[] clear = Longs.toByteArray(aid);

    try {
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, S_KEY);

        byte[] bytes = cipher.doFinal(clear);

        System.out.println("Byte array: " + Arrays.toString(bytes));

        return ENCODER.encodeAsString(bytes);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
