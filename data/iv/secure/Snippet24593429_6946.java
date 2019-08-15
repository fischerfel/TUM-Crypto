private static void decryptFile(String file, String password, byte[] Salt, byte[] IV, String attachmentunecryptedhash, String attachmentoriginalsizeString, String attachmentcreated)
{
        long attachmentSize = 0;
        String attachmentCreated = null;
        String attachmentModified = null;
        String attachmentHash = null;
        byte[] buffers = new byte[16];
        byte[] endOfFile = new byte[16];
        int counterForFile = 0;
        int attachmentoriginalsize = 0;
        int noBytes = 0;

        try
        {
            if(Paths.get(file.trim()).toFile().exists() == true) //If the File Exists
            {
                //Creates Secret Key For Decryption -- Passes Password, Salt, Iterations, and Key Length
                PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), Salt, 65536, 256);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                SecretKey secretKey = factory.generateSecret(keySpec);
                SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

                //Initalizes Cipher For Decrypt Mode -- Passes Password and Salt
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(IV));

                //Prepares To Write New Buffer Containing Decrypted Information to Output File
                String unencryptedFile = file.trim().replaceAll(".aes", "");
                FileInputStream fileInputStream = new FileInputStream(file.trim());
                FileOutputStream fileOutputStream = new FileOutputStream(unencryptedFile);
                attachmentoriginalsize = Integer.parseInt(attachmentoriginalsizeString);



                //Writes Encrypted File to Disk Using Secure Cipher Output Stream
                while((noBytes = fileInputStream.read(buffers)) != -1)
                {

                    //Writes 1 encrypted byte at a time
                    fileOutputStream.write(cipher.update(buffers,0,noBytes));
                    //counterForFile += 16;
                }

                buffers = cipher.doFinal();  //Line 1437 Where the Error Exists

                fileOutputStream.write(buffers);
                fileOutputStream.flush();


                //Close Files, Cleanup
                fileInputStream.close();
                fileOutputStream.close();

                System.exit(1);

}
