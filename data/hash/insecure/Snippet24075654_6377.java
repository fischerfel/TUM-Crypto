package com.epe.smaniquines.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.epe.smaniquines.R;
import com.epe.smaniquines.adapter.SimpleGestureFilter;
import com.epe.smaniquines.backend.AlertDialogManager;
import com.epe.smaniquines.backend.ConnectionDetector;
import com.epe.smaniquines.uc.Menu;
import com.epe.smaniquines.util.Const;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DetailsActivity extends Activity implements OnClickListener,
        OnTouchListener {
    private SimpleGestureFilter detector;

    ImageView proImage, ivSave, ivInfo, ivPlay, ivBak, iv_share;
    RelativeLayout rl_botm, rl_option;
    TextView tv_facebuk, tv_twiter, tv_nothanks, tv_email, tv_save, tv_quote;
    String big_img;
    int pos;
    ImagePagerAdapter adapter;
    ViewPager viewPager;
    private static SharedPreferences mSharedPreferences;
    ArrayList<String> resultArray;
    private DisplayImageOptions options;
    public static ImageLoader imageLoader;
    RelativeLayout rl_info;
    public boolean flag = false;
    Menu menu;
    public boolean flag1 = false;
    boolean isOnClick = false;
    int i = 0;
    String cat_nem;

    File casted_image;
    private int PicPosition;
    private Handler handler = new Handler();
    ProgressDialog pDialog;
    ConnectionDetector cd;
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    int mFlipping = 0;
    ViewFlipper viewFlipper;
    Timer timer;
    int flagD = 0;
    String data;
    String shareType;
    File image;
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    TextView titledetail;
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    public static String PACKAGE_NAME;
    static String TWITTER_CONSUMER_KEY = "AzFSBq1Od4lYGGcGR0u9GkMIT"; // place

    static String TWITTER_CONSUMER_SECRET = "MBwdard2Y4l6LT6z219NJ6x8aZ4jyK8JBKZ85usRPcDP8ujwM0"; // place
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming

    PointF start = new PointF();

    PointF mid = new PointF();

    float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_detail);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        initialize();
        cat_nem = getIntent().getStringExtra("cat_name");
        System.out.println(":::::::::::CAt nem:::::::::" + cat_nem);
        titledetail.setText(cat_nem);
        proImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        proImage.setOnTouchListener(this);
        showHashKey(this);
        printKeyHashForThisDevice();
        pos = getIntent().getIntExtra("pos", 0);
        System.out.println(":::::::::::::::current pos::::::::::" + pos);
        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(DetailsActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if twitter keys are set
        if (TWITTER_CONSUMER_KEY.trim().length() == 0
                || TWITTER_CONSUMER_SECRET.trim().length() == 0) {
            // Internet Connection is not present
            alert.showAlertDialog(DetailsActivity.this, "Twitter oAuth tokens",
                    "Please set your twitter oauth tokens first!", false);
            // stop executing code by return
            return;
        }
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
        /*
         * Intent i = getIntent(); data = i.getStringExtra("data"); shareType =
         * i.getStringExtra("type");
         */

        big_img = getIntent().getStringExtra(Const.TAG_BIG_IMG);
        // imageLoader.displayImage(big_img, proImage, options);

        resultArray = getIntent().getStringArrayListExtra("array");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setVisibility(View.VISIBLE);
        adapter = new ImagePagerAdapter();
        viewPager.setCurrentItem(resultArray.indexOf(pos));
        viewPager.setAdapter(adapter);

        ivInfo.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        tv_email.setOnClickListener(this);
        tv_facebuk.setOnClickListener(this);
        tv_nothanks.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_twiter.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        ivBak.setOnClickListener(this);
        tv_quote.setOnClickListener(this);
        proImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setVisibility(View.VISIBLE);
                Animation anim;
                anim = (Animation) getResources().getAnimation(
                        R.anim.animated_activity_slide_right_in);

                viewPager.setAnimation(anim);
            }
        });
        // viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        /*
         * imageLoader.displayImage(big_img, proImage, options);
         * proImage.postDelayed(swapImage, 1000);
         */
        viewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isOnClick) {
                        if (event.getX() > viewPager.getWidth() / 2) {
                            // go to next

                            viewPager.setCurrentItem(resultArray.indexOf(i),
                                    true);

                            i++;
                        } else {
                            viewPager.setCurrentItem(resultArray.indexOf(i),
                                    true);
                            i--;
                            // go to previous
                        }
                        return true;
                    }
                }
                return false;
            }
        });

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
        tv_quote = (TextView) findViewById(R.id.tv_qote);

        iv_share = (ImageView) findViewById(R.id.iv_share);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(DetailsActivity.this));
        rl_info = (RelativeLayout) findViewById(R.id.rl_info);
        rl_info.setVisibility(View.GONE);
        resultArray = new ArrayList<String>();
        ivPlay.setVisibility(View.VISIBLE);
        titledetail = (TextView) findViewById(R.id.titledetail);

        // twitter

        // Login button

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_share:
            rl_option.setVisibility(View.VISIBLE);
            rl_info.setVisibility(View.GONE);
            if (flag1) {
                rl_option.setVisibility(View.VISIBLE);
                flag1 = false;
            } else {
                rl_option.setVisibility(View.GONE);
                flag1 = true;
            }

            break;

        case R.id.iv_play:
            /*
             * proImage.setVisibility(View.GONE);
             * 
             * AdapterViewFlipper flipper = (AdapterViewFlipper)
             * findViewById(R.id.flipper); flipper.setAutoStart(true);
             * flipper.setAdapter(new FlipperAdapter(DetailsActivity.this,
             * resultArray));
             */
            i = 0;
            final Handler handler = new Handler();

            final Runnable ViewPagerVisibleScroll = new Runnable() {
                @Override
                public void run() {
                    if (i <= adapter.getCount() - 1) {
                        viewPager.setCurrentItem(i, true);
                        handler.postDelayed(this, 2000);
                        i++;
                    }
                }
            };
            new Thread(ViewPagerVisibleScroll).start();

            ivPlay.setVisibility(View.INVISIBLE);
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

            // facebookShare();
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

            // loginToTwitter();
            // new updateTwitterStatus().execute("3sManiquines");

            break;
        case R.id.iv_info:
            rl_info.setVisibility(View.VISIBLE);
            rl_option.setVisibility(View.GONE);
            if (flag) {
                rl_info.setVisibility(View.VISIBLE);
                flag = false;
            } else {
                rl_info.setVisibility(View.GONE);
                flag = true;
            }

            break;
        case R.id.tv_qote:
            rl_option.setVisibility(View.GONE);

            String url = big_img;

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("Hi Go to product for details:");
            int start = builder.length();
            builder.append(url);
            int end = builder.length();

            builder.setSpan(new URLSpan(url), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
            i.putExtra(Intent.EXTRA_SUBJECT, "Quote");
            i.putExtra(Intent.EXTRA_TEXT, builder);
            startActivity(Intent.createChooser(i, "Select application"));
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchPointX = event.getX();
        float touchPointY = event.getY();
        int[] coordinates = new int[2];
        rl_info.getLocationOnScreen(coordinates);
        rl_option.getLocationOnScreen(coordinates);
        if (touchPointX < coordinates[0]
                || touchPointX > coordinates[0] + rl_info.getWidth()
                || touchPointY < coordinates[1]
                || touchPointY > coordinates[1] + rl_info.getHeight())
            rl_info.setVisibility(View.INVISIBLE);
        rl_option.setVisibility(View.INVISIBLE);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

        case MotionEvent.ACTION_DOWN: // first finger down only

            break;

        case MotionEvent.ACTION_UP: // first finger lifted

            /*
             * if (i < resultArray.size()) {
             * imageLoader.displayImage(resultArray.get(i), proImage); i++; }
             */
        case MotionEvent.ACTION_POINTER_UP: // second finger lifted

            break;

        case MotionEvent.ACTION_POINTER_DOWN: // second finger down

            break;

        case MotionEvent.ACTION_MOVE:

            break;

        }
        return true;

    }

    //
    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return resultArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = DetailsActivity.this;
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageLoader.displayImage(resultArray.get(position), imageView);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

}
