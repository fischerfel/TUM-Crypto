    public static void DecryptFile(String inFile, String outFile,
        PrivateKey rsaPrivateKey) {

    FileOutputStream outFs = null;

    try {
        // Create instance of AesManaged for
        // symetric decryption of the data.
        AesManaged aesManaged = new AesManaged();
        {
            aesManaged.keySize = 256;
            aesManaged.blockSize = 128;
            aesManaged.mode = "AES/CBC/PKCS5Padding";

            // Create byte arrays to get the length of
            // the encrypted key and IV.
            // These values were stored as 4 bytes each
            // at the beginning of the encrypted package.
            byte[] LenK = new byte[4];
            byte[] LenIV = new byte[4];

            // Use FileStream objects to read the encrypted
            // file (inFs) and save the decrypted file (outFs).
            {
                byte[] fileBytes = FileUtils.readFileToByteArray(new File(inFile));

                ByteArrayInputStream inFs = new ByteArrayInputStream(
                        fileBytes);
                ;
                for (int i = 0; i < LenK.length; i++) {
                    LenK[i] = (byte) inFs.read();
                }
                for(int i = 0; i< LenIV.length;i++){
                    LenIV[i] = (byte)inFs.read();
                }

                // Convert the lengths to integer values.
                int lenK = BitConverter.ToInt32(LenK, 0);
                int lenIV = BitConverter.ToInt32(LenIV, 0);

                //int startC = lenK + lenIV + 8;
                //int lenC = (int) fileBytes.length - startC;

                // Create the byte arrays for
                // the encrypted AesManaged key,
                // the IV, and the cipher text.
                byte[] KeyEncrypted = new byte[lenK];
                byte[] IV = new byte[lenIV];

                // Extract the key and IV
                for(int i = 0;i<lenK;i++){
                    KeyEncrypted[i] = (byte)inFs.read();
                }
                for(int i =0;i<lenIV;i++){
                    IV[i] = (byte)inFs.read();
                }
                // to decrypt the AesManaged key.
                byte[] KeyDecrypted = decryptKey(KeyEncrypted,rsaPrivateKey);

                Cipher transform = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec ivspec = new IvParameterSpec(IV);

                SecretKeySpec secretKeySpec = new SecretKeySpec(KeyDecrypted, "AES");

                transform.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
                // Decrypt the key.

                outFs = new FileOutputStream(outFile);

                int count = 0;
                int offset = 0;
                int blockSizeBytes = aesManaged.blockSize / 8;
                byte[] data = new byte[blockSizeBytes];

                CipherInputStream cipherIn = new CipherInputStream(
                        inFs, transform);
                while ((count = cipherIn.read(data, 0, blockSizeBytes)) != -1) {
                    outFs.write(data, 0, count);
                }

                inFs.close();
                cipherIn.close();
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
