public UserDetails createNewUser(String username,String passwd,Set<Group> groups){
       UserDetails u=new UserDetails();
       u.setname(username);
       u.setGroups(groups);
       u.setPassword(createHash(passwd));
       return u;
}
public String createHash(String data){
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes());
        byte byteData[] = digest.digest();
        //convert bytes to hex chars
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
}
