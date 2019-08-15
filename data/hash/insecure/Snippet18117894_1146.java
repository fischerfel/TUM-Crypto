    MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
    md.update(fileToSign.getBytes("UTF-8"));
    hash = md.digest();        
