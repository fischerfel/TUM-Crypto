      public class CameraActivity extends AppCompatActivity {
private RecyclerView horizontal_rv;
Bitmap result, upload_bitmap, large_bitmap;
int i=0,j=0;
Calendar c;
Parcelable listState;
String path,large_bitmap_path;
SimpleDateFormat df , tf;
String formattedDate, formattedTime;
String fileName;
ProgressDialog progressDialog;
private ArrayList<String> horizontalList, fileNameList;
ArrayList<Uri> large_bitmapList;
private RecyclerViewAdapter recycleViewAdapter;
CameraView camera;
ImageView back_button;
ImageView switch_camera;
Button upload_btn;
LinearLayoutManager horizontal_lm;
ByteArrayOutputStream bytes;
int flag_camera_switch=0,flag_flash_toggle=0;
ImageView flash_toggle,pick_from_gallery;
private static int LOAD_IMAGE_RESULTS = 1;



@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    horizontalList = new ArrayList<>(); // initializing the list for small images
    large_bitmapList=new ArrayList<>();
    fileNameList=new ArrayList<>();
    upload_btn = (Button) findViewById(R.id.upload_icon);  //button to upload image on server

    upload_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                 uploadButtonClick();
        }});
    camera = (CameraView) findViewById(R.id.camera);
    switch_camera = (ImageView) findViewById(R.id.switch_camera);
    Button button = (Button) findViewById(R.id.capture_button);
    horizontal_rv = (RecyclerView) findViewById(R.id.horizontal_recycler_view);//initializing the recycler view
    horizontal_lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false); // calling the layout manager
    horizontal_rv.setLayoutManager(horizontal_lm); //setting the layout manager
    recycleViewAdapter = new RecyclerViewAdapter(horizontalList); // initializing the adapter, passing the list
    horizontal_rv.setAdapter(recycleViewAdapter);
    // set up the custom action bar
    horizontal_rv.addOnScrollListener(new RecyclerView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            super.onScrollStateChanged(recyclerView, newState);
        }

    });
    camera.setCameraListener(new CameraListener() {
        @Override
        public void onPictureTaken(byte[] picture) {
            super.onPictureTaken(picture);
            upload_bitmap= BitmapFactory.decodeByteArray(picture, 0, picture.length);
            result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            captureImage();
        }
    });
    //Action Bar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setCustomView(R.layout.camera_actionbar);
    View view = getSupportActionBar().getCustomView();
    back_button = (ImageView) view.findViewById(R.id.back_button_actionbar);
    flash_toggle = (ImageView) view.findViewById(R.id.flash_actionbar);
    pick_from_gallery = (ImageView) view.findViewById(R.id.upload_gallery_actionbar);
    pick_from_gallery.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
            startActivityForResult(i, LOAD_IMAGE_RESULTS);
        }
    });
    back_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CameraActivity.this, DashboardActivty.class);
            startActivity(intent);
            finish();
        }
    });
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try{
                camera.captureImage();
               }
            catch (Exception exception){
                Toast.makeText(getApplicationContext(),"Please wait",Toast.LENGTH_SHORT).show();
            }

        }
    });


    // toggle for camera - front and back
    switch_camera.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switchCamera();
        }
    });
    // toggle for flash
    flash_toggle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            flashToggle();
        }
    });
}
public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
    Bitmap resizedBitmap=null;
    try {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
    }
    catch (Exception e){
        Toast.makeText(CameraActivity.this,"Please wait",Toast.LENGTH_SHORT).show();
    }
    return resizedBitmap;
}  // recycle bitmap 50x50
@Override
protected void onResume() {
    super.onResume();
    camera.start();
    if(listState!=null)
        horizontal_lm.onRestoreInstanceState(listState);
}

