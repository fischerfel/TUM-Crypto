public class MySecurity
{
    private static Cipher ecipher;
    private Cipher dcipher;
    private static String Password = "$emP0sTM@rkT0P$3cu!ty12345678912";
    private static String InitialVector = "OFRna73m*aze01xY";

    // ENCRYPTION ******************************************************
    public String encryptText(String plainText)
    {
        String errorText = "Error";

        try {
            SecretKeySpec secretKey = new SecretKeySpec(Password.getBytes("ASCII"), "AES");
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(InitialVector.getBytes("ASCII")));
             // Encode the string into bytes using utf-8
            byte[] utf8 = plainText.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);
             // Encode bytes to base64 to get a string
            return Base64.encodeToString(enc, Base64.DEFAULT);  

        } catch (InvalidKeyException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (BadPaddingException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }


        return errorText;
    }

    public static boolean encryptFile(String filePath)
    {

        try {
            SecretKeySpec secretKey = new SecretKeySpec(Password.getBytes("ASCII"), "AES");
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(InitialVector.getBytes("ASCII")));

            //create input stream to read in file that needs to be encrypted
            FileInputStream inputStream = new FileInputStream(filePath);
            //create output stream to write out the encrypted results, append .vault to out encrypted files
            FileOutputStream outputStream = new FileOutputStream(filePath + ".vault");
            //wrap the output stream
            CipherOutputStream encryptedOutputStream = new CipherOutputStream(outputStream, ecipher);

            // Encrypt the file
            int bytes;
            byte[] data = new byte[8];
            while((bytes = inputStream.read(data)) != -1)
            {
                encryptedOutputStream.write(data, 0, bytes);
            }

            // Flush and close streams.
            encryptedOutputStream.flush();
            encryptedOutputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return false;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } 
        return true;
    }

    public String decryptText(String cryptoText)
    {
        String errorText = "Error";

        try {
            SecretKeySpec secretKey = new SecretKeySpec(Password.getBytes("ASCII"), "AES");
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(InitialVector.getBytes("ASCII")));

        // Decode base64 to get bytes
            byte[] dec = Base64.decode(cryptoText, Base64.DEFAULT);        
         // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);        
         // Decode using utf-8
            return new String(utf8, "UTF8");    

        } catch (InvalidKeyException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        } catch (BadPaddingException e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }  
        return errorText;
    } 

    public boolean decryptFile(String filePath)
    {

        try {
            SecretKeySpec secretKey = new SecretKeySpec(Password.getBytes("ASCII"), "AES");
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(InitialVector.getBytes("ASCII")));

            //create input stream to read in file that needs to be decrypted
            FileInputStream inputStream = new FileInputStream(filePath);
            //create output stream to write out the decrypted results, remove .vault to from file
            FileOutputStream outputStream = new FileOutputStream(filePath.replace(".vault", filePath));
            //wrap the output stream
            CipherInputStream encryptedInputStream = new CipherInputStream(inputStream, dcipher);

            //Decrypt the file
            int bytes;
            byte[] data = new byte[8];
            while((bytes = encryptedInputStream.read(data)) != -1)
            {
                outputStream.write(data, 0, bytes);
            }

            // Flush and close streams.
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            encryptedInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return false;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } 
        return true;
    }
}
