...

// ***** ADDITION *****
private AssetManager mAssetManager;

public ImageLoader(Context context){
    //Make the background thead low priority. This way it will not affect the UI performance
    photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
    // ***** ADDITION *****
    mAssetManager = context.getAssets();

    //Find the dir to save cached images
    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
    else
        cacheDir=context.getCacheDir();
    if(!cacheDir.exists())
        cacheDir.mkdirs();
}

private Bitmap getBitmap(String src) {
    Bitmap myBitmap = null;
        //Decryption
        try {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // ***** CHANGE *****
        InputStream input = mAssetManager.open(src); //open file in asset manager
        CipherInputStream cis = new CipherInputStream(input, cipher);

        myBitmap = BitmapFactory.decodeStream(cis);

        }
        catch(Exception e){
            e.printStackTrace();
            Log.v("ERROR","Error : "+e);
        }


        return myBitmap;
    }
....
