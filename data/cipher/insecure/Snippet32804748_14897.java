public class encrypt
{
    public static void main(String[] args) throws Exception
    {
        byte[] keyBytes = "java".getBytes();
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(keyBytes, "Blowfish");
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("Blowfish");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKeySpec);

        java.io.BufferedInputStream bufferedInputStream = new java.io.BufferedInputStream(new java.io.FileInputStream("tmp.class"));
        javax.crypto.CipherOutputStream cipherOutputStream = new javax.crypto.CipherOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream("data.dll")), cipher);
        int i;
        do
        {
            i = bufferedInputStream.read();
            if (i != -1)
                cipherOutputStream.write(i);
        }while (i != -1);
        bufferedInputStream.close();
        cipherOutputStream.close();

        bufferedInputStream = new java.io.BufferedInputStream(new java.io.FileInputStream("tmp$del.class"));
        cipherOutputStream = new javax.crypto.CipherOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream("data_2.dll")), cipher);
        do
        {
            i = bufferedInputStream.read();
            if (i != -1)
                cipherOutputStream.write(i);
        }while (i != -1);
        bufferedInputStream.close();
        cipherOutputStream.close();

        System.exit(0);
    }
}