@Override
protected void onPause() {
    super.onPause();
    camera.stop();

}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Here we need to check if the activity that was triggers was the Image Gallery.
    // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
    // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
    if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
        // Let's read picked image data - its URI
        Uri pickedImage = data.getData();
        // Let's read picked image path using content resolver
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        horizontalList.add(imagePath);
        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close();
    }
} // get image from the gallery
public String getStringImage(Bitmap bmp){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] imageBytes = baos.toByteArray();
    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    return encodedImage;
} // function to get String (Base64) encoded to upload to server
private void uploadImage(final String image_path){
    //Showing the progress dialog
    final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //Disimissing the progress dialog
                    loading.dismiss();
                    //Showing toast message of the response
                    Toast.makeText(CameraActivity.this, s , Toast.LENGTH_SHORT).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //Dismissing the progress dialog
                    loading.dismiss();
                    //Showing toast
                    Toast.makeText(CameraActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
         String img_path=image_path;
            //Converting Bitmap to String

            //Creating parameters
            Map<String,String> parameters = new Hashtable<String, String>();
            //Adding parameters
            parameters.put("postedFile", img_path);
            parameters.put("folderName", "999987");
            parameters.put("categoryName", "Detail Complete");
            parameters.put("estimateNumber", "999999");
            parameters.put("userName", "Chinook/Images");
            parameters.put("imageUploadID", "2017-10-09 01.43.36 PM");
            parameters.put("dateTime", "2017-10-09 01.43 PM");
            parameters.put("type", "image");
            parameters.put("accessToken", "Q)4%v59!@lyr");
            parameters.put("fileName",fileNameList.get(j));
            j++;
            //returning parameters
            return parameters;
        }
    };

    //Creating a Request Queue
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    //Adding request to the queue
    requestQueue.add(stringRequest);
} // convert image to base64 to upload it. 
public void captureImage(){
            large_bitmap=getResizedBitmap(upload_bitmap,500);
            bytes = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 25, bytes);
            Bitmap newResult=getResizedBitmap(result,400,400);
            String fileNameSmall = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
            String fileNameLarge= new SimpleDateFormat("HHmmss'.txt'").format(new Date());
            path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), newResult, fileNameSmall, null);
            large_bitmap_path= MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), large_bitmap, fileNameLarge, null);
            c = Calendar.getInstance();
            df = new SimpleDateFormat("yyyy-MM-dd");
            fileNameList.add(fileName);
            horizontalList.add(path);
            large_bitmapList.add(Uri.parse(large_bitmap_path));
            i++;
            recycleViewAdapter.notifyDataSetChanged();
}
public SSLContext getSslContext() {

    TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    } };

    SSLContext sslContext=null;

    try {
        sslContext = SSLContext.getInstance("TLS");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    return sslContext;

}
public void flashToggle(){
    if(flag_flash_toggle==0){
        camera.setFlash(CameraKit.Constants.FLASH_AUTO);
        flash_toggle.setImageResource(R.drawable.camera_flash_auto);
        flag_flash_toggle=1;
    }
    else if(flag_flash_toggle==1){
        camera.setFlash(CameraKit.Constants.FLASH_ON);
        flag_flash_toggle=2;
        flash_toggle.setImageResource(R.drawable.camera_flash_on);
    }
    else if(flag_flash_toggle==2){
        camera.setFlash(CameraKit.Constants.FLASH_OFF);
        flash_toggle.setImageResource(R.drawable.camera_flash_off);
        flag_flash_toggle=0;
    }


} // function to toggle the camera
public void switchCamera(){
    if(flag_camera_switch==0){
        camera.setFacing(CameraKit.Constants.FACING_BACK);
        flag_camera_switch=1;
    }
    else if(flag_camera_switch==1){
        camera.setFacing(CameraKit.Constants.FACING_FRONT);
        flag_camera_switch=0;
    }
} // function to switch between front and back camera
public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
    int width = image.getWidth();
    int height = image.getHeight();

    float bitmapRatio = (float)width / (float) height;
    if (bitmapRatio > 1) {
        width = maxSize;
        height = (int) (width / bitmapRatio);
    } else {
        height = maxSize;
        width = (int) (height * bitmapRatio);
    }
    return Bitmap.createScaledBitmap(image, width, height, true);
} // resize image for uploading ; overloaded function
public void uploadButtonClick(){
    getSslContext();
    for (int i=0;i<large_bitmapList.size();i++)
        //uploadImage(getStringImage(large_bitmap));
        try {
            uploadImage(getStringImage(MediaStore.Images.Media.getBitmap(this.getContentResolver(), large_bitmapList.get(i))));
        } catch (IOException e) {
          Toast.makeText(CameraActivity.this,"Erro..",Toast.LENGTH_SHORT).show();
        }

} // code to upload images on the server
protected void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);

    state.putParcelable("LIST_STATE_KEY", horizontal_lm.onSaveInstanceState());
  // state.putParcelableArrayList("SAVED_RECYCLER_VIEW_DATASET_ID",horizontalList);
}
protected void onRestoreInstanceState(Bundle state) {
    super.onRestoreInstanceState(state);

    listState = state.getParcelable("LIST_STATE_KEY");
    if(state != null)
        listState=state.getParcelable("LIST_STATE_KEY");
}
