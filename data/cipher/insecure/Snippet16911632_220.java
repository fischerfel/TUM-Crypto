public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

    // Инициализация секретных ключей
    KeyGenerator keyGenS = KeyGenerator.getInstance("AES");
    keyGenS.init(128);
    SecretKey sKey1 = keyGenS.generateKey();
    SecretKey sKey2 = keyGenS.generateKey();
    // Перевод секретных ключей в строку и запись в файл
    String key1 = SecretKeyToString(sKey1);
    String key2 = SecretKeyToString(sKey2);

    spreader.write(fileName1, key1);
    spreader.write(fileName2, key2);
    spreader.write(fileNameS1, key1);
    spreader.write(fileNameS2, key2);


    // Чтение секретных ключей из файла и перевод обратно в тип SecretKey
    key1 = spreader.read(fileName1);
    System.out.println("Секретный ключ 1го пользователя: " +key1);


    SecretKey seansKey1=getKeyInstance(key1);

    key2 = spreader.read(fileName2);
    System.out.println("Секретный ключ 2го пользователя: " +key2);

    SecretKey seansKey2=getKeyInstance(key2);


    //инициализация и зашифрование сеансового ключа с помощью секретных
    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE,seansKey1);

    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();

    String stringsecretKey = SecretKeyToString(secretKey);
    byte[] byteKey = stringsecretKey.getBytes();
    byte[] byteCipherKey1 = aesCipher.doFinal(byteKey); 
    String encryptedKey = new BASE64Encoder().encode(byteCipherKey1);
    System.out.println("Зашифрованный сеансовый ключ с помощью секретного ключа 1: " +encryptedKey);





    aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE,SeansKey2);


     byteKey = etringsecretKey.getBytes();
     byte[] byteCipherKey2 = aesCipher.doFinal(byteKey); 
     encryptedKey = new BASE64Encoder().encode(byteCipherKey2);
    System.out.println("Зашифрованный сеансовый ключ с помощью секретного ключа 2: " +encryptedKey);
    spreader.write(fileNameEK2, encryptedKey);

    //Чтение данных из файла
    String text =spreader.read(fileName);
    System.out.println(text);

    // Зашифрование данных


            aesCipher.init(Cipher.ENCRYPT_MODE,secretKey); // константная переменная

            byte[] byteText = text.getBytes();
            byte[] byteCipherText = aesCipher.doFinal(byteText); 
            encryptedText = new BASE64Encoder().encode(byteCipherText);
            System.out.println("Зашифрованный текст: " +encryptedText);

            spreader.write(fileNameOK, encryptedText);





}
