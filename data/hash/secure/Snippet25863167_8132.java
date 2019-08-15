        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            String newPass = digest.toString();
            System.out.println("**** " + newPass);
            return newPass;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Author.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
