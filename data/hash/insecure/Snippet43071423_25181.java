MessageDigest cript2 = MessageDigest.getInstance("SHA-1");
        cript2.update(nonce.getBytes("ASCII"));
        PasswordDigest = new String(cript2.digest());
