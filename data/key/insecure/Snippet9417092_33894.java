  public void decryptFile(){

    String inFile = "sample.enc";
    String outFile = "sample.jpg";
    String dir = Environment.getExternalStorageDirectory() +"/Android/data/HOT/";
    InputStream is ;
    byte[] iv = new byte[16];
    try {
        is = new FileInputStream(dir+inFile);

        is.read(iv);

    } catch (FileNotFoundException e1) {
        // TODO Auto-generated catch block
        Log.d("D1","no file found");
    } catch (IOException e) {
        // TODO Auto-generated catch block
        Log.d("D-2","no file found");
        e.printStackTrace();
    }

    byte[] k = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    Key key = new SecretKeySpec(k,"AES");




    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(iv));
        OutputStream outs = new FileOutputStream(dir+outFile);
        is = new FileInputStream(dir+inFile);
        while(true){
            byte[] chunk = new byte[64*1024];
            is.read(chunk);
            if(chunk.length == 0){

                break;

            }
            outs.write(cipher.doFinal(chunk));              
        }


    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        Log.d("D","1");

        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        Log.d("D","2");
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        Log.d("D","3");
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        Log.d("D","4");
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        Log.d("D","5");
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        Log.d("D","6");
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        Log.d("D","7");
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        Log.d("D","8");
        e.printStackTrace();
    }

    ImageView im = (ImageView)findViewById(R.id.imageView2);

    Bitmap mainBitmap = BitmapFactory.decodeFile(dir+outFile);

    im.setImageBitmap(mainBitmap);

}
