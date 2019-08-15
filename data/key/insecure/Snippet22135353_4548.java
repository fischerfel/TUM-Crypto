String sb="fffe7a50" //Example of DES key 
byte []b=sb.getBytes();  //string to bytes
SecretKey key2 = new SecretKeySpec(b, 0, b.length, "DES");
