        }catch (IllegalArgumentException e){

        }

        //bitmap.recycle();
    } else {
        try {
            getFile(key, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public static String hashKeyForDisk(String key) {
    String cacheKey;
    try {
        final MessageDigest mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(key.getBytes());
        cacheKey = bytesToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
        cacheKey = String.valueOf(key.hashCode());
    }
    return cacheKey;
}

private static String bytesToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
        String hex = Integer.toHexString(0xFF & bytes[i]);
        if (hex.length() == 1) {
            sb.append('0');
        }
        sb.append(hex);
    }
    return sb.toString();
}


static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    Bitmap _bitmap;
    Context _context;
    public BitmapWorkerTask (Bitmap bitmap,Context context) {
        _context = context;
        _bitmap = bitmap;
    }
    @Override
    protected Bitmap doInBackground(String... params) {

        addBitmapToMemoryCache(params[0], _bitmap);
        return _bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        try{
            loadBitmap(getImg(POSITION), _context);
        }catch (OutOfMemoryError oom){

        }

    }
}
