public class DBEncript {

    String mPassword = null;
    public final static int SALT_LEN = 8;
    byte [] mInitVec = null;
    byte [] mSalt = null;
    Cipher mEcipher = null;
    Cipher mDecipher = null;
    private final int KEYLEN_BITS = 128; // see notes below where this is used.
    private final int ITERATIONS = 65536;
    private final int MAX_FILE_BUF = 1024;

    public DBEncript (String password){
        mPassword = password;
    }

    public byte [] getSalt (){
        return (mSalt);
    }

    public byte [] getInitVec (){
        return (mInitVec);
    }

    private void Db (String msg){
        System.out.println ("** DBEncript ** " + msg);
    }

    /**
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidParameterSpecException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public void setupEncrypt () throws NoSuchAlgorithmException, 
                                                           InvalidKeySpecException, 
                                                           NoSuchPaddingException, 
                                                           InvalidParameterSpecException, 
                                                           IllegalBlockSizeException, 
                                                           BadPaddingException, 
                                                           UnsupportedEncodingException, 
                                                           InvalidKeyException {
        SecretKeyFactory factory = null;
        SecretKey tmp = null;
        mSalt = new byte [SALT_LEN];
        SecureRandom rnd = new SecureRandom ();
        rnd.nextBytes (mSalt);
        Db ("generated salt :" + Hex.encodeHex (mSalt));

        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec spec = new PBEKeySpec (mPassword.toCharArray (), mSalt, ITERATIONS, KEYLEN_BITS);
        tmp = factory.generateSecret (spec);
        SecretKey secret = new SecretKeySpec (tmp.getEncoded(), "AES");

        mEcipher = Cipher.getInstance ("AES/CBC/PKCS5Padding");
        mEcipher.init (Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = mEcipher.getParameters ();

        mInitVec = params.getParameterSpec (IvParameterSpec.class).getIV();

        Db ("mInitVec is :" + Hex.encodeHex (mInitVec));
    }

    public void setupDecrypt (String initvec, String salt) throws NoSuchAlgorithmException,InvalidKeySpecException,NoSuchPaddingException, 
                                                                                       InvalidKeyException,InvalidAlgorithmParameterException,DecoderException{
        SecretKeyFactory factory = null;
        SecretKey tmp = null;
        SecretKey secret = null;

        mSalt = Hex.decodeHex (salt.toCharArray ());
        Db ("got salt " + Hex.encodeHex (mSalt));

        mInitVec = Hex.decodeHex (initvec.toCharArray ());
        Db ("got initvector :" + Hex.encodeHex (mInitVec));


        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(mPassword.toCharArray (), mSalt, ITERATIONS, KEYLEN_BITS);

        tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        mDecipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(mInitVec));
    }


    public void WriteEncryptedFile (File input, File output) throws IOException, IllegalBlockSizeException,BadPaddingException{
        FileInputStream fin;
        FileOutputStream fout;
        long totalread = 0;
        int nread = 0;
        byte [] inbuf = new byte [MAX_FILE_BUF];

        fout = new FileOutputStream (output);
        fin = new FileInputStream (input);

        while ((nread = fin.read (inbuf)) > 0 )
        {
           // Db ("read " + nread + " bytes");
            totalread += nread;
            byte [] trimbuf = new byte [nread];
            for (int i = 0; i < nread; i++)
                trimbuf[i] = inbuf[i];

            byte [] tmp = mEcipher.update (trimbuf);

            if (tmp != null)
                fout.write (tmp);
        }

        byte [] finalbuf = mEcipher.doFinal ();
        if (finalbuf != null)
            fout.write (finalbuf);

        fout.flush();
        fin.close();
        fout.close();
        fout.close ();

        Db ("wrote " + totalread + " encrypted bytes");
    }



    public void ReadEncryptedFile (File input, File output) throws IllegalBlockSizeException,BadPaddingException, IOException{

        FileInputStream fin; 
        FileOutputStream fout;
        CipherInputStream cin;
        long totalread = 0;
        int nread = 0;
        byte [] inbuf = new byte [MAX_FILE_BUF];

        fout = new FileOutputStream (output);
        fin = new FileInputStream (input);

        cin = new CipherInputStream (fin, mDecipher);
        while ((nread = cin.read (inbuf)) > 0 )
        {
            //Db ("read " + nread + " bytes");
            totalread += nread;

            byte [] trimbuf = new byte [nread];
            for (int i = 0; i < nread; i++)
                trimbuf[i] = inbuf[i];

            fout.write (trimbuf);
        }

        fout.flush();
        cin.close();
        fin.close ();       
        fout.close();   

    }


    public void callRead(File input, File output) {
        String iv = null;
        String salt = null;
        DBEncript en = new DBEncript ("123");
        try{
            en.setupEncrypt ();
            iv = new String(Hex.encodeHex(en.getInitVec())).toUpperCase ();
            salt = new String(Hex.encodeHex(en.getSalt())).toUpperCase ();
          }catch (InvalidKeyException e){
            e.printStackTrace();
          }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
          }catch (InvalidKeySpecException e){
            e.printStackTrace();
          }catch (NoSuchPaddingException e){
            e.printStackTrace();
          }catch (InvalidParameterSpecException e){
            e.printStackTrace();
          }catch (IllegalBlockSizeException e){
            e.printStackTrace();
          }catch (BadPaddingException e){
            e.printStackTrace();
          }catch (UnsupportedEncodingException e){
            e.printStackTrace();
          }


          /*
           * decrypt file
           */
          DBEncript dc = new DBEncript ("123");
          try{
            dc.setupDecrypt (iv, salt);
          }catch (InvalidKeyException e){
            e.printStackTrace();
          }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
          }catch (InvalidKeySpecException e){
            e.printStackTrace();
          }catch (NoSuchPaddingException e){
            e.printStackTrace();
          }catch (InvalidAlgorithmParameterException e){
            e.printStackTrace();
          }catch (DecoderException e){
            e.printStackTrace();
          }


          try{
              dc.ReadEncryptedFile (input, output);
              System.out.println ("decryption finished to " + output.getName ());
            }catch (IllegalBlockSizeException e){
              e.printStackTrace();
            }catch (BadPaddingException e){
              e.printStackTrace();
            }catch (IOException e){
              e.printStackTrace();
            }
    }



}
