public static String md5encryption(String text)
    {   String hashtext = null;
        try 
        {
            String plaintext = text;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
              hashtext = "0"+hashtext;   
            }
        } catch (Exception e1) 
        {
            // TODO: handle exception
            JOptionPane.showMessageDialog(null,e1.getClass().getName() + ": " + e1.getMessage());   
        }
        return hashtext;     
    }
