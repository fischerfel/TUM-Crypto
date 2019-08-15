public class AesFileIo {
    private static final String EOL = "\n";
    private static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    private static final SecretKeySpec secretKeySpec =
            new SecretKeySpec(AES_KEY_128, "AES");
    private static final IvParameterSpec ivSpec = new IvParameterSpec(IV);

    public void AesFileIo() {
        Security.addProvider(new org.bouncycastle.jce.provider
                .BouncyCastleProvider());
    }

    public String readFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ObjectInputStream is = new ObjectInputStream(
                new FileInputStream(fileName));
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            InputStreamReader isr = new InputStreamReader(cis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(EOL);
            }
            is.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("FileNotFoundException: probably OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void writeFile(String fileName, String theFile) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(theFile.getBytes());
            ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(fileName));
            os.write(encrypted);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
