    public static byte[] verschlüsseln(String daten) throws Exception {
        // Benötigt: daten, DreifachDES.password, DreifachDES.macString
        // Ändert: saltString
        // Ausführt: Verschlüsselt "daten," 3DES mit Salt und ein MAC wird
        // benutzt.
        // hash(DreifachDES.password + salt) ist der Schlüssel.
        // Der Output ist ein byte[]

        // Erzeugen Digest für Passwort + Salt
        password="testForNathan";
        final MessageDigest md = MessageDigest.getInstance("SHA1");

        // Erzeugen zufällig 24 Byte Salt
        Random züfallig = new SecureRandom();
        byte[] salt = new byte[24];
        String saltString = Arrays.toString(salt);
        new Base64(true);
        saltString = new String(salt, "UTF-8");
        byte[] unhashedBytes = (password+saltString).getBytes("UTF-8");

        final byte[] keyBytes2 = unhashedBytes;

        System.out.println("Hex key before hash: " + bytesToHex(unhashedBytes));



        //Hash the pw+salt
        byte[] digestVonPassword = md.digest(keyBytes2);

        byte[] digestVonPassword2 = new byte[digestVonPassword.length + salt.length];
        System.arraycopy(digestVonPassword, 0, digestVonPassword2, 0, digestVonPassword.length);
        System.arraycopy(salt, 0, digestVonPassword2, digestVonPassword.length, salt.length);

        // Wir brauchen nur 24 Bytes, benutze die Erste 24 von der Digest
        final byte[] keyBytes = Arrays.copyOf(digestVonPassword2, 24);

        // Erzeugen der Schlüssel
        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");

        // Erzeugen eine züfallig IV
        byte[] ivSeed = new byte[8];
        final IvParameterSpec iv = new IvParameterSpec(ivSeed);

        // Erzeugen Cipher mit 3DES, CBC und PKCS5Padding
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        // Erzeugen byte[] von String message
        final byte[] plainTextBytes = daten.getBytes("UTF-8");
        byte[] vorIvCipherText = cipher.doFinal(plainTextBytes);

        // Erzeugen die MAC (Message Authentication Code, Mesage
        // Authentifizierung Chiffre)
        // Später mache ich einmal ein zufällig String, und wir benutzen das
        // immer.
        SecretKeySpec macSpec = new SecretKeySpec(
                keyBytes2, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(macSpec);
        byte[] macBytes = mac.doFinal(macString.getBytes());
        System.out.println("Hex version of MAC: " + bytesToHex(macBytes));


        // Erzeugen byte outputStream um die Arrays zu verbinden
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();

        // Verbinden IV, Salt, MAC, und verschlüsselt String
        ostream.write(cipher.getIV());
        ostream.write(salt);
        ostream.write(macBytes);
        ostream.write(vorIvCipherText);

        final byte[] cipherText = ostream.toByteArray();

        return cipherText;
    }
