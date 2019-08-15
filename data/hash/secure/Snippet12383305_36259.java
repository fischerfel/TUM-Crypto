MessageDigest md = MessageDigest.getInstance("SHA-256");    
md.update(s1.getBytes());    
byte[] h = md.digest();    
Base32 base32 = new Base32();    
String base32_h = replaceIllegalCharacters(base32.encodeAsString(h));    
System.out.println("\n"+base32_h+"\n");    
String cc1 = base32_h.substring(0, 4);
