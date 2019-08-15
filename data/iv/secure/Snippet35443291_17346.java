  int length = dis.readInt();//recieve length of byte array for incoming message
        byte[] encryptedMessage = new byte[length];//create a byte array to the length recieved
        dis.readFully(encryptedMessage);//fill the byte array with incoming data

        //decrypt using AES

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");//create a cipher with correct parameters
        IvParameterSpec ivParaSpec = new IvParameterSpec(iv);//create IvParameter spec using IV provided in assignment brief

        aesCipher.init(Cipher.DECRYPT_MODE,key,ivParaSpec);//initialise the Cipher in DECRYPT mode
        byte[] decryptedMessage = aesCipher.doFinal(encryptedMessage);//create decryptedMessage and put in byte array

        String decMess = new String(decryptedMessage,"UTF-8");

        System.out.println("User ID:");
        System.out.println(uid);

        System.out.println("Decrypted Message:");
        System.out.println(decMess);
