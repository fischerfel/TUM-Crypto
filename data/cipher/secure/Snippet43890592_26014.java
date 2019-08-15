public class Move
{
    private static Move instance;

    String key = "pT5IkWNR90gJo5YM";
    String initVector = "RandomInitVector";
    Cipher cipher;


    private Move()
    {
//      try
//      {
//          cipher = Cipher.getInstance("AES/CBC/NoPadding");
//      }
//      catch (NoSuchAlgorithmException | NoSuchPaddingException e)
//      {
//          e.printStackTrace();
//      }
    }


    public void saveData(ArrayList<Account> dataToSave)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(Config.SERIAL_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(encrypt(dataToSave));
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public ArrayList<Account> loadData()
    {
        ArrayList<Account> loadedData = new ArrayList<Account>();
        File f = new File(Config.SERIAL_FILE);
        if (f.exists())
        {
            try
            {
                FileInputStream fileIn = new FileInputStream(Config.SERIAL_FILE);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                loadedData = (ArrayList<Account>) in.readObject();
                in.close();
                fileIn.close();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            loadedData = decrypt(loadedData);
        }
        else
        {
            loadedData = new ArrayList<Account>();
        }
        return loadedData;
    }


    private ArrayList<Account> encrypt(List<Account> decrypted)
    {
        ArrayList<Account> encrypted = new ArrayList<Account>();

        try
        {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            for (int i = 0; i < decrypted.size(); i++)
            {
                try
                {
                    byte[] login = cipher.doFinal(Base64.getDecoder().decode(decrypted.get(i).getLogin().getBytes()));
                    encrypted.add(new Account(login.toString(), "pass"));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        return encrypted;
    }


    private ArrayList<Account> decrypt(List<Account> encrypted)
    {
        ArrayList<Account> decrypted = new ArrayList<Account>();

        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            for (int i = 0; i < encrypted.size(); i++)
            {
                byte[] login = cipher.doFinal(Base64.getDecoder().decode(encrypted.get(i).getLogin()));
                decrypted.add(new Account(new String(login), "pass"));
            }
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        return decrypted;
    }


    public static Move getMove()
    {
        if (instance == null)
        {
            instance = new Move();
        }
        return instance;
    }
}
