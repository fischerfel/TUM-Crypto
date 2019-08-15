public boolean decryptFile(String filePath)
    {

        try {
            SecretKeySpec secretKey = new SecretKeySpec(Password.getBytes("ASCII"), "AES");
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(InitialVector.getBytes("ASCII")));

            //Creating File object to into FileInputStream() constructor
            String fileDirectory = Environment.getExternalStorageDirectory().getPath() + filePath;
            String actualFilePath = fileDirectory + "/" + "Text.txt";
            File actualFile = new File(actualFilePath);

            //create input stream to read in file that needs to be decrypted
            FileInputStream inputStream = new FileInputStream(actualFile);

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
