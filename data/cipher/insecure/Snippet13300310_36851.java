public class AccessApp extends Activity implements OnClickListener {
private SharedPreferences sp;
String user,pass;
Button lBttn,cBttn;
EditText uname,pword;
Intent i;

int flag=0;

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState)
{ 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    lBttn=(Button)findViewById(R.id.login_button);
    cBttn=(Button)findViewById(R.id.cancel_button);
    uname=(EditText)findViewById(R.id.username);
    pword=(EditText)findViewById(R.id.password);

    lBttn.setOnClickListener(this);
    cBttn.setOnClickListener(this);
}
public void onClick(View arg0) {

    sp=this.getSharedPreferences("AccessApp", MODE_WORLD_READABLE);
    user = sp.getString("USERNAME_KEY", "");
    pass = sp.getString("PASSWORD_KEY", "");




   if(lBttn.equals(arg0)){

      if((uname.getText().toString().equals(user))&& 
        (pword.getText().toString().equals(pass)))

            {
          Toast.makeText(this, "You are Logged In", 20000).show();

               Intent intent;
               intent=new Intent(this,details.class);
               startActivity(intent);
              flag=1;
            }

        else 
           {
            Toast.makeText(this, "Wrong Username or Password",20000).show();
            flag=0;   
           }       
        } 
        else if(cBttn==arg0){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
          builder.setTitle("Exit");
         builder.setMessage("Do you want to exit");
    builder.setCancelable(false);
    builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

 public void onClick(DialogInterface dialog, int which) {
 // TODO Auto-generated method stub
Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_HOME);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);

 finish();
 }
 });
    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
               arg0.cancel();
            }
        });
    AlertDialog alert=builder.create();
    alert.show();

        }

    }
@Override
public boolean onKeyDown(int keyCode, KeyEvent event)  {
 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

     Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
     finish();
 }
 return super.onKeyDown(keyCode, event);
}

public static String decrypt(String encryptedText, byte[ ] key) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] toDecrypt = Base64.decode(encryptedText);
    byte[] encrypted = cipher.doFinal(toDecrypt);
    return new String(encrypted);
}
}
