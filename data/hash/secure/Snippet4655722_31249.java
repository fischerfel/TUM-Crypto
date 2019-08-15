public Login authenticate(Login login) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String password = login.getPassword();
            try {
                md.update(password.getBytes("UTF-16"));
                byte[] digest = md.digest();
                String query = "SELECT L FROM Login AS L WHERE L.email=? AND L.password=?";
                Object[] parameters = { login.getEmail(), digest };
                List<Login> resultsList = (getHibernateTemplate().find(query,parameters));
                 if (resultsList.isEmpty()) {
                         //error dude
                     }
                 else if (resultsList.size() > 1) {
                         //throw expections
                     }
                 else {
                       Login login1 = (Login) resultsList.get(0);
                       return login1;
                 }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }                
       return null;  
    }
