private void performDecryption(DocumentModel document)
    {                               
        byte[] keyBytes = generateByteArray(document.getEncryptionKey());


        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");

        File encryptedDocument = new File(getBookFolderDocumentName(document, document.getFileSuffix()));
        File decryptedDocument = new File(BOOK_FOLDER + document.getGeneratedAssetName() + "_decrypted" + "." + document.getFileSuffix());

        decryptedDocument.mkdirs();
        if (decryptedDocument.exists())
            decryptedDocument.delete();


        Cipher cipher = null;    

        try
        {

            cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);         
        } 
        catch (NoSuchAlgorithmException noSuchAlgorithmEx)
        {
            Log.e("Decryption", "NoSuchAlgorithmException: " + noSuchAlgorithmEx.getMessage());
        }
        catch (NoSuchPaddingException noSuchPaddingEx)
        {
            Log.e("Decryption", "NoSuchPaddingException: " + noSuchPaddingEx.getMessage());
        }
        catch (InvalidKeyException invalidKeyEx)
        {
            Log.e("Decryption", "InvalidKeyException: " + invalidKeyEx.getMessage());
        } 

        FileInputStream encryptedFileStream = null;
        FileOutputStream decryptedFileStream = null;


        try
        {

            encryptedFileStream = new FileInputStream(encryptedDocument);
            decryptedFileStream = new FileOutputStream(decryptedDocument);



            long totalFileSize = encryptedDocument.length();
            long totalDecrypted = 0;
            int lastPercentage = -1;
            int currentPercentage = 0;

            byte[] encryptedBuffer = new byte[4096];
            byte[] decryptedBuffer = new byte[4096];
            int encryptedLength = 0;
            int decryptedLength = 0;

            while((encryptedLength = encryptedFileStream.read(encryptedBuffer)) > 0)
            {   
                while (encryptedLength % 16 != 0) // the code never lands in this loop
                {                   
                    encryptedBuffer[encryptedLength] = 0;
                    encryptedLength++;
                }

                decryptedLength = cipher.update(encryptedBuffer, 0, encryptedLength, decryptedBuffer);


                while (decryptedLength % 16 != 0) // the code never lands in this loop
                {
                    decryptedBuffer[decryptedLength] = 0;
                    decryptedLength++;
                }

                decryptedFileStream.write(decryptedBuffer, 0, decryptedLength);


                totalDecrypted += encryptedLength;

                currentPercentage = (int)(((float)totalDecrypted / (float)totalFileSize) * 100f);

                if (currentPercentage != lastPercentage)
                {
                    lastPercentage = currentPercentage;
                    Log.i("Decryption", "Decrypting... " + currentPercentage + "%");
                }
            }




            Log.i("Decryption", "Finished decrypting!");
        }
        catch (FileNotFoundException fileNotFoundEx)
        {
            Log.e("Decryption", "FileNotFoundException: " + fileNotFoundEx.getMessage());
        }
        catch (IOException ioEx)
        {
            Log.e("Decryption", "IOException: " + ioEx.getMessage());
        } 
        catch (ShortBufferException e) 
        {       
            e.printStackTrace();
        }
        finally
        {

        }

        try 
        {                   
            encryptedFileStream.close();
            decryptedFileStream.close();
            cipherOutputStream.close();         
        } 
        catch (IOException e1) 
        {

        }


        document.setDecryptedFilePath(decryptedDocument.getAbsolutePath());



        Log.i("Decryption", "Finished!");
    }
