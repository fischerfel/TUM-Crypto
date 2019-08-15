    desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

        //sensitive information
        byte[] text = "This is sample text for encryption".getBytes();

        System.out.println("Text [Byte Format] : " + text);
        System.out.println("Text : " + new String(text));

        // Encrypt the text
        byte[] textEncrypted = desCipher.doFinal(text);
