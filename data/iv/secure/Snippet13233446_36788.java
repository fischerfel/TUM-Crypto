    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String data = null;

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];

        if(data != null){
            File bufferFile = new File(data);
            Log.e("","data : "+data);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(bufferFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
            int userId = RPCCommunicator.getUserId(Store.this);
            String secretSalt = RPCCommunicator.getSecretKey(userId);
            String iv = RPCCommunicator.getIv(secretSalt);
            SecretKeySpec keySpec = new SecretKeySpec(secretSalt.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            try {
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            CipherInputStream cis = new CipherInputStream(fis, cipher);

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            //o2.inSampleSize=2;
            o2.inTempStorage = new byte[4*1024];

            int width = (int) RPCCommunicator.getPlaceHolderWidth(Store.this, 45);
            int height = (int) RPCCommunicator.getPlaceHolderHeight(Store.this, 25);

            Rect rect = new Rect();
            rect.inset(width, height);

            Bitmap finall = BitmapFactory.decodeStream(cis,rect,o2);
            if(finall != null){
                Bitmap bmp = Bitmap.createScaledBitmap(finall, width, height, true);
                return bmp;
            } else {
                return null;
            }
        }
        return null;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null) {
            final ImageView imageView = imageViewReference.get();
            if(bitmap != null){
                if (imageView != null) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                    imageView.startAnimation(animation);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                int width = (int) RPCCommunicator.getPlaceHolderWidth(Store.this, 45);
                int height = (int) RPCCommunicator.getPlaceHolderHeight(Store.this, 25);

                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width, height);
                param.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                param.rightMargin = 15;
                imageView.setBackgroundResource(R.drawable.placeholder);
                imageView.setLayoutParams(param);
            }
        }
    }
}
