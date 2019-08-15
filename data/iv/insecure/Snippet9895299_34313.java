public class DesEncrypter {
Cipher ecipher;
Cipher dcipher;

DesEncrypter(SecretKey key) {
    // Create an 8-byte initialization vector
    byte[] iv = new byte[]{
        (byte)0x8E, 0x12, 0x39, (byte)0x9C,
        0x07, 0x72, 0x6F, 0x5A
    };
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
    try {
        ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // CBC requires an initialization vector
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    } catch (java.security.InvalidAlgorithmParameterException e) {
    } catch (javax.crypto.NoSuchPaddingException e) {
    } catch (java.security.NoSuchAlgorithmException e) {
    } catch (java.security.InvalidKeyException e) {
    }
}

// Buffer used to transport the bytes from one stream to another
byte[] buf = new byte[1024];

public void encrypt(InputStream in, OutputStream out) {
    try {
        // Bytes written to out will be encrypted
       AppendableOutputStream out_append = new AppendableOutputStream(out);

       OutputStream out_c = new CipherOutputStream(out_append, ecipher);

        // Read in the cleartext bytes and write to out to encrypt
        int numRead = 0;
        int count = 0;
        int max = 1024;
        boolean first = true;

        while ((numRead = in.read(buf, 0, max)) > 0) {                 
            System.out.println("running Total: " + count);
            count += numRead;
            // if this read puts as at less than a meg, encrypt
            if(count <= 1024*1024){
                System.out.println("encrypted " + numRead + " of " + max +" bytes : total " + count);
                out_c.write(buf, 0, numRead);
                // last encryption pass, close buffer and fix max
                if(count == 1024*1024){
                    // fix reading 1k in case max was decreased
                    max = 1024;
                    out_c.close();
                }
                // if next read will go over a meg, read less than 1k
                else if(count + max > 1024*1024)
                    max = 1024*1024 - count;
            }
            // past the first meg, don't encrypt
            else{
                System.out.println("processed " + numRead + " of " + max +" bytes : total " + count);
                out.write(buf, 0, numRead);
            }

        }
        out.close();

    } catch (java.io.IOException e) {}

}

// Movies encrypt only 1 MB 128 passes.

public void decrypt(InputStream in, OutputStream out) {
    try {
        // Bytes read from in will be decrypted
        InputStream in_c = new CipherInputStream(in, dcipher);

        // Read in the decrypted bytes and write the cleartext to out
        int numRead = 0;
        int count = 0;
        int max = 1024;

        while ((numRead = in_c.read(buf, 0, max)) > 0) {
            count += numRead;
            System.out.println("decrypted " + numRead + " of " + max +" bytes : total " + count);
            out.write(buf, 0, numRead);
            if(count + max > 1024*1024){
                max = 1024*1024 - count;
            }
            if(count == 1024*1024)
                max = 0;
        }

        //in.skip(count);
        int n = 0;
        while((numRead = in.read(buf)) > 0 && n < 10){
        count += numRead;
        System.out.println("processed " + numRead + " of 1024 bytes : total " + count);
            out.write(buf,0,numRead);
            //System.out.println("buf"+buf.length  +" numered" + numRead+ " n"+n);
            // If i look at the file after anything under n < 51 the file  doesn't change.
            n++;

        }
        out.flush();
        out.close();
    } catch (java.io.IOException e) {
        System.out.println("AHHHHHHHH!!!!!!");
    }
}
