public interface WithMD5Calculator{

    default String getMd5(){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            //... not important
        }catch(NoSuchAlgorithmException e){
           //... do some extra stuff and throw wrapped in ServiceException
        }
    }

    // rest of code not important
}
