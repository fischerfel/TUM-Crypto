 public static void encrypt(InputStream is, OutputStream os, List<Integer> userids) throws Exception//
    {
        byte[] key = MulticastCryptography.createKey();
        List<Object[]> list = new ArrayList<>();
        for (Integer userid : userids)
        {
            list.add(new Object[]
            {
                 userid, DatabaseManager.getUserkey(userid)
            });
        }
        encrypt(is, os, list, key);
    }

public static void encrypt(InputStream is, OutputStream os, List<Object[]> list, byte[] key) throws Exception
{
     DataOutputStream dos = new DataOutputStream(os);
     byte[] header = generateHeader(list, key);

     dos.writeInt(header.length);
     dos.write(header);

     byte[]headerhash=getHash(header);
     byte []signature=encrypt(headerhash, key);
     dos.writeInt(signature.length);
     dos.write(signature);

     SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
     Cipher cipher = Cipher.getInstance("AES");
     cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

     CipherOutputStream cos = new CipherOutputStream(dos, cipher);
     byte[] buffer = new byte[10240000];
     int n;
     while ((n = is.read(buffer)) != -1)
     {
         cos.write(buffer, 0, n);
     }
     cos.flush();
     cos.close();
}


 public static byte[] encrypt(byte[] data, byte[] key) throws Exception
    {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(data);
    }

public static byte[] generateHeader(List<Object[]> list, byte[] key) throws Exception //userid, userkey
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(list.size());

        for (Object[] entry : list)
        {
            Integer userid = (Integer) entry[0];
            byte[] userkey = (byte[]) entry[1];
            byte[] ekey = encrypt(key, userkey);

            dos.writeInt(userid);
            dos.writeInt(ekey.length);
            dos.write(ekey);
        }

        dos.flush();
        return baos.toByteArray();
    }

public static byte [] getHash(byte []data)
    {
        try
        {
            MessageDigest sha256=MessageDigest.getInstance("SHA-256");
            return sha256.digest(data);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(MulticastCryptography.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }
