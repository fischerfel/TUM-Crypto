    moteurCryptage = Cipher.getInstance("PBEWithMD5AndDES");

        PBEKeySpec spécifClé=new PBEKeySpec(mdp.toCharArray());
        SecretKeyFactory usineàClefs=SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey clé=null;
        try {
            clé = usineàClefs.generateSecret(spécifClé);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(DiskUtilView.class.getName()).log(Level.SEVERE, null, ex);
        }

    moteurCryptage.init(Cipher.ENCRYPT_MODE,clé);
        byte[] paramètresEncodage;
        try {
            paramètresEncodage=moteurCryptage.getParameters().getEncoded();
        } catch (IOException ex) {
            paramètresEncodage=null;
        }

    destination=moteurCryptage.update(source1.getBytes());
    destination=moteurCryptage.doFinal(source2.getBytes());

    moteurCryptage.init(Cipher.DECRYPT_MODE,clé,paramètresEncodage);

    source=new String(moteurCryptage.doFinal(destination));
