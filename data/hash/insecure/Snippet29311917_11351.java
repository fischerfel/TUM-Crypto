byte[] usernameBytes; //Set equal to perhaps UTF32 encoding of the string
byte[] realmBytes; //Set equal to perhaps UTF32 encoding of the string
byte[] passwordBytes; //Set equal to perhaps UTF32 encoding of the string

MessageDigest md = MessageDigest.getInstance("MD5");
md.update(usernameBytes); //Updates digest with these bytes
md.update(realmBytes);    //Updates digest with these bytes
md.update(passwordBytes); //Updates digest with these bytes

byte[] hashResult = md.digest(); //Outputs result

//Insert code to convert the byte array to an outputtable form (or perhaps you're writing to a binary file)
