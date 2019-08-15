public class FBCheckInActivity extends Activity{

private CallbackManager callbackManager;
private LoginManager loginManager;
private File mFileTemp;
public static final int REQUEST_CODE_TAKE_PICTURE = 2;
public String path;
Bitmap sharingPhoto;
String caption="";
ImageView chooseImage;
EditText captionText;
Button postFacebook;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.facebook_sharing);
    printHashKey();
    sharingPhoto= BitmapFactory.decodeResource(this.getResources(), R.drawable.no_image);
    FacebookSdk.sdkInitialize(getApplicationContext());
    fbSharing(sharingPhoto);

}

private void fbSharing(Bitmap sharingPhoto) {
    chooseImage=(ImageView) findViewById(R.id.share_image);
    chooseImage.setImageBitmap(sharingPhoto);
    captionText=(EditText) findViewById(R.id.caption_text);
    postFacebook=(Button) findViewById(R.id.buttonPost);
    postFacebook.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginFaceBook();
        }


    });
    chooseImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openCamera();
        }
    });
    captionText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            caption = captionText.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });
}

@Override
protected void onResume() {
    super.onResume();
}

protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    if(requestCode==REQUEST_CODE_TAKE_PICTURE){
        Log.d("zambo","after selecting pic");
        path = mFileTemp.getPath();
        Log.d("zambo", path);
        sharingPhoto = BitmapFactory.decodeFile(path);
        chooseImage.setImageBitmap(sharingPhoto);
        fbSharing(sharingPhoto);
    }
    else {
        Log.d("zambo","callback manager for facebook sharing");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
public void printHashKey(){
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.urbanwand",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}

public void  loginFaceBook(){
    callbackManager = CallbackManager.Factory.create();
    List<String> permissionNeeds = Arrays.asList("publish_actions");
    loginManager = LoginManager.getInstance();

    loginManager.logInWithPublishPermissions(FBCheckInActivity.this, permissionNeeds);
    loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            graphApi(accessToken);
        }

        @Override
        public void onCancel() {
            Toast.makeText(FBCheckInActivity.this, "cancel", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(FBCheckInActivity.this, "error", Toast.LENGTH_SHORT).show();

        }
    });
}
public void openCamera(){
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    try {
        Uri mImageCaptureUri = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment
                    .getExternalStorageDirectory(), "IMG_"
                    + ".jpg");
            mImageCaptureUri = Uri.fromFile(mFileTemp);
        } else {
                    /*
                     * The solution is taken from here:
                     * http://stackoverflow.com/questions
                     * /10042695/how-to-get-camera-result-as-a-uri-in-data-folder
                     */
            mFileTemp = new File(getFilesDir(), "IMG_"
                    + ".jpg");
            mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    } catch (ActivityNotFoundException e) {
        e.printStackTrace();
        //Log.d("User_Photo", "cannot take picture", e);
    }
}
public void graphApi(AccessToken accessToken){
    GraphRequest request = GraphRequest.newUploadPhotoRequest(accessToken, "me/photos", sharingPhoto, caption, null, new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            Log.d("zambo","On Completed Callback");
            AlertDialog.Builder completeDialog = new AlertDialog.Builder(FBCheckInActivity.this);
            completeDialog.setMessage("Thank you for sharing your happiness!!");
            completeDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(FBCheckInActivity.this, DinerHotSellers.class));
                    overridePendingTransition(0, 0);
                }
            });
            completeDialog.create();
            completeDialog.show();
        }
    });
    request.executeAsync();
}
