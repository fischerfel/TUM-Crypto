public String encrypt(String password)
    {
        try
        {

            KeySpec ks=new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf=SecretKeyFactory.getInstance(algo);
            SecretKey key=skf.generateSecret(ks);
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest=md.digest();
            byte[] salt=Arrays.copyOf(digest, 16);
            AlgorithmParameterSpec aps=new PBEParameterSpec(salt, 20);
            Cipher cipher=Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, key, aps);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return password;
    }

    @Override
    public String decrypt(String password)
    {
        try
        {
            KeySpec ks=new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf=SecretKeyFactory.getInstance(algo);
            SecretKey key=skf.generateSecret(ks);
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest=md.digest();
            byte[] salt=Arrays.copyOf(digest, 16);
            AlgorithmParameterSpec aps=new PBEParameterSpec(salt, 20);
            Cipher cipher=Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, key, aps);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return password;
    }
