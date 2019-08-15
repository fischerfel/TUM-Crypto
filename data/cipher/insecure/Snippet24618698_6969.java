public class MainActivity extends Activity {

static byte[] keyValue = {'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y'};

@Override
protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ImageView imgView = (ImageView) findViewById(R.id.imageView1);

    Bitmap picture = getBitmapFromAsset("result.jpg");
    Bitmap resultPic = picture.copy(Bitmap.Config.ARGB_8888, true);
    picture.recycle();

    int width = resultPic.getWidth();
    int height = resultPic.getHeight();

    int[] pixels = new int[width * height];

    resultPic.getPixels(pixels, 0, width, 0, 0, width, height);

    //--------- perform encryption

    byte[] content = intArrToByteArr(pixels);

    try {

        content = decrypt(content);

    } catch (Exception e) { }

    pixels = byteArrToIntArr(content);

    //--------------------------------------------

    resultPic.setPixels(pixels, 0, width, 0, 0, width, height);

    imgView.setImageBitmap(resultPic);

    File file = new File(Environment.getExternalStorageDirectory().toString() + "/result.jpg");

    try {

        FileOutputStream fOut = new FileOutputStream(file);
        resultPic.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.close();
    }
    catch (Exception e) { }
}

public static byte[] encrypt(byte[] Data) throws Exception {

    Key key = generateKey();
    Cipher c = Cipher.getInstance("RC4");
    c.init(Cipher.ENCRYPT_MODE, key);

    byte[] encVal = c.doFinal(Data);

    return encVal;
}

public static byte[] decrypt(byte[] encryptedData) throws Exception {

    Key key = generateKey();
    Cipher c = Cipher.getInstance("RC4");
    c.init(Cipher.DECRYPT_MODE, key);

    byte[] decValue = c.doFinal(encryptedData);

    return decValue;
}

private static Key generateKey() throws Exception {

    Key key = new SecretKeySpec(keyValue, "RC4");

    return key;
}

public static byte[] intArrToByteArr(int[] input){

    ByteBuffer byteBuffer = ByteBuffer.allocate(input.length * 4);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(input);

    byte[] array = byteBuffer.array();

    return array;
}

public static int[] byteArrToIntArr(byte[] input){

    IntBuffer intBuf = ByteBuffer.wrap(input).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
    int[] array = new int[intBuf.remaining()];
    intBuf.get(array);

    return array;
}

private Bitmap getBitmapFromAsset(String strName) {

    AssetManager assetManager = getAssets();
    InputStream istr = null;

    try {

        istr = assetManager.open(strName);
    } catch (IOException e) { }

    Bitmap bitmap = BitmapFactory.decodeStream(istr);

    return bitmap;
}
