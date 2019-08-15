        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(password.getBytes("UTF-8"));
        byte[] digestBytes = messageDigest.digest();
