package com.myprotection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto
{
    private String mPassword = null;
    byte [] mInitVec = null;
    byte [] mSalt = null;
    Cipher mEcipher = null;
    Cipher mDecipher = null;
    private final int KEYLEN_BITS = 256;
    private final int ITERATIONS = 65536;
    private final int MAX_FILE_BUF = 1024;


    public Crypto (String password, String hint) throws Exception
    {
        mPassword = password;
        mSalt = getSaltedByteFromString( hint );
    }


    public void WriteEncryptedFile (String input, String output) throws Exception {

        FileInputStream fin = new FileInputStream (new File(input));
        FileOutputStream fout = new FileOutputStream (new File(output));
        int nread = 0;
        byte [] inbuf = new byte [MAX_FILE_BUF];

        //Initialize Encryption Vector
        initializeInitVec();

        while ((nread = fin.read (inbuf)) > 0 )
        {

            // create a buffer to write with the exact number of bytes read. Otherwise a short read fills inbuf with 0x0
            // and results in full blocks of MAX_FILE_BUF being written. 
            byte [] trimbuf = new byte [nread];
            for (int i = 0; i < nread; i++)
                trimbuf[i] = inbuf[i];

            // encrypt the buffer using the cipher obtained previosly
            byte [] tmp = mEcipher.update (trimbuf);

            // I don't think this should happen, but just in case..
            if (tmp != null)
                fout.write (tmp);
        }

        // finalize the encryption since we've done it in blocks of MAX_FILE_BUF
        byte [] finalbuf = mEcipher.doFinal ();
        if (finalbuf != null)
            fout.write (finalbuf);

        fout.flush();
        fin.close();
        fout.close();
        fout.close ();
    }


    public void ReadEncryptedFile (String input, String output) throws Exception {

        FileInputStream fin = new FileInputStream (new File(input));
        FileOutputStream fout = new FileOutputStream (new File(output));
        CipherInputStream cin;
        int nread = 0;
        byte [] inbuf = new byte [MAX_FILE_BUF];

        //Initializing decrypting 
        setupDecrypt();

        // creating a decoding stream from the FileInputStream above using the cipher created from setupDecrypt()
        cin = new CipherInputStream (fin, mDecipher);

        while ((nread = cin.read (inbuf)) > 0 )
        {

            // create a buffer to write with the exact number of bytes read. Otherwise a short read fills inbuf with 0x0
            byte [] trimbuf = new byte [nread];
            for (int i = 0; i < nread; i++)
                trimbuf[i] = inbuf[i];

            // write out the size-adjusted buffer
            fout.write (trimbuf);
        }

        fout.flush();
        cin.close();
        fin.close ();       
        fout.close();
    }

    private void setupDecrypt () throws Exception {

        SecretKeyFactory factory = null;
        SecretKey tmp = null;
        SecretKey secret = null;

        //Get initialized the vector
        initializeInitVec();

        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(mPassword.toCharArray (), mSalt, ITERATIONS, KEYLEN_BITS);
        tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        /* Decrypt the message, given derived key and initialization vector. */
        mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        mDecipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(mInitVec));
    }

    private void initializeInitVec() throws Exception {

        SecretKeyFactory factory = null;
        SecretKey tmp = null;

        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec (mPassword.toCharArray (), mSalt, ITERATIONS, KEYLEN_BITS);
        tmp = factory.generateSecret (spec);
        SecretKey secret = new SecretKeySpec (tmp.getEncoded(), "AES");

        //Create the Encryption cipher object and store as a member variable
        mEcipher = Cipher.getInstance ("AES/CBC/PKCS5Padding");
        mEcipher.init (Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = mEcipher.getParameters ();

        // get the initialization vectory and store as member var 
        mInitVec = params.getParameterSpec (IvParameterSpec.class).getIV();
    }

    private byte[] getSaltedByteFromString( String hintString ) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(hintString.getBytes("UTF-8"));
        return md.digest();
    }
}
