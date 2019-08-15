public class MainActivity extends AppCompatActivity {

    int user_id;
    CallbackManager callbackManager;

    SharedPreferences preferences;
    int my_user_id;
    int my_age;
    String my_f_id;
    String my_name;
    String my_location;

    private Camera mCamera;
    private CameraPreview mPreview;

    boolean previewStarted = false;

    // camera2 stuff

    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener mTextureViewListner;

    private CameraDevice mCamDevice;
    private CameraDevice.StateCallback mCamDeviceSCallback;

    private String mCamId;
    private static final int requestCamPermisionResult = 0;
    private Size mPreviewSize;

    private CaptureRequest.Builder mCapturRBuilder;

    private HandlerThread bHandlerThread;
    private Handler bHandler;
    RelativeLayout siz;

    private static SparseIntArray orientation = new SparseIntArray();
    static {
        orientation.append(Surface.ROTATION_0, 0);
        orientation.append(Surface.ROTATION_90, 90);
        orientation.append(Surface.ROTATION_180, 180);
        orientation.append(Surface.ROTATION_270, 270);
    }

    private boolean versionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("ReelyChat", this.MODE_PRIVATE);

        Intent intent2 = getIntent();
        if(intent2.hasExtra("go")){

        }
        else{
            if (preferences.contains("f_id")) {

                my_f_id = preferences.getString("f_id", null);
                my_user_id = preferences.getInt("user_id", 0);
                my_age = preferences.getInt("age", 0);
                my_name = preferences.getString("name", null);
                my_location = preferences.getString("location", null);

                if (my_f_id == null){
                    facebookInit();
                    setContentView(R.layout.activity_main);
                    siz = (RelativeLayout)findViewById(R.id.siz);
                    getCam();


                    facebookCall();
                    // notifyMessage();
                } else {
                    Intent intent;
                    intent = new Intent(MainActivity.this, Results.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                facebookInit();
                setContentView(R.layout.activity_main);
                siz = (RelativeLayout)findViewById(R.id.siz);
                getCam();

                facebookCall();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //        profileTracker.stopTracking();
    }

    private void facebookInit(){
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    private void facebookCall(){
        if(AccessToken.getCurrentAccessToken() != null){
            getFacebookInfo(AccessToken.getCurrentAccessToken());
        }

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_location", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AccessToken.getCurrentAccessToken() != null) {
                    getFacebookInfo(loginResult.getAccessToken());
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });
    }

    private void getFacebookInfo(final AccessToken accessToken){

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    Intent intent;
                    intent = new Intent(MainActivity.this, Login.class);
                    Bundle b = new Bundle();

                    System.out.println(accessToken.getUserId());
                    System.out.println(object.getString("first_name"));
                    // System.out.println(object.getString("birthday"));
                    System.out.println(object.getString("gender"));

                    b.putString("user_id", accessToken.getUserId());
                    b.putString("name", object.getString("first_name"));
                    if(object.has("birthday")){
                        b.putString("dob", object.getString("birthday"));
                    }else{
                        b.putString("dob", "");
                    }
                    b.putString("dob", object.getString("birthday"));
                    b.putString("gender", object.getString("gender"));
                    if(object.has("location")){
                        b.putString("location", object.getJSONObject("location").getString("name"));
                    }else{
                        b.putString("location", "");
                    }


                    b.putString("verified", object.getString("verified"));
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, birthday, gender, email, location, verified");
        request.setParameters(parameters);
        request.executeAsync();
    }


    /** A basic Camera preview class */
    private void getCam(){
        if(versionCheck()){
            mTextureView = (TextureView)findViewById(R.id.textureView2);
            mTextureView.setVisibility(View.VISIBLE);
            callToStart();
        }else{
            mCamera = getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
    }

    // Camera methods
    public static Camera getCameraInstance(){

        int cameraCount = 0;

        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                }
            }
        }
        return cam;
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
            catch (IOException e) {
                // Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            mCamera.stopPreview();
            mCamera.setDisplayOrientation(90);
            mCamera.stopPreview();

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // Stop preview before making changes
            try {
                mCamera.stopPreview();
            }
            catch (Exception e){
                // Ignore: tried to stop a non-existent preview
            }

            // Set preview size and make any resize, rotate or
            // reformatting changes here

            // Start preview with new settings
            try {
                android.view.ViewGroup.LayoutParams lp = mPreview.getLayoutParams();
                lp.width = siz.getWidth() + 50;
                lp.height = siz.getHeight();
                mPreview.setLayoutParams(lp);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            }
            catch (Exception e){
                // Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("YOUR PACKAGE NAME", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
    //----------------------------------------------------------

    // Camera2 methods
    private void callToStart(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextureViewListner = new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    setupCam(width, height);
                    conectCam();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            };

            mCamDeviceSCallback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    mCamDevice = camera;
                    startPreview();
                    previewStarted = true;
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        camera.close();
                    }
                    mCamDevice = null;
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        camera.close();
                    }
                    mCamDevice = null;
                }
            };
        }
    }

    private void setupCam(int width, int heigth){

        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                for(String cameraId: camManager.getCameraIdList()){
                    CameraCharacteristics camChhar = camManager.getCameraCharacteristics(cameraId);
                    if(camChhar.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK){
                        continue;
                    }
                    StreamConfigurationMap map = camChhar.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    int deviseOrientation = getWindowManager().getDefaultDisplay().getRotation();
                    int totalRotation = sensorDOrientation(camChhar, deviseOrientation);
                    boolean swapRotation = totalRotation == 90 || totalRotation == 270;
                    int rotatedWidth = width;
                    int rotatedHeigth = heigth;
                    if(swapRotation){
                        rotatedWidth = heigth;
                        rotatedHeigth = width;
                    }
                    mPreviewSize = choueOSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeigth);
                    mCamId = cameraId;
                    return;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void conectCam() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    camManager.openCamera(mCamId, mCamDeviceSCallback, bHandler);
                }else{
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(this, "reelyChat requires access to camera", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[] {android.Manifest.permission.CAMERA}, requestCamPermisionResult);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    camManager.openCamera(mCamId, mCamDeviceSCallback, bHandler);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPreview(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface preSurface = new Surface(surfaceTexture);

            try {
                mCapturRBuilder = mCamDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mCapturRBuilder.addTarget(preSurface);

                mCamDevice.createCaptureSession(Arrays.asList(preSurface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                session.setRepeatingRequest(mCapturRBuilder.build(), null, bHandler);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {
                        Toast.makeText(getApplicationContext(), "ops looks like there was a problem", Toast.LENGTH_LONG).show();
                    }
                }, null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeCam(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCamDevice.close();
        }
        mCamDevice = null;
    }

    private void startBThread(){
        bHandlerThread = new HandlerThread("reelyChatCam");
        bHandlerThread.start();
        bHandler = new Handler(bHandlerThread.getLooper());
    }

    private void stopBThread(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bHandlerThread.quitSafely();
            try {
                bHandlerThread.join();
                bHandlerThread = null;
                bHandler = null;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static int sensorDOrientation(CameraCharacteristics camCharacteristics, int deviceOrientation){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int sensorOrientation = camCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            deviceOrientation = orientation.get(deviceOrientation);
            return (sensorOrientation + deviceOrientation + 360) % 360;
        }
        else
            return 0;
    }

    private static Size choueOSize(Size[] choices, int width, int heigth){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<Size> bigEnough = new ArrayList<Size>();
            for(Size option : choices){
                if(option.getHeight() == option.getWidth() * heigth / width && option.getWidth() >= width && option.getHeight() >= heigth){
                    bigEnough.add(option);
                }
            }
            if(bigEnough.size() > 0){
                return Collections.min(bigEnough, new compareSizeByArea());
            }
            else{
                return choices[0];
            }
        }
        else
            return null;
    }

    private static class compareSizeByArea implements Comparator<Size> {
        @Override
        public int compare(Size o1, Size o2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return Long.signum((long) o1.getWidth() * o1.getHeight() / (long) o2.getWidth() * o2.getHeight());
            }
            else
                return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(versionCheck()){
            if(requestCode == requestCamPermisionResult){
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    // Toast.makeText(getApplicationContext(), "Please grant access to device camera in aplication settings to use this application", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        if(versionCheck()) {
            startBThread();
            if(mTextureView.isAvailable()){
                setupCam(mTextureView.getWidth(), mTextureView.getHeight());
                conectCam();
            }
            else{
                mTextureView.setSurfaceTextureListener(mTextureViewListner);
            }
        }
    }

    @Override
    protected void onPause() {
        if(versionCheck()) {
            if(previewStarted){
                closeCam();
                stopBThread();
            }
        }
        super.onPause();
    }
}
