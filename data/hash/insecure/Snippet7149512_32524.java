public void register() throws Exception {

      UserController uControl = new UserController();

          Boolean registered = uControl.Register(this.email,this.passwd);  



/*             User usr = new User();

             usr.setEmail(this.email);

             MessageDigest digest = MessageDigest.getInstance("SHA-1");

             digest.reset();

             digest.update(pwdSalt);

             byte[] input = digest.digest(this.passwd. getBytes("UTF-8"));    

             BASE64Encoder encoder = new BASE64Encoder();

             usr.setPassword(encoder.encode(input));

             em.persist(usr);*/

       }
