 public void marsEncryption(Integer dlugoscKlucza, File file, List<User> listaOdbiorcow, ProgressBar pb,String mode) throws NoSuchAlgorithmException, NoSuchProviderException,
        InvalidKeyException, NoSuchPaddingException, IOException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

    //Generowanie klucza
    SecretKey secretKey = generateMarsSymetricKey(dlugoscKlucza);

    //inicjalizacja
    Cipher cipher = Cipher.getInstance("MARS/"+mode+"/PKCS5Padding", "IAIK");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    //Pobranie wektora poczatkowego
    byte[] ivBytes = cipher.getIV();
    IvParameterSpec iv = new IvParameterSpec(ivBytes);

    //Zamiana klucza na tablice bajtow
    byte[] KeyBytes = secretKey.getEncoded();

    //Zaszyfrowanie klucza symetrycznego kluczem publicznym odbiorcow
    for (User odbiorca : listaOdbiorcow) {
        byte[] encryptedKey = rsaEncryption(KeyBytes, odbiorca.publicKey);
        odbiorca.symetricKey = Base64.encodeBase64String(encryptedKey);
    }

    //Zapisanie do pliku xmla
    XMLFactory.createXML(dlugoscKlucza, ivBytes, listaOdbiorcow, file.toString());
    //Szyforwanie pliku
    String newFile = file.toString() + "Crypt.xml";
    encryptTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            FileOutputStream fos = new FileOutputStream(newFile, true);
            try (FileInputStream fis = new FileInputStream(file.toString())) {
                byte[] block = new byte[(int) (128)];
                double postep = 0;
                double postepMax = file.length();
                updateProgress(0, postepMax);
                int i;
                while ((i = fis.read(block)) != -1) {
                    //Szyfrowanie
                    byte[] block2 = cipher.update(block, 0, i);
                    //Zapisanie do pliku
                    postep += i;
                    updateProgress(postep, postepMax);
                    if (isCancelled()) {
                        fis.close();
                        fos.close();
                        File f = new File(newFile);
                        f.delete();
                        updateProgress(0, postepMax);
                        break;
                    }
                    fos.write(block2);
                }
                byte[] outputFinalUpdate = cipher.doFinal();
                fos.write(outputFinalUpdate);
                return null;
            }
        }
    };
    pb.progressProperty().bind(encryptTask.progressProperty());
    new Thread(encryptTask).start();
}
 private SecretKey generateMarsSymetricKey(int dlugoscKlucza) throws NoSuchAlgorithmException, NoSuchProviderException {
    KeyGenerator keyGen = KeyGenerator.getInstance("MARS", "IAIK");
    System.out.println("Wybrana długość klucza: " + dlugoscKlucza);
    keyGen.init(dlugoscKlucza);
    SecretKey secretKey = keyGen.generateKey();
    return secretKey;
}
