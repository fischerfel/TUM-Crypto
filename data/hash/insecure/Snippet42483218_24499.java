MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();
        cript.update(userPass.getBytes("UTF-8"));
        hashedpw = new String(cript.digest());
