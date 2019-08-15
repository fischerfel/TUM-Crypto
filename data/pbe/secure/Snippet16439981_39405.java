ublic void handle(Callback[] callbacks)   throws IOException,  UnsupportedCallbackException
  {

      for (int i = 0; i < callbacks.length; i++)
       {         


            WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
            try {
                pasandsalt = getdataforChecking();
          } catch (ClassNotFoundException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
          }

            try {
                passwordforchecking = hash(pwcb.getPassword(),Base64.decodeBase64(pasandsalt[1]));

            } catch (Exception e) {


                // TODO Auto-generated catch block
                e.printStackTrace();
            }



             if((pwcb.getIdentifier().equals("bob")) && (passwordforchecking.equals(pasandsalt[0])) )
             {
                 return;

             }
         }

   }

  private static String hash(String password, byte[] salt) throws Exception    
  { 
             SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
           KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
           return Base64.encodeBase64String(f.generateSecret(spec).getEncoded());

     }


  public static String[] getdataforChecking() throws ClassNotFoundException
  {

      String[] arr = new String [2];
      Connection conn = null;
      Class.forName("org.postgresql.Driver");
        try
        {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/plovdivbizloca",
                    "postgres", "tan");
        }

        catch (SQLException ex)
        {

            ex.printStackTrace();
        }


        Statement mystmt = null;
        String selectQuery = "select * from passwordforservice;";
        try
        {
            mystmt = conn.createStatement();
            ResultSet mysr = mystmt.executeQuery(selectQuery);
            while (mysr.next())
            {
                arr[0] = mysr.getString(1);
                arr[1]= mysr.getString(2);

            }

        }


        catch (Exception ex)
        {
            ex.printStackTrace();

        }
        return arr;



}

  }
