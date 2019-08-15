    MessageDigest md5 = MessageDigest.getInstance("MD5");                    
    byte[] hashedPassword = md5.digest(password.getBytes());
    return new String(hashedPassword); 
