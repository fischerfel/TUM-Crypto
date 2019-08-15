    public byte[] rand(byte[] seed, int n) {
        try {
            byte[] data = null;
            ByteArrayOutputStream ret = new ByteArrayOutputStream(n);
            while (ret.size() < n) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(seed);
                if (data != null)
                    md.update(data);
                data = md.digest();
                ret.write(data, 0, Math.min(n - ret.size(), data.length));
            }
            return ret.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
