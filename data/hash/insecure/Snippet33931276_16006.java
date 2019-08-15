byte[] key = siteSecret.getBytes("UTF-8");
key = Arrays.copyOf(MessageDigest.getInstance("SHA").digest(key), 16);
