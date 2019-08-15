 public ArrayList<FootballClub> FootBallInputStream() throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        File file = new File("FootballClub.ser");
        fileIn = new FileInputStream(file);
        CipherInputStream CipherIn = new CipherInputStream(fileIn, cipher);
        in = new ObjectInputStream(CipherIn);
        ArrayList<FootballClub> e = (ArrayList<FootballClub>) in.readObject();
        in.close();
        fileIn.close();

        return e;

    }

    public void FootBallOutputStream(ArrayList<FootballClub> e) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        Cipher cipher = (Cipher.getInstance("DES"));
        cipher.init(Cipher.ENCRYPT_MODE, key);
        File file = new File("FootballClub.ser");
        fileOut = new FileOutputStream(file);
        CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
        out = new ObjectOutputStream(cipherOut);
        out.writeObject(e);
        out.close();
        fileOut.close();
    }
