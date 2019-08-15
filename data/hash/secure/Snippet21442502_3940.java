MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest("This is string".getBytes("UTF-8"));
System.out.println(DatatypeConverter.printBase64Binary(hash));
