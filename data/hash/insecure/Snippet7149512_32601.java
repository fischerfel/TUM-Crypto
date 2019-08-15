public Boolean Register(String _userName,String _passwd) throws NoSuchAlgorithmException, UnsupportedEncodingException

    {

         User usr = new User();

         usr.setEmail(_userName);

         MessageDigest digest = MessageDigest.getInstance("SHA-1");

         digest.reset();

         digest.update(pwdSalt);

         byte[] input = digest.digest(_passwd.getBytes("UTF-8"));    

         BASE64Encoder encoder = new BASE64Encoder();

         usr.setPassword(encoder.encode(input));

         em.persist(usr);



        return true;

    }
