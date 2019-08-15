private static final String ZERO_PADDING_KEY = "32_length_String";
private static final String IV = "32_length_String";
private static final String CIPHER_ALGORITHM = "AES/CBC/ZeroBytePadding";

public static String encryptAESURL(String url) {
    try {

        byte[] key = ZERO_PADDING_KEY.getBytes("UTF-8");

        SecretKeySpec sks = new SecretKeySpec(key, "AES");

        byte[] iv = Arrays.copyOf(IV.getBytes("UTF-8"), 16);

        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, sks, ivspec);

        byte[] encryptedText = cipher.doFinal(url.getBytes("UTF-8"));

        String resul = bytesToHex(encryptedText);
        url = resul;
        return resul;

    } catch (Exception e) {
        Log.e("ENCRYPT ERROR", e.getMessage());
        e.printStackTrace();
        //  throw new CryptoException("Unable to decrypt", e);

    }
    return url;

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
