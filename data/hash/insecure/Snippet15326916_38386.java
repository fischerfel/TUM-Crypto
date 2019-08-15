try {
            MessageDigest mdg = MessageDigest.getInstance("MD5");      

            mdg.update(usernamepassword.get(0).getBytes(), 0, usernamepassword.get(0).length());
            mdg.update(usernamepassword.get(1).getBytes(), 1, usernamepassword.get(0).length());     


        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UPCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
