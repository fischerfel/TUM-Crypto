import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

String passwordToHash = vars.get("password");
String   salt= vars.get("salt");
String generatedPassword = null;
    try {
     MessageDigest md = MessageDigest.getInstance("SHA-512");
     md.update(salt.getBytes("UTF-8"));
     byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
     StringBuilder sb = new StringBuilder();
     for(int i=0; i< bytes.length ;i++){
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
     }
     generatedPassword = sb.toString();
     log.info(generatedPassword);
     vars.put("generatedPassword", generatedPassword);
    } 
   catch (NoSuchAlgorithmException e){
    e.printStackTrace();
   }
