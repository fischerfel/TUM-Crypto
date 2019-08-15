private Bitmap getBitmap(String src) {
    Bitmap myBitmap = null;
        try {

            //Decryption
            try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            AssetManager is = this.getAssets();        
            InputStream input = is.open(src); //open file in asset manager
            CipherInputStream cis = new CipherInputStream(input, cipher);

            myBitmap = BitmapFactory.decodeStream(cis);

            }
            catch(Exception e){
                e.printStackTrace();
                Log.v("ERROR","Error : "+e);
            }


            return myBitmap;


        } catch (IOException e) {
            e.printStackTrace();
            Log.v("ERROR","Error : "+e);

            return null;
        }
    }
