   class MD5Hashing implements HashFunction{
        @Override
        public int getHash(String key) throws Exception{
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] byteArray = digest.digest(key.getBytes("UTF-8"));
                ByteBuffer buffer = ByteBuffer.wrap(byteArray);
                return buffer.getInt()& 0x7fffffff;
        }
    }
