    private static boolean initialised;
    private static void init() {
      if (initialised)
        return;
      Security.addProvider(new BouncyCastleProvider());
      initialised = true;
    }
    public static String makeKey() {
        init();
        KeyGenerator generator = KeyGenerator.getInstance(algorithm, provider);
        generator.init(keySize);
        Key key = generator.generateKey();
        byte[] encoded = key.getEncoded();
        return Strings.toHex(encoded);
}

public static String aesDecrypt(String hexKey, String hexCoded) {
        init();
        SecretKeySpec key = new SecretKeySpec(Strings.fromHex(hexKey), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding", provider);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] codedBytes = Strings.fromHex(hexCoded);
        CipherInputStream inputStream = new CipherInputStream(new ByteArrayInputStream(codedBytes), cipher);
        byte[] bytes = getBytes(inputStream, 256);
        String result = new String(bytes, "UTF-8");
        return result;
}

public static String aesEncrypt(String hexKey, String input) {
        init();
        SecretKeySpec key = new SecretKeySpec(Strings.fromHex(hexKey), algorithm);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(input.length());
        CipherOutputStream outputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
        setText(outputStream, input);
        byte[] outputBytes = byteArrayOutputStream.toByteArray();
        String output = new String(Strings.toHex(outputBytes));
        return output;
}
public static void setText(OutputStream outputStream, String text, String encoding) {
    try {
        outputStream.write(text.getBytes(encoding));
        outputStream.flush();
    } finally {
            outputStream.close();
    }
}
public static byte[] getBytes(InputStream inputStream, int bufferSize) {
    try {
        List<ByteArrayAndLength> list = Lists.newList();
        while (true) {
            byte[] buffer = new byte[bufferSize];
            int count = inputStream.read(buffer);
            if (count == -1) {
                byte[] result = new byte[ByteArrayAndLength.length(list)];
                int index = 0;
                for (ByteArrayAndLength byteArrayAndLength : list) {
                    System.arraycopy(byteArrayAndLength.bytes, 0, result, index, byteArrayAndLength.length);
                    index += byteArrayAndLength.length;
                }
                assert index == result.length;
                return result;
            }
            list.add(new ByteArrayAndLength(buffer, count));
        }
    } finally {
            inputStream.close();
    }
}
    static class ByteArrayAndLength {
    byte[] bytes;
    int length;

    public ByteArrayAndLength(byte[] bytes, int length) {
        super();
        this.bytes = bytes;
        this.length = length;
    }

    static int length(List<ByteArrayAndLength> list) {
        int result = 0;
        for (ByteArrayAndLength byteArrayAndLength : list) {
            result += byteArrayAndLength.length;
        }
        return result;
    }
}
