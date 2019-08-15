    public static Bitmap getBitmapFromURL(String src) {
        Bitmap myBitmap = null;
            try {

                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                //Decryption
                try {
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
                IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

                InputStream input = connection.getInputStream();
                CipherInputStream cis = new CipherInputStream(input, cipher);


                myBitmap = BitmapFactory.decodeStream(cis);

                }
                catch(Exception e){
                    e.fillInStackTrace();
                    Log.v("ERROR","Errorchence : "+e);
                }

                return myBitmap;


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
}
