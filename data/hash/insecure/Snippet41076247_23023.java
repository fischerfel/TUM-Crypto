    String check = info.mail;
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    String checkHash = Base64.encodeBase64String(md.digest(check.getBytes()));

    if(checkHash.equals(hash)){
        return ResponseEntity.ok("Password reset to: " + info.password);
    }else{
        return ResponseEntity.ok("Hash didn't equal to: " + checkHash);
    }
