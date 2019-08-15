case GET_NEXT_BILL:
{
    int lastGetId= (int)params.get(0);
    // recup params
    cs.TraceEvenements(adresseDistante+"#Get facture#"+Thread.currentThread().getName());

    System.out.println("Recuperation de la cle secrète");
    KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) servKs.getEntry("clesecComptChiffr", new KeyStore.PasswordProtection("pass".toCharArray()));
    SecretKey cléSecrète = secretKeyEntry.getSecretKey();

    Cipher chiffr = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
    chiffr.init(Cipher.ENCRYPT_MODE, cléSecrète);

    ResultSet rs = bdAccess.select("SELECT IDFacture, Montant, Destinataire, AdresseLivraison, DATE_FORMAT(DateEmission, '%d-%m-%Y') as DateEmission"
                     + " FROM FACTURES WHERE IDFacture > "+lastGetId+" AND Valide= 0");

    if(rs.next())
    {
        String[][] stringArray = {{String.valueOf(rs.getInt(1)), String.valueOf(rs.getFloat(2)), 
            rs.getString(3), rs.getString(4), rs.getString(5)}};

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(stringArray);

        rep.add(chiffr.doFinal(byteArrayOutputStream.toByteArray()));
         typeRep= TypesRep.OK; 
    }
    else
    {
        rep.add("Il n'y a plus de facture non validée");
        typeRep= TypesRep.ERREUR; 
    }    
    break;
}
