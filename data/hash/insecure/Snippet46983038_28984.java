        MessageDigest dgst = MessageDigest.getInstance("sha-1");
        byte[] hash = dgst.digest("some long, complex and random password".getBytes());
        byte[] keyBytes = new byte[keySize/8];
        System.arraycopy(hash, 0, keyBytes, 0, keySize/8);
        SecretKey desSecret = new SecretKeySpec(keyBytes, "DESede");
