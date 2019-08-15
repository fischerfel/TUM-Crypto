String        myInput = "test1234";
MessageDigest      md = MessageDigest.getInstance("SHA");
byte[]            myD = md.digest(myInput.getBytes());
BASE64Encoder    en64 = new BASE64Encoder();
String       myOutput = new String ( 
                            Java.net.URLEncoder.encode( en64.encode(myD)));
// myOutput becomes "F009U%2Bx99bVTGwS3cQdHf%2BJcpCo%3D"
