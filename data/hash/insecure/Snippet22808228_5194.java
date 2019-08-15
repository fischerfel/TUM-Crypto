import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.text.LoginFilter.UsernameFilterGeneric;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_SELECT_FILE = 2;
    private static final int REQUEST_PIC_CROP = 100;

    private static final String TAG = "RegisterActivity";

    private ImageView ivAddPhoto;
    private TextView tvAddPhoto;
    private EditText etUserName, etPassword, etEmail, etConfirmPassword;
    private Button btnSubscriptionButton, btnAddPhoto, mPolicyButton;
    private ImageView mUserNameStatusView, mEmailStatusView;
    public ProgressBar pbUserName, pbEmail;
    public ProgressDialog pd;
    private WebView webView;

    private static Uri mFileUri;
    private BackgroundTaskRegister mBackgroundTask = null;  

    private String mLogin;
    private String mPassword;
    private String mEmail;
    private File mPic;
    private String mConfirmPassword;

    //private List<String> mFacebookData = new ArrayList<String>();
    private String mFacebookData = "";
    private String selectedImagePath = "";
    private boolean mGallaryPhotoSelected = false;
    private Context mContext;
    private File file;

    public boolean cancel = false;
    public View focusView = null;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title

        // Handle action bar actions click
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;            
        case R.id.action_reset:
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = RegisterActivity.this;

        getActionBar().setDisplayHomeAsUpEnabled(true);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        pbUserName = (ProgressBar) findViewById(R.id.pbUserName);
        pbUserName.setVisibility(View.INVISIBLE);
        pbEmail = (ProgressBar) findViewById(R.id.pbEmail);
        pbEmail.setVisibility(View.INVISIBLE);
        mPolicyButton = (Button) findViewById(R.id.btnPolicy);

        ivAddPhoto = (ImageView) findViewById(R.id.ivAddPhoto);
        tvAddPhoto = (TextView) findViewById(R.id.tvAddPhoto);
        btnAddPhoto = (Button) findViewById(R.id.btnAddPhoto);
        mUserNameStatusView = (ImageView) findViewById(R.id.ivUserNameStatus);
        mUserNameStatusView.setVisibility(View.INVISIBLE);
        mEmailStatusView = (ImageView) findViewById(R.id.ivEmailStatus);
        mEmailStatusView.setVisibility(View.INVISIBLE);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the image capture Intent
                selectImage();
            }
        });

        if (savedInstanceState != null) {
            Log.i(TAG, "im in a savedInstanceState");
            mFileUri = savedInstanceState.getParcelable("fileUri");
        }

        btnSubscriptionButton = (Button) findViewById(R.id.btnSubscriptionButton);
        btnSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin = etUserName.getText().toString();
                mPassword = md5(md5(etPassword.getText().toString()));
                mEmail = etEmail.getText().toString();

                if (mLogin != null || mPassword != null || mEmail != null){
                    mPic = new File(selectedImagePath);
                    attemptRegistration();
                }
            }
        });


        etUserName.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mUserNameStatusView.setVisibility(View.INVISIBLE);
                if(!etUserName.hasFocus()) {
                    mLogin = etUserName.getText().toString();

                    // Check for a valid userName.
                    if (TextUtils.isEmpty(mLogin)) {
                        etUserName.setError(getString(R.string.error_field_required));
                        focusView = etUserName;
                        cancel = true;
                    } else if (mLogin.length() > 30) {
                        etUserName.setError(getString(R.string.error_invalid_username));
                        focusView = etUserName;
                        cancel = true;
                    } else if (!checkCharacters(mLogin)){
                        etUserName.setError(getString(R.string.error_character_not_allowed));
                        focusView = etUserName;
                        cancel = true;
                    } else {
                        cancel = false;
                        etUserName.setError(null);
                        pbUserName.setVisibility(View.VISIBLE);
                        checkValidityOfString(2);
                    }

                }
            }
        });

        mPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //metti il link della pagina che ti serve per la policy
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        etEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmailStatusView.setVisibility(View.INVISIBLE);
                if(!etEmail.hasFocus()) {
                    mEmail = etEmail.getText().toString();

                    // Check for a valid email address.
                    if (TextUtils.isEmpty(mEmail)) {
                        etEmail.setError(getString(R.string.error_field_required));
                        focusView = etEmail;
                        cancel = true;
                    } else if (!mEmail.contains("@")) {
                        etEmail.setError(getString(R.string.error_invalid_email));
                        focusView = etEmail;
                        cancel = true;
                    } else if (!checkCharacters(mEmail)){
                        etEmail.setError(getString(R.string.error_character_not_allowed));
                        focusView = etEmail;
                        cancel = true;
                    } else {
                        cancel = false;
                        etEmail.setError(null);
                        pbEmail.setVisibility(View.VISIBLE);
                        checkValidityOfString(3);
                    }
                }
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = { "Usa Camera", "Prendi da Galleria", "Chiudi" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aggiungi Foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Usa Camera")) {
                    // call android default camera

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);  

                    Log.i(TAG, "Usa Camera - mFileUri" + mFileUri.toString());

                    try {
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        // Do nothing for now
                        Log.e(TAG, "ActivityNotFoundException");
                    }

                } else if (items[item].equals("Prendi da Galleria")) {

                    Intent intent = new Intent();
                    // call android default gallery
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);                    
                    try {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_SELECT_FILE);
                    } catch (ActivityNotFoundException e) {
                        // Do nothing for now
                        Log.e(TAG, "ActivityNotFoundException");
                    }

                } else if (items[item].equals("Chiudi")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
        case REQUEST_CAMERA: 

            Log.i("mFileUri: ", mFileUri.getPath().toString());
            performCrop(mFileUri, mContext);
            //File file1 = new File(mFileUri.getPath());
            //Bitmap mBitmap = CameraHelper.decodeFile(file1);
            //ivAddPhoto.setImageBitmap(mBitmap);
            break;

        case REQUEST_SELECT_FILE:
            if (data != null){
                mFileUri = data.getData();
                Log.i("mFileUri: ", mFileUri.getPath().toString());
                performCrop(mFileUri, mContext);
                mGallaryPhotoSelected = true;
                tvAddPhoto.setText("Cambia");
            }
            break;

        case REQUEST_PIC_CROP:
            Bundle extras = data.getExtras();
            selectedImagePath = mFileUri.getPath();

            if (mGallaryPhotoSelected) {
                selectedImagePath = getRealPathFromURI(mFileUri);
                Log.i("TAG", "Absolute Path " + selectedImagePath);
                mGallaryPhotoSelected = true;
            }


            if (extras != null) {
                Log.i("TAG", "Inside Extra " + selectedImagePath);

                Bitmap photo = (Bitmap) extras.get("data");

                selectedImagePath = String.valueOf(System.currentTimeMillis()) + ".jpg";
                file = new File(Environment.getExternalStorageDirectory(),  selectedImagePath);

                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                        Toast.makeText(this,"Sorry, Camera Crashed-Please Report as Crash A.", Toast.LENGTH_LONG).show();
                    }   

                selectedImagePath = Environment.getExternalStorageDirectory() + "/" + selectedImagePath;
                Log.i("TAG", "After File Created  " + selectedImagePath);



                Bitmap bm;
                //bm = CameraHelper.decodeFile(file);
                bm = CameraHelper.decodeSampledBitmapFromUri(selectedImagePath, 150, 150);
                ivAddPhoto.setImageBitmap(bm);
            }
            break;
        }

    }



    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else { 
            cursor.moveToFirst(); 
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void performCrop(Uri picUri, Context context) {
        try {
            Log.i("crop", "performing crop");

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            //cropIntent.putExtra("outputX", 95);
            //cropIntent.putExtra("outputY", 95);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQUEST_PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation changes
        outState.putParcelable("file_uri", mFileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url   
        mFileUri = savedInstanceState.getParcelable("file_uri");
    }

    // Create a file Uri for saving an image or video 
    private static Uri getOutputMediaFileUri(int type) {
          return Uri.fromFile(CameraHelper.getOutputMediaFile(type));
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegistration() {
        if (mBackgroundTask != null) {
            return;
        }

        // Reset errors.
        etPassword.setError(null);
        etConfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        mPassword = etPassword.getText().toString();
        mConfirmPassword = etConfirmPassword.getText().toString();

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            etPassword.setError(getString(R.string.error_field_required));
            focusView = etPassword;
            cancel = true;
        } else if (mPassword.length() < 6 || mPassword.length() > 30) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        } else if (!checkCharacters(mPassword)){
            etPassword.setError(getString(R.string.error_character_not_allowed));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid confirmPassword.
        if (TextUtils.isEmpty(mConfirmPassword)) {
            etConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = etConfirmPassword;
            cancel = true;
        } else if (!mConfirmPassword.equals(mPassword)) {
            etConfirmPassword.setError(getString(R.string.error_invalid_confirmed_password));
            focusView = etConfirmPassword;
            cancel = true;
        } 

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            pd = new ProgressDialog(mContext);
            pd.setMessage("Registrazione in corso...");
            pd.show();
            mBackgroundTask = new BackgroundTaskRegister();
            //mBackgroundTask.execute((Void) null);
            mBackgroundTask.execute(1);
        }
    }

    private boolean checkCharacters(String s){
        UsernameFilterGeneric usernameFilterGeneric  = new UsernameFilterGeneric();
        for (char ch : s.toCharArray()) {
            if (!usernameFilterGeneric.isAllowed(ch)){
                //Log.i(TAG, "this characters is not allowed: " + Character.toString(ch));
                return false;
            }
        }
        return true;
    }

    //metodo che lancia la chiamata background per 
    //la verifica se esistono già il login e email
    private void checkValidityOfString(int i) {
        mBackgroundTask = new BackgroundTaskRegister();
        mBackgroundTask.execute(i);
    }


    //md5 for crypting and hash
    private static String md5(String data) {
        byte[] bdata = new byte[data.length()];
        int i;
        byte[] hash;

        for (i = 0; i < data.length(); i++)
            bdata[i] = (byte) (data.charAt(i) & 0xff);

        try {
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            hash = md5er.digest(bdata);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        StringBuffer r = new StringBuffer(32);
        for (i = 0; i < hash.length; i++) {
            String x = Integer.toHexString(hash[i] & 0xff);
            if (x.length() < 2)
                r.append("0");
            r.append(x);
        }
        return r.toString();
    }


    /**
     * Represents an asynchronous task used to upload 
     * information to the webserver and display the results
     */
    public class BackgroundTaskRegister extends AsyncTask<Integer, Void, Integer> {

        public int mOption;

        @Override
        protected Integer doInBackground(Integer... params) {
            // TODO: attempt authentication against a network service.

            Log.i(TAG, "Operation is: " + Integer.toString(params[0]));

            int responseStatus = 0;
            switch (params[0]) {
            case 1: responseStatus = MVPFunctions.getInstance().register(mLogin, mPassword, mEmail, mPic, "", mFacebookData);
                    mOption = 1;
                    break;
            case 2: responseStatus = MVPFunctions.getInstance().checklogin(mLogin);
                    mOption = 2;
                    break;
            case 3: responseStatus = MVPFunctions.getInstance().checkemail(mEmail);
                    mOption = 3;
                    break;
            }

            return responseStatus;
        }

        @Override
        protected void onPreExecute(){
            /*
             * This is executed on UI thread before doInBackground(). It is
             * the perfect place to show the progress dialog.
             */
        }

        @Override
        protected void onPostExecute(Integer result) {
            mBackgroundTask = null;

            switch (mOption) {
            case 1: if (result == 0) {
                        pd.cancel();
                        Toast.makeText(mContext, "Registrazione andata a buon fine!", Toast.LENGTH_SHORT).show();
                        lovertechSession.createLoginSession(mLogin, mPassword);
                        Intent intent = new Intent(getApplicationContext(), WallActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "username o password errati!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            case 2: pbUserName.setVisibility(View.INVISIBLE); 
                    if (result == 0) {
                        mUserNameStatusView.setVisibility(View.VISIBLE);
                    } else if (result == 101) {
                        mUserNameStatusView.setVisibility(View.INVISIBLE);
                        etUserName.setError(getString(R.string.error_username_not_available));
                        focusView = etUserName;
                        cancel = true;
                    }
                    break;
            case 3: pbEmail.setVisibility(View.INVISIBLE);
                    if (result == 0) {
                        mEmailStatusView.setVisibility(View.VISIBLE);
                    } else if (result == 117) {
                        mEmailStatusView.setVisibility(View.INVISIBLE);
                        etEmail.setError(getString(R.string.error_email_already_used));
                        focusView = etEmail;
                        cancel = true;
                    } else if (result == 129) {
                        mEmailStatusView.setVisibility(View.INVISIBLE);
                        etEmail.setError(getString(R.string.error_email_uncorrect_format));
                        focusView = etEmail;
                        cancel = true;
                    }
                    break;
            }   
        }
    }
}
