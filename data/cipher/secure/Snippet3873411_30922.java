public class AesFileIo {

    private static final String EOL = "\n";
    private static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivSpec;

    AesFileIo(byte[] aesKey, byte[] iv) {
        Security.addProvider(new org.bouncycastle.jce.provider
                .BouncyCastleProvider());
        ivSpec = new IvParameterSpec(iv);
        secretKeySpec = new SecretKeySpec(aesKey, "AES");
    }

    public String readFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            InputStreamReader isr = new InputStreamReader(cis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(EOL);
            }
            fis.close();
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
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(encrypted);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
