public static String SHA256 (List<String> parametros, String clave) 

{
try {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    for(String param:parametros){
        byte p[] = new byte[param.length()];
        p = param.getBytes();
        sha.update(p);
    }
    byte bClave[] = new byte[clave.length()];
    bClave = clave.getBytes();
    byte[] hash = sha.digest(bClave);
    return ( hexString256 (hash));
   }catch (NoSuchAlgorithmException e){
   return ("Error");
  }
}
