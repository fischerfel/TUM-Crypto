package com.epe.smaniquines.ui;


public class DetailsActivity extends Activity implements OnClickListener {
    ImageView proImage, ivSave, ivInfo, ivPlay, ivBak, iv_share;
    RelativeLayout rl_botm, rl_option;
    TextView tv_facebuk, tv_twiter, tv_nothanks, tv_email, tv_save;
    String big_img;
    ArrayList<String> resultArray;
    private DisplayImageOptions options;
    public static ImageLoader imageLoader;
    RelativeLayout rl_info;
    public boolean flag = false;
    int i = 0;
    private int PicPosition;
    private Handler handler = new Handler();
    int mFlipping = 0;
    ViewFlipper viewFlipper;
    Timer timer;
    int flagD = 0;
    String data;
    String shareType;
    File image;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        initialize();
        showHashKey(this);


        printKeyHashForThisDevice();
        /*
         * Intent i = getIntent(); data = i.getStringExtra("data"); shareType =
         * i.getStringExtra("type");
         */

        big_img = getIntent().getStringExtra(Const.TAG_BIG_IMG);
        resultArray = getIntent().getStringArrayListExtra("array");
        System.out.println("::::::::::::::ArraySize::::::::" + resultArray);
        imageLoader.displayImage(big_img, proImage, options);
        ivInfo.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        tv_email.setOnClickListener(this);
        tv_facebuk.setOnClickListener(this);
        tv_nothanks.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_twiter.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        ivBak.setOnClickListener(this);
        // viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        imageLoader.displayImage(big_img, proImage, options);
        proImage.postDelayed(swapImage, 3000);

    }

    public void open(View view) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello test"); // <- String
        Uri screenshotUri = Uri.parse(image.getPath());
        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    void initialize() {
        proImage = (ImageView) findViewById(R.id.iv_det);
        ivInfo = (ImageView) findViewById(R.id.iv_info);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivBak = (ImageView) findViewById(R.id.iv_back);
        rl_botm = (RelativeLayout) findViewById(R.id.rl_bottom);
        rl_option = (RelativeLayout) findViewById(R.id.rl_options);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_facebuk = (TextView) findViewById(R.id.tv_facebook);
        tv_nothanks = (TextView) findViewById(R.id.tv_no_thanks);
        tv_twiter = (TextView) findViewById(R.id.tv_twiter);
        rl_option.setVisibility(View.GONE);

        iv_share = (ImageView) findViewById(R.id.iv_share);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(DetailsActivity.this));
        rl_info = (RelativeLayout) findViewById(R.id.rl_info);
        rl_info.setVisibility(View.GONE);
        resultArray = new ArrayList<String>();
        ivPlay.setVisibility(View.VISIBLE);

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_share:
            rl_option.setVisibility(View.VISIBLE);

            break;

        case R.id.iv_play:
            proImage.setVisibility(View.GONE);
            AdapterViewFlipper flipper = (AdapterViewFlipper) findViewById(R.id.flipper);
            flipper.setAdapter(new FlipperAdapter(DetailsActivity.this,
                    resultArray));
            ivPlay.setVisibility(View.GONE);
            break;
        case R.id.iv_back:
            finish();
            break;
        case R.id.tv_email:
            rl_option.setVisibility(View.GONE);
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL,
                    new String[] { "youremail@yahoo.com" });
            email.putExtra(Intent.EXTRA_SUBJECT, "subject");
            email.putExtra(Intent.EXTRA_TEXT, "message");
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email,
                    "Choose an Email client :"));
            break;
        case R.id.tv_save:
            save();
            rl_option.setVisibility(View.GONE);
            break;
        case R.id.tv_facebook:

            facebookShare();
            /*
             * save(); open(v);
             */
            rl_option.setVisibility(View.GONE);
            break;
        case R.id.tv_no_thanks:
            rl_option.setVisibility(View.GONE);
            break;
        case R.id.tv_twiter:
            rl_option.setVisibility(View.GONE);
            break;
        case R.id.iv_info:
            rl_option.setVisibility(View.GONE);
            if (flag) {
                rl_info.setVisibility(View.VISIBLE);
                flag = false;
            } else {
                rl_info.setVisibility(View.GONE);
                flag = true;
            }

            break;

        }
    }

    public static void showHashKey(Context context) {
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.epe.3SManiquines", PackageManager.GET_SIGNATURES); // Your
                                                                            // package
                                                                            // name
                                                                            // here
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    // slide show..!!!

    MediaPlayer introSound, bellSound;
    Runnable swapImage = new Runnable() {
        @Override
        public void run() {
            myslideshow();
            handler.postDelayed(this, 1000);
        }
    };

    private void myslideshow() {
        PicPosition = resultArray.indexOf(big_img);
        if (PicPosition >= resultArray.size())
            PicPosition = resultArray.indexOf(big_img); // stop
        else
            resultArray.get(PicPosition);// move to the next gallery element.
    }

    //

    // SAVE TO SD CARD..!!
    void save() {
        BitmapDrawable drawable = (BitmapDrawable) proImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File sdCardDirectory = Environment.getExternalStorageDirectory();

        File dir = new File(sdCardDirectory.getAbsolutePath()
                + "/3sManiquines/");
        image = new File(sdCardDirectory, "3s_" + System.currentTimeMillis()
                + ".png");
        dir.mkdirs();
        boolean success = false;

        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();

            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            addImageToGallery(dir + "", DetailsActivity.this);
            Toast.makeText(getApplicationContext(), "Image saved with success",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error during image saving", Toast.LENGTH_LONG).show();
        }
    }//

    public static void addImageToGallery(final String filePath,
            final Context context) {

        ContentValues values = new ContentValues();

        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,
                values);
    }

    // facebook...
    public void sharetext(String text) // Text to be shared
    {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);

        share.setType("text/plain");
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "TITLE");
        share.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(share, "Share via"));
        finish();

    }

    public void shareimage(String text) // argument is image file name with
                                        // extention
    {

        Intent shareimage = new Intent(android.content.Intent.ACTION_SEND);

        shareimage.setType("*/*");// for all
        shareimage.setClassName("com.android.mms",
                "com.android.mms.ui.ComposeMessageActivity");

        shareimage.putExtra(Intent.EXTRA_STREAM, resultArray.indexOf(big_img));
        startActivity(Intent.createChooser(shareimage, "Share Image"));
        finish();
    }

    private void printKeyHashForThisDevice() {
        try {
            System.out
                    .println("::::::::::::::::::::HAsh key called:::::::::::::");
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String keyHash = Base64.encodeToString(md.digest(),
                        Base64.DEFAULT);
                System.out
                        .println(":::::::::::KEy hash:::::::::::::" + keyHash);

                System.out.println("================KeyHash================ "
                        + keyHash);

            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    // facebook post image and text......!!
    private void facebookShare() {

        try {

            String aarti = "Hello friends this is demo for facebook.";

            FacebookShared fs = new FacebookShared(DetailsActivity.this, this);

            String vincentUrl = big_img;

            fs.setData(aarti, vincentUrl);

            fs.loginToFacebook(); // for logout dialog

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }
}
