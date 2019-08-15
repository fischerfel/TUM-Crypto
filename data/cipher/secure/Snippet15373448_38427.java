public void encrypt(String original)
{
    SecureRandom sr = new SecureRandom();

    byte [] key = new byte[16];
    byte [] iv = new byte[16];

    sr.nextBytes(key);
    sr.nextBytes(iv);

    Cipher cipher;

    try 
    {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec IV=new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key,"AES"), IV);
        byte[] utf8 = original.getBytes("UTF-8");
        byte []encryptedAES = cipher.doFinal(utf8);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(128);//128 bits
        KeyPair kp = kpg.genKeyPair();

        Cipher publicKeyCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        publicKeyCipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
        byte [] encryptedRSA = publicKeyCipher.doFinal(encryptedAES); //error here

        int length1=encryptedAES.length;
        int length2=IV.getIV().length;
        int length3=encryptedRSA.length;
        int length=length1+length2+length3;
        byte [] result= new byte[length];

        int l=0,m=0;

        for (int i=0; i<length; i++)
        {
            if(i<length1)
            {
                result[i] = encryptedAES[i];
            }
            else if(i>=length1 && i<length2)
            {
                result[i] = IV.getIV()[l];
                l++;
            }
            else if(i>=length2)
            {
                result[i] = encryptedRSA[m];
                m++;
            }
        }
        Log.i("Encrypted", "done");
        this.encryptedMessage=Base64.encodeToString(result, false);
        Log.i("Encrypted Message:", this.encryptedMessage);

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
