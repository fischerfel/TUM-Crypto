 @Override
public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
        convertView = mInflater.inflate(R.layout.image_item, null);
    }

    try {
        File bufferFile = new File(ids.get(position));
        FileInputStream fis   = new FileInputStream(bufferFile);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        BitmapFactory.Options o = new BitmapFactory.Options();
        final int REQUIRED_SIZE=300*1024;

        //Find the correct scale value. It should be the power of 2.
        int width_tmp= o.outWidth, height_tmp= o.outHeight;
        int scale=1;
        while(true){
            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                break;
            width_tmp/=2;
            height_tmp/=2;
            scale*=2;
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;

        Bitmap ops = BitmapFactory.decodeStream(cis,null,o2);
        ((ImageView) convertView.findViewById(R.id.imgView)).setImageBitmap(ops);
        cis.close();
        fis.close();

        System.gc();

    } catch (Exception e) {
        e.printStackTrace();
        ((ImageView) convertView.findViewById(R.id.imgView)).setImageResource(R.drawable.image_unavailablee);
    }

    return convertView;
}
