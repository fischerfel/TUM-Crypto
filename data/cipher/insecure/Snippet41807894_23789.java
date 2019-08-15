    EnvoiRequete(cliSock, new RequeteBIMAP(TypesReq.GET_NEXT_BILL, params));
    ReponseP rep = (ReponseP)LectureReponse(cliSock);

    if(rep.getTypeRet() == Reponse.TypesRep.OK){
        try {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) cliKs.getEntry("clesecComptChiffr", new KeyStore.PasswordProtection("pass".toCharArray()));
            SecretKey cle = secretKeyEntry.getSecretKey();

            Cipher dechiffr = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
            dechiffr.init(Cipher.DECRYPT_MODE, cle);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dechiffr.doFinal((byte[])rep.getParams().get(0)));

            String[][] stringArray2= null;
             try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                stringArray2 = (String[][]) objectInputStream.readObject();
             }

            ListeFactures lf= new ListeFactures(stringArray2);
            lf.setVisible(true);

            lastGetID= Integer.valueOf(stringArray2[0][0]);
            FieldFacture.setText(stringArray2[0][0]);

        } catch (ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | IOException | IllegalBlockSizeException | BadPaddingException | UnrecoverableEntryException | KeyStoreException ex) {
            Logger.getLogger(Comptable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else
        JOptionPane.showMessageDialog(null, rep.getParams().get(0));
