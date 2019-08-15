MessageDigest cript2 = MessageDigest.getInstance("SHA-1");
        cript2.reset();
        cript2.update((nonce+created+hashedpw).getBytes("UTF-8"));
        PasswordDigest = new String(cript2.digest());
        PasswordDigest = PasswordDigest.getBytes("UTF-8").encodeBase64()
