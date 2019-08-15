File saveFile = new File("Settings.set");
        saveFile.delete();
        FileOutputStream fout = new FileOutputStream(saveFile);

        //Encrypt the settings
        //Generate a key
        byte key[] = "My Encryption Key98".getBytes();
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey skey = keyFactory.generateSecret(desKeySpec);

        //Prepare the encrypter
        Cipher ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, skey);
        // Seal (encrypt) the object
        SealedObject so = new SealedObject(this, ecipher);

        ObjectOutputStream o = new ObjectOutputStream(fout);
        o.writeObject(so);
        o.close();
