            package com.company.digtal.web.generic.utils;


            import java.io.File;
            import java.io.FileInputStream;
            import java.io.FileNotFoundException;
            import java.io.FileOutputStream;
            import java.io.IOException;
            import java.io.InputStream;
            import java.io.OutputStream;

            import java.security.InvalidKeyException;
            import java.security.Key;

            import java.security.NoSuchAlgorithmException;
            import java.security.NoSuchProviderException;
            import java.security.PrivateKey;
            import java.security.PublicKey;
            import java.security.Security;

            import java.util.Iterator;

            import javax.crypto.Cipher;
            import javax.crypto.CipherInputStream;
            import javax.crypto.CipherOutputStream;
            import javax.crypto.NoSuchPaddingException;

            import org.bouncycastle.jce.provider.BouncyCastleProvider;
            import org.bouncycastle.openpgp.PGPException;
            import org.bouncycastle.openpgp.PGPPrivateKey;
            import org.bouncycastle.openpgp.PGPPublicKey;
            import org.bouncycastle.openpgp.PGPPublicKeyRing;
            import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
            import org.bouncycastle.openpgp.PGPSecretKey;
            import org.bouncycastle.openpgp.PGPSecretKeyRing;
            import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
            import org.bouncycastle.openpgp.PGPUtil;
            import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyConverter;




            public  class CipherEncryptionUtil 
            {

                public static void main( 
                        String[] args) 
                        throws Exception 
                    { 
                        Security.addProvider(new BouncyCastleProvider()); 

                        String clearTxtInputFilePath = "C:\\temp\\delete\\PGP_Test\\big_input.txt";
                        String encryptedFilePath = "C:\\temp\\delete\\PGP_Test\\output\\encrypted_output_big.pgp";
                        String decryptedFilePath = "C:\\temp\\delete\\PGP_Test\\output\\decrypted_output_big.txt";

                        String publicKeyPath = "C:\\temp\\delete\\PGP_Test\\Online-eng-request_public.asc";

                        String privateKeyPath = "C:\\temp\\delete\\PGP_Test\\secring.skr";
                        String passPhrase = "Jabble wants to chase a lady bug";


                        //Encrypt plain txt file with public key
                        OutputStream    out = new FileOutputStream(encryptedFilePath); 

                        FileInputStream fis = new FileInputStream(clearTxtInputFilePath);
                        CipherOutputStream cos = encrypt(out, publicKeyPath); 


                        int i;
                        byte[] block = new byte[2048];
                        while ((i = fis.read(block)) != -1) {
                            cos.write(block, 0, i);
                             }


                        cos.close();
                        fis.close();



                        //Decrypt plain txt file with private key
                        CipherInputStream    cIn = decrypt(new FileInputStream(encryptedFilePath),privateKeyPath, passPhrase.toCharArray()); 
                        FileOutputStream fClearTxtOs = new FileOutputStream(decryptedFilePath);

                        while ((i = cIn.read(block)) != -1) {
                            fClearTxtOs.write(block, 0, i);
                             }
                        fClearTxtOs.close();
                        cIn.close();

                    } 

                public static CipherOutputStream encrypt(OutputStream outputStream, String publicKeyPath)
                {
                    Cipher cipher;
                    Key publicKey = null;

                    try 
                    {
                        cipher = Cipher.getInstance("RSA", "BC");

                    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
                        String msg = "failed to create output stream";
                        System.out.println(  msg + " : " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException( msg, e );
                    }

                    try {
                        publicKey = getPublicKey(publicKeyPath);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                    } catch (InvalidKeyException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    return ( new CipherOutputStream(outputStream, cipher));
                }


                public static CipherInputStream  decrypt(InputStream inputStream, 
                        String  keyIn, 
                        char[]      passwd)
                {
                    Cipher cipher;
                    Key secretKey = null;

                    try 
                    {
                        cipher = Cipher.getInstance("RSA", "BC");

                    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
                        String msg = "failed to create output stream";
                        System.out.println(  msg + " : " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException( msg, e );
                    }

                    try {
                        secretKey = getPrivateKey(new FileInputStream(keyIn),passwd);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);
                    } catch (InvalidKeyException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    return ( new CipherInputStream(inputStream, cipher));
                }







                public static PublicKey getPublicKey(
                    String filePath)
                    throws PGPException, NoSuchProviderException, FileNotFoundException, IOException
                {
                    PGPPublicKey    encKey = readPublicKey(new FileInputStream(filePath));
                    return new JcaPGPKeyConverter().setProvider("BC").getPublicKey(encKey);
                }




                public static PrivateKey getPrivateKey( 
                        InputStream    in, char[]      passwd) 
                        throws IOException, PGPException, NoSuchProviderException 
                    { 
                        in = PGPUtil.getDecoderStream(in); 

                        PGPSecretKeyRingCollection        pgpSec = new PGPSecretKeyRingCollection(in); 

                        // 
                        // we just loop through the collection till we find a key suitable for encryption, in the real 
                        // world you would probably want to be a bit smarter about this. 
                        // 

                        // 
                        // iterate through the key rings. 
                        // 
                        Iterator<?> rIt = pgpSec.getKeyRings(); 

                        while (rIt.hasNext()) 
                        { 
                            PGPSecretKeyRing    kRing = (PGPSecretKeyRing)rIt.next();     
                            Iterator<?>                        kIt = kRing.getSecretKeys(); 

                            while (kIt.hasNext()) 
                            { 
                                PGPSecretKey    k = (PGPSecretKey)kIt.next(); 

                                if (k != null) 
                                { 
                                    PGPPrivateKey pk = k.extractPrivateKey(passwd, "BC"); 
                                     return new JcaPGPKeyConverter().setProvider("BC").getPrivateKey(pk);
                                } 
                            } 
                        } 

                        throw new IllegalArgumentException("Can't find secured key in key ring."); 
                    } 

                public static PGPPublicKey readPublicKey( 
                        InputStream    in) 
                        throws IOException, PGPException 
                    { 
                        in = PGPUtil.getDecoderStream(in); 

                        PGPPublicKeyRingCollection        pgpPub = new PGPPublicKeyRingCollection(in); 

                        // 
                        // we just loop through the collection till we find a key suitable for encryption, in the real 
                        // world you would probably want to be a bit smarter about this. 
                        // 

                        // 
                        // iterate through the key rings. 
                        // 
                        Iterator<?> rIt = pgpPub.getKeyRings(); 

                        while (rIt.hasNext()) 
                        { 
                            PGPPublicKeyRing    kRing = (PGPPublicKeyRing)rIt.next();     
                            Iterator<?>                        kIt = kRing.getPublicKeys(); 

                            while (kIt.hasNext()) 
                            { 
                                PGPPublicKey    k = (PGPPublicKey)kIt.next(); 

                                if (k.isEncryptionKey()) 
                                { 
                                    return k; 
                                } 
                            } 
                        } 

                        throw new IllegalArgumentException("Can't find encryption key in key ring."); 
                    } 




            }
