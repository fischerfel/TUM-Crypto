{

 ......

 final File file = getCacheFile(imageUrl);

 file.getParentFile().mkdirs(); 

 file.createNewFile();

 ......

}

      public File getCacheFile(String url) 
         {
             // First compute the cache key and cache file path for this URL
             File cacheFile = null;
             try
             {
                 MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
                 mDigest.update(url.getBytes());
                 final String cacheKey = bytesToHexString(mDigest.digest());
                 if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
                 {
                     cacheFile = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "Android"
                            + File.separator + "data"
                            + File.separator + GiftSuggestions.this.getPackageName()
                            + File.separator + "cache"
                            + File.separator + "bitmap_" + cacheKey + ".tmp");              
                 }
             }
             catch (NoSuchAlgorithmException e) 
             {
                 // Oh well, SHA-1 not available (weird), don't cache bitmaps.
             }
             return cacheFile;
         }

         private String bytesToHexString(byte[] bytes) 
         {
             // http://stackoverflow.com/questions/332079
             StringBuffer sb = new StringBuffer();
             for (int i = 0; i < bytes.length; i++)
             {
                 String hex = Integer.toHexString(0xFF & bytes[i]);
                 if (hex.length() == 1) {
                     sb.append('0');
                 }
                 sb.append(hex);
             }
             return sb.toString();
         }
