public static void main(String[] args) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
    final byte[] IV_PARAM = {
        0x06,
        0x07,
        0x08,
        0x09,
        0x0A,
        0x0B,
        0x0C,
        0x0D,
        0x0E,
        0x0F,
        0x00,
        0x01,
        0x02,
        0x03,
        0x04,
        0x05,
    };

    // MAGATZEM DE CLAUS
    String magatzemClaus = null;
    String contraMagatzemClaus = null;
    String alias = null;
    String contraAlias = null;
    String text = null;
    String concatenat = null;

    int opcio = 0;

    KeyStore keystore = null;
    SecretKey secretkey = null;
    PublicKey publickey = null;
    PrivateKey privateKey = null;

    byte[] missatgeEncriptat = null;
    byte[] clauEncriptada = null;
    byte[] clauMissatgeEncriptat = null;
    byte[] missatgeADesencriptar = null;
    byte[] missatgeDesencriptat = null;
    byte[] clau = null;

    // SCANNER
    Scanner teclat = new Scanner(System.in);

    // DEMANEM EL TEXT A ENCRIPTAR
    System.out.print("Introdueix el text: ");
    text = teclat.next();

    // DEMANEM MAGATZEM DE CLAUS
    System.out.print("MAGATZEM DE CLAUS: QuiMatA3\n");
    magatzemClaus = "QuiMatA3";

    // DEMANEM CONTRASENYA PER AL MAGATZEM DE CLAUS
    System.out.print("CONTRASENYA DE [" + magatzemClaus + "]: QuiMatA3\n");
    contraMagatzemClaus = "QuiMatA3";

    //DEMANEM OPCIO
    System.out.println("Transformaci� a realitzar [1] xifrar [2] desxifrar.");
    opcio = teclat.nextInt();

    //////////////////////////////////////////////
    //  KEYSTORE
    //////////////////////////////////////////////

    try {
        keystore = KeyStore.getInstance("JKS");
        File arxiu = new File(magatzemClaus + ".jks");
        if (arxiu.isFile()) {
            FileInputStream llegeix = new FileInputStream(arxiu);
            keystore.load(llegeix, contraMagatzemClaus.toCharArray());
        }
    } catch (KeyStoreException e) {
        System.err.println("error keystore: " + e.toString());
    } catch (FileNotFoundException e) {
        System.err.println("Error fileinput: " + e.toString());
    } catch (Exception e) {
        System.err.println("error al carregar: " + e.toString());
    }

    switch (opcio) {
        case 1:

            ///////////////////////////////////////////
            //      GENEREM SECRET KEY
            ///////////////////////////////////////////

            try {

                //GENEREM LA CLAU SECRETA
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(192);
                secretkey = keyGenerator.generateKey();

                // ALIAS DE LA CLAU
                System.out.println("ALIAS DE LA CLAU [" + contraMagatzemClaus + "]: QuiMat\n");
                alias = "QuiMat";

                //////////////////////////////////
                // ENCRIPTAR
                /////////////////////////////////
                Cipher xifrar = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec ivParam = new IvParameterSpec(IV_PARAM);
                xifrar.init(Cipher.ENCRYPT_MODE, secretkey, ivParam);
                missatgeEncriptat = xifrar.doFinal(text.getBytes());

            } catch (Exception e) {
                System.err.println("Error al generar clau secreta o al xifrar!" + e.toString());
            }

            //GENEREM LA CLAU PUBLICA

            try {
                publickey = keystore.getCertificate(alias).getPublicKey();
            } catch (KeyStoreException e) {
                System.err.println("Error al generar clau p�blica! " + e.toString());
            }

            //ENCRPITEM CLAU SIM�TRICA AMB LA CLAU P�BLICA
            // CLAU ENCRIPTADA
            Cipher xifrar;
            try {
                xifrar = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                xifrar.init(Cipher.ENCRYPT_MODE, publickey);
                clauEncriptada = xifrar.doFinal(secretkey.getEncoded());
            } catch (Exception e) {
                System.err.println("Error al encriptar la clau sim�trica: " + e.toString());
            }

            // CONCATENEM
            concatenat = MadMarConverter.byteArrayToHexString(missatgeEncriptat) +
                MadMarConverter.byteArrayToHexString(clauEncriptada);
            System.out.println("Concatenaci� clau i text: " + concatenat);

            break;

        case 2:

            ////////////////////////////////////////////////////////////////////
            ////////////            DESENCRIPTAR
            ////////////////////////////////////////////////////////////////////

            // ALIAS DE LA CLAU
            System.out.println("ALIAS DE LA CLAU [" + contraMagatzemClaus + "]: QuiMat\n");
            alias = "QuiMat";
            //teclat.next();

            // CONTRASENYA ALIAS
            System.out.println("CONTRASENYA DE L'ALIAS  [" + alias + "]:QuiMatP\n");
            contraAlias = "QuiMatP";

            clauMissatgeEncriptat = MadMarConverter.hexStringToByteArray(text);

            //SEPAREM CLAU SIMETRICA DE MISSATGE ENCRIPTAT
            clau = Arrays.copyOfRange(clauMissatgeEncriptat, 0, 256); //GUARDEM CLAU EN UN ARRAY
            missatgeADesencriptar = Arrays.copyOfRange(clauMissatgeEncriptat, 256, clauMissatgeEncriptat.length); //MISSATGE EN UN ARRAY

            //CLAU PRIVADA

            privateKey = (PrivateKey) keystore.getKey(alias, contraAlias.toCharArray());

            /////////////////////////////////////
            //      DESXIFRAT CLAU SIMETRICA
            /////////////////////////////////////
            byte[] clauDesencriptada = null;

            try {

                Cipher desxifrar = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                desxifrar.init(Cipher.DECRYPT_MODE, privateKey);
                clauDesencriptada = desxifrar.doFinal(clau);
            } catch (Exception e) {
                System.err.println("Error al desencriptar clau: " + e.toString());
            }

            secretkey = new SecretKeySpec(clauDesencriptada, 0, clauDesencriptada.length, "AES");

            /////////////////////////////////////////////////////
            //          DESXIFRAR MISSATGE
            /////////////////////////////////////////////////////
            try {
                Cipher desxifrar = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec ivParam = new IvParameterSpec(IV_PARAM);
                desxifrar.init(Cipher.DECRYPT_MODE, secretkey, ivParam);
                missatgeDesencriptat = desxifrar.doFinal(missatgeADesencriptar);
            } catch (Exception e) {
                System.err.println("Error al desencriptar missatge: " + e.toString());
            }

            System.out.println("Miisatge desencriptat: " + new String(missatgeDesencriptat));
            break;
        case 0:
            System.out.println("deu!");
            break;
    }

    teclat.close();
}
