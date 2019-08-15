try {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] output = digest.digest(password);

        digest.update(salt);
        digest.update(output);
        return new BigInteger(1, digest.digest());
    } catch (NoSuchAlgorithmException e) {
        throw new UnsupportedOperationException(e);
    }
