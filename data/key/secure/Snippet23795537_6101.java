 package com.infovale.cripto;

 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.math.BigInteger;
 import java.util.Arrays;

 import javax.crypto.Cipher;
 import javax.crypto.CipherInputStream;
 import javax.crypto.CipherOutputStream;
 import javax.crypto.KeyGenerator;
 import javax.crypto.SecretKey;
 import javax.crypto.spec.SecretKeySpec;

 import android.content.Context;

 public class AES extends Cripto {

public static String encrypt(Context c, String nomeArquivo) {
    String caminhoArquivoPuro = new _Path().getPathFilePure() + "/"
            + nomeArquivo;
    String caminhoArquivoCriptografado = new _Path().getPathFileCripto()
            + "/" + nomeArquivo;

    String key = null;
    ;

    try {
        FileInputStream fis = new FileInputStream(new File(
                caminhoArquivoPuro));
        File outfile = new File(caminhoArquivoCriptografado);

        int read;
        if (!outfile.exists())
            outfile.createNewFile();

        FileOutputStream fos = new FileOutputStream(outfile);
        FileInputStream encfis = new FileInputStream(outfile);

        Cipher encipher = Cipher.getInstance("AES");

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecretKey skey = kgen.generateKey();
        encipher.init(Cipher.ENCRYPT_MODE, skey);
        CipherInputStream cis = new CipherInputStream(fis, encipher);

        key = bytesToString(skey.getEncoded());

        byte[] b = stringToBytes(key);

        while ((read = cis.read()) != -1) {
            fos.write((char) read);
            fos.flush();
        }
        fos.close();

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        entityFiles file = new entityFiles();

        file.setCaminhoArquivoPuro(caminhoArquivoPuro);
        file.setCaminhoArquivoCriptografado(caminhoArquivoCriptografado);
        file.setKeyArquivo(key);
        file.setNomeArquivo(nomeArquivo);

        ArquivoDataSource datasource = new ArquivoDataSource(c);

        datasource.open();

        file = datasource.createRegistro(file);

        datasource.close();
    }

    return key;
}

public static void decrypt(Context c, String nomeArquivo, String key) {

    String caminhoArquivoPuro = new _Path().getPathFileCripto() + "/"
            + nomeArquivo;
    String caminhoArquivoCriptografado = new _Path().getPathFileDescripto()
            + "/" + nomeArquivo;

    try {
        FileInputStream fis = new FileInputStream(new File(
                caminhoArquivoPuro));
        File outfile = new File(caminhoArquivoPuro);

        int read;
        if (!outfile.exists()) {
            outfile.createNewFile();
        }

        File decfile = new File(caminhoArquivoCriptografado);

        if (!decfile.exists()) {
            decfile.createNewFile();
        }

        FileInputStream encfis = new FileInputStream(outfile);
        FileOutputStream decfos = new FileOutputStream(decfile);

        Cipher decipher = Cipher.getInstance("AES");            



        SecretKey originalKey = new SecretKeySpec(stringToBytes(key), 0,
                stringToBytes(key).length, "AES");


        decipher.init(Cipher.DECRYPT_MODE, originalKey);
        CipherOutputStream cos = new CipherOutputStream(decfos, decipher);

        while ((read = encfis.read()) != -1) {
            cos.write(read);
            cos.flush();
        }
        cos.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static String bytesToString(byte[] b) {
    byte[] b2 = new byte[b.length + 1];
    b2[0] = 1;
    System.arraycopy(b, 0, b2, 1, b.length);
    return new BigInteger(b2).toString(36);
}

public static byte[] stringToBytes(String s) {
    byte[] b2 = new BigInteger(s, 36).toByteArray();
    return Arrays.copyOfRange(b2, 1, b2.length);
}

 }
