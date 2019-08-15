private static SecretKey key = generateAESkey();
private static String cipherString = "AES/CBC/PKCS5Padding";

public static void main(String[] args) throws Exception {
    ByteArrayOutputStream log = new ByteArrayOutputStream();
    appendToLog("Test1", log);
    appendToLog("Test2 is longer", log);
    appendToLog("Test3 is multiple of block size!", log);
    appendToLog("Test4 is shorter.", log);

    byte[] encLog = log.toByteArray();

    List<String> logs = decryptLog(new ByteArrayInputStream(encLog));

    for(String logLine : logs) {
        System.out.println(logLine);
    }
}

private static SecretKey generateAESkey() {
    try {
        return KeyGenerator.getInstance("AES").generateKey();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return null;
}

private static byte[] generateIV() {
    SecureRandom random = new SecureRandom();
    byte[] iv = new byte[16];
    random.nextBytes(iv);
    return iv;
}

public static void appendToLog(String s, OutputStream os) throws Exception {
    Cipher cipher = Cipher.getInstance(cipherString);
    byte[] iv = generateIV();
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] data = cipher.doFinal(s.getBytes("UTF-8"));
    os.write(data.length);
    os.write(iv);
    os.write(data);
}

public static List<String> decryptLog(InputStream is) throws Exception{
    ArrayList<String> logs = new ArrayList<String>();
    while(is.available() > 0) {
        int len = is.read();
        byte[] encLogLine = new byte[len];
        byte[] iv = new byte[16];
        is.read(iv);
        is.read(encLogLine);

        Cipher cipher = Cipher.getInstance(cipherString);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] data = cipher.doFinal(encLogLine);
        logs.add(new String(data, "UTF-8"));
    }
    return logs;
}
