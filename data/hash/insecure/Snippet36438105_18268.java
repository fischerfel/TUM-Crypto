private boolean ismd5HashValid(String md5hash, String path) {
        org.apache.hadoop.fs.Path pathDir = new org.apache.hadoop.fs.Path(path);
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        InputStream is = null;
        try {
            FileSystem fs = FileSystem.get(conf);
            MessageDigest md = MessageDigest.getInstance("MD5");
             is = fs.open(pathDir);
            byte[] bytes = new byte[1024];
            int numBytes;
            while ((numBytes = is.read(bytes)) != -1) {
                md.update(bytes, 0, numBytes);
            }
            byte[] digest = md.digest();
            String result = new String(Base64.encodeBase64(digest));
            Log.info("Source file md5hash {} Downloaded file md5hash {}", md5hash, result);         
            if (md5hash.equals(result)) {
                Log.info("md5hash check is valid");
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.warn(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            Log.warn(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    return false;
    }
