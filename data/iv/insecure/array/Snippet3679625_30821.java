public class AesWriter extends Activity {
    ...
    private void writeConfig() {
        ...
        try {
            Cipher cipher = Cipher.getInstance(AesReader.AES_ALGORITHM, 
                    AesReader.PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, AesReader.getSecretKeySpec(),
                    AesReader.getIvParameterSpec()); 
            byte[] encrypted = cipher.doFinal(config.getBytes());
            OutputStreamWriter out =
                new OutputStreamWriter(openFileOutput(fileName, 0));
            out.write(AesReader.asHex(encrypted));
            out.close();
            ...

public class AesReader extends Activity {
    public static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    public static final String PROVIDER = "BC"; 
    private static final byte[] aesKey128 = { // Hard coded for now
        78, -90, 42, 70, -5, 20, -114, 103, 
        -99, -25, 76, 95, -85, 94, 57, 54};
    private static final byte[] ivBytes = { // Hard coded for now
        -85, -67, -5, 88, 28, 49, 49, 85, 
        114, 83, -40, 119, -65, 91, 76, 108};
    private static final SecretKeySpec secretKeySpec = 
        new SecretKeySpec(aesKey128, "AES");
    private static final IvParameterSpec ivSpec = 
        new IvParameterSpec(ivBytes);
        ...
    private void readConfig() {
        String fileName = configuration.getFileName();
        try {
            InputStream is = openFileInput(fileName);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            InputStreamReader isr = new InputStreamReader(cis);
            BufferedReader reader = new BufferedReader(isr);
            String s;
            while ((s = reader.readLine()) != null) {
                configuration.modify(s);
            }
            is.close();
            ...
    public static SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }
    public static IvParameterSpec getIvParameterSpec() {
        return ivSpec;
    }
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }
