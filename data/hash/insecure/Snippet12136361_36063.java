public class CameraApp extends Activity {

private static final int IMAGE_CAPTURE = 0;

private ImageButton startBtn;
private ImageButton gallery;
private Uri imageUri;
private ImageView imageView;
SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
Date now = new Date();
String fileName = formatter.format(now);
final Context context = this;


@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera_app);
    imageView = (ImageView)findViewById(R.id.img);
    startBtn = (ImageButton) findViewById(R.id.startBtn);
    gallery = (ImageButton)findViewById(R.id.gallery);

    startBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            startCamera();
        }
    });

    gallery.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setDataAndType(Uri.fromFile(new  File(Environment.getExternalStorageDirectory(),"camera_app")),"image/*");
            startActivity(i);
        }
    });
}

public void startCamera() {
    File photo = null;
    File folder= new File(Environment.getExternalStorageDirectory(),"camera_app");
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");


    if(folder.exists()){
    if (android.os.Environment.getExternalStorageState().equals( android.os.Environment.MEDIA_MOUNTED)) {
        photo = new File(android.os.Environment.getExternalStorageDirectory(), "camera_app/DF_" + fileName + "_PIC.jpg");
    } 
    }

    else{
        folder.mkdirs();
        photo = new File(android.os.Environment.getExternalStorageDirectory(), "camera_app/DF_" + fileName + "_PIC.jpg");
    }
    /*else {
        photo = new File(getCacheDir(), "camera_app/FILE_NAME.jpg");
    }    */

    if (photo != null) {

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }  
}
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == IMAGE_CAPTURE) {
        if (resultCode == RESULT_OK){
            //try {
                Uri selectedImage = imageUri;

                ContentResolver cr = getContentResolver();
                //InputStream inputStream = cr.openInputStream(imageUri);

               Bitmap bitmap;
               try {
               InputStream inputStream = cr.openInputStream(imageUri);
               bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                imageView.setImageBitmap(bitmap);

                java.security.MessageDigest md = MessageDigest.getInstance("MD5");

                  byte[] buffer = new byte[1024];

                 int read;
                    do {
                      read = inputStream.read(buffer);
                      if (read > 0)
                        md.update(buffer, 0, read);
                    } while (read != -1);
                    inputStream.close();

                    byte[] digest = md.digest();

                    String hexString = "";
                    for (int i = 0; i < digest.length; i++) {
                      hexString += Integer.toString((digest[i] & 0xff) 
                                + 0x100, 16).substring(1);
                    }

               final EditText input = new EditText(this);

                input.setText("DF_" + fileName + "_PIC_");
                new AlertDialog.Builder(this)
                .setTitle("Rename")
                .setMessage("<MD5> "+ hexString)
                .setView(input) 

               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                        String value = input.getText().toString();
                        String value1 = value + ".jpg";
                        final File F=new File(android.os.Environment.getExternalStorageDirectory(), "camera_app/DF_" + fileName + "_PIC.jpg");

                        File newfile=new File(F.getParent(),value1);
                        F.renameTo(newfile);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                })
                .show();

            } catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                Log.e("Camera", e.toString());
            }


        } else {
            imageUri = null;
            imageView.setImageBitmap(null);

        }       
    } 

}
