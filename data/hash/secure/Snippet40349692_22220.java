public String getPasswordDigest(String password) throws NoSuchAlgorithmException{

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte [] digestBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            String digestString = new String(digestBytes, StandardCharsets.UTF_8);

            return digestString;

        }catch(NoSuchAlgorithmException e){
            throw new  NoSuchAlgorithmException(e.getMessage());
        }
    }
}
