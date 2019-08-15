 public class Safety {
    public static Users encryptUser(Users user){
        Users usera=user;
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES/CBC/PKCS5Padding");
            Key key=kg.generateKey();
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String fNE=new String(cipher.doFinal(user.getFirstname().getBytes()),"UTF-8");
            String lNE=new String(cipher.doFinal(user.getLastname().getBytes()) , "UTF-8");
            String userNameE= new String(cipher.doFinal(user.getUsername().getBytes()),"UTF-8");
            String passWordE= new String(cipher.doFinal(user.getPassword().getBytes()),"UTF-8");
            String eME= new String(cipher.doFinal(user.getEmail().getBytes()),"UTF-8");
            String sQE= new String(cipher.doFinal(user.getsQ().getBytes()),"UTF-8");
            String sAE= new String(cipher.doFinal(user.getsA().getBytes()),"UTF-8");
            Users usere=new Users(fNE, lNE, userNameE, passWordE, eME, sQE, sAE, user.getUserID());
            return usere;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();                
        }

        return usera;
    }

    public static String decryptuser(Users user){
       //what should I do here exactly? 
    }
}
