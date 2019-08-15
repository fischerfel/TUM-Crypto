private String getSHA(String str) {    
 MessageDigest md;    
 String out = "";    
 try {    
 md = MessageDigest.getInstance("SHA-512");    
 md.update(str.getBytes());    
 byte[] mb = md.digest();        
 for (int i = 0; i < mb.length; i++) {
 byte temp = mb[i];    
 String s = Integer.toHexString(new Byte(temp));    
 while (s.length() < 2) 
{    
 s = "0" + s;    
 }    
 s = s.substring(s.length() - 2);    
 out += s;
 }    
 } 
catch (NoSuchAlgorithmException e)
 {    
 e.printStackTrace();    
 }
 return out;    
}
