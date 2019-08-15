if(userPath.equals("/createUser"))
      {

            String Type = request.getParameter("user_type");

            String Id = request.getParameter("amka");
            String Name = request.getParameter("name");
            String Surname = request.getParameter("surname");
            String Nickname = request.getParameter("nickname");
            String Pass = request.getParameter("password");
            String Email = request.getParameter("e-mail");
            String City = request.getParameter("city");
            String Street = request.getParameter("street");
            String Phone = request.getParameter("phone");
            String At = request.getParameter("at");
            String Spec = request.getParameter("spec");


            //byte[] b_pass = Pass.getBytes("UTF-8");                //Κρυπτογράφηση password
            String salt = null;
            String hash=null;

           if ("doctor".equals(Type))

           {

        //   ID = Integer.parseInt(Id);
               MessageDigest md;
    try {

            Random r = SecureRandom.getInstance("SHA1PRNG"); 
            salt=new BigInteger(130, r).toString(32);  
            md = MessageDigest.getInstance("SHA-256"); 
            md.update(salt.getBytes("UTF-8")); 
            byte[] digest = md.digest();
            salt=Base64.encodeBase64String(digest);

            md.update(Pass.concat(salt).getBytes("UTF-8")); 
            digest = md.digest();
            hash=Base64.encodeBase64String(digest); // μετατροπή σε Base64



            } 
              catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
            }

           currentUser.setPassword(hash);
           currentUser.setUsername(Nickname);
           currentUser.setSalt(salt);
           UsersFacade.create(currentUser);

           currentGroup.setUsername(currentUser);
           currentGroup.setGroupname(Type);
           GroupsFacade.create(currentGroup);

           /*
           currentDoc.setIddoctors(ID);
           currentDoc.setName(Name);
            currentDoc.setSurname(Surname);
            currentDoc.setNickname(Nickname);
            currentDoc.setEmail(Email);
            currentDoc.setCity(City);
            currentDoc.setStreet(Street);
            currentDoc.setPhone(Phone);
            currentDoc.setSpec(Spec);
            DoctorsFacade.create(currentDoc);
             */

          }
