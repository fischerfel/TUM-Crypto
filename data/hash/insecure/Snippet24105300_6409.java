   String first_name = "Abdul";
   String last_name = "Ansari";
   String DOB = "29/09/1994";
   String fourth_element = ""; //Add something
   String data = first_name + last_name + DOB + fourth_element;
   data = data.replaceAll(" ","");
   data = data.toLowerCase();
   MessageDigest md = MessageDigest.getInstance("MD5");
   md.update(data.getBytes());
   byte[] digest = md.digest();
   // Then convert the bytes to hex and add them in database
