 @Override
public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
        convertView = mInflater.inflate(R.layout.image_item, null);
    }

    try {
        File bufferFile = new File(ids.get(position));
        FileInputStream fis   = new FileInputStream(bufferFile);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("asfadasdasd".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("cdsdfngfgbxfvbd".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        Log.e("","HEAP SIZE "+Debug.getNativeHeapAllocatedSize());

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTempStorage = new byte[2*1024];
        o.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

        Bitmap ops = BitmapFactory.decodeStream(cis,null,o);

        ((ImageView) convertView.findViewById(R.id.imgView)).setImageBitmap(ops);
        ops = null;
        cis.close();
        fis.close();
        System.gc();

    } catch (Exception e) {
        e.printStackTrace();
        ((ImageView) convertView.findViewById(R.id.imgView)).setImageResource(R.drawable.image_unavailablee);
    }

    return convertView;
}
