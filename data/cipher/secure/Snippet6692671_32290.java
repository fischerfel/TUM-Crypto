public class Fileencrypt {

     public static void main(String args[])  throws IOException, InvalidKeyException, java.security.InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, KeyStoreException, CertificateException, CertificateEncodingException, IllegalStateException, NoSuchProviderException, SignatureException, UnrecoverableKeyException{   
     try{ 

         byte[] plainData;
         byte[] encryptedData = null;

         KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
         kpg.initialize(2048);
         KeyPair kp = kpg.genKeyPair();
         PublicKey publicKey = kp.getPublic();
         PrivateKey privateKey = kp.getPrivate();



         Cipher cipher = Cipher.getInstance("RSA");
         cipher.init(Cipher.ENCRYPT_MODE, publicKey);

         try { 
         X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream("C:\\Output\\Publickey.txt");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();
        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        fos = new FileOutputStream("C:\\Output\\Privatekey.txt");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }





         File f = new File("C:\\Output\\text.txt");
         FileInputStream in = new FileInputStream(f);
         plainData = new byte[(int)f.length()];
         in.read(plainData);

         try {
            encryptedData = cipher.doFinal(plainData);
        } catch (IllegalBlockSizeException e) {

            e.printStackTrace();
        } catch (BadPaddingException e) {

            e.printStackTrace();
        }

        System.out.println(encryptedData); 
        FileOutputStream target = new FileOutputStream(new File("C:\\Output\\encrypted.txt"));
         target.write(encryptedData);
         target.close();   
     }   
     catch(IOException e){e.printStackTrace();}   
     catch(InvalidKeyException ei){ei.printStackTrace();
     }   
     }   
   }
