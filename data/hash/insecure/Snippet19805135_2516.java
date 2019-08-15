       public class RegisterActivity  extends Activity{
   TextView tv1,tv2;
   Button b;
    EditText email,nname,npassword,phone,repassword,usernam;
   ImageView iv;
     Button btnCreateAccount;
    private DatabaseRegister dbHelper;
@Override
  protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState);
     setContentView(R.layout.register);
//  dbHelper = new DatabaseRegister(this);
    dbHelper.open();

  initControls();
    }
       private void initControls()
      {
tv1=(TextView)findViewById(R.id.tv_register_job);
tv2=(TextView)findViewById(R.id.tv_fill_data);
    b=(Button)findViewById(R.id.btn_register);
       email=(EditText)findViewById(R.id.et_email);
      nname=(EditText)findViewById(R.id.et_name);
      npassword=(EditText)findViewById(R.id.et_password);
     phone=(EditText)findViewById(R.id.et_phone);
       repassword=(EditText)findViewById(R.id.et_repass);
       usernam=(EditText)findViewById(R.id.et_username);
      iv=(ImageView)findViewById(R.id.iv_search);
   btnCreateAccount.setOnClickListener(new OnClickListener() {

   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub
    Log.i("click", "button");
   }
        });

          }
          private void ClearForm()
         {

        usernam.setText("");
           nname.setText("");
          npassword.setText("");
         email.setText("");
       repassword.setText("");
             phone.setText("");
          }
        private void RegisterMe(View v)
         {
//Get user details. 
String username = usernam.getText().toString();
String password = npassword.getText().toString();
String confirmpassword = repassword.getText().toString();
String name=nname.getText().toString();
String contact=phone.getText().toString();
String emailid=email.getText().toString();
//Check if all fields have been completed.
if (username.equals("") || password.equals("")||confirmpassword.equals("")||name.equals("")||contact.equals("")||email id.equals("")){
    Toast.makeText(getApplicationContext(), 
            "Please ensure all fields have been completed.",
              Toast.LENGTH_SHORT).show();
    return;
    }

//Check password match. 
   if (!password.equals(confirmpassword)) {
    Toast.makeText(getApplicationContext(), 
            "The password does not match.",
                Toast.LENGTH_SHORT).show();
                npassword.setText("");
                repassword.setText("");
    return;
   }

//Encrypt password with MD5.
password = md5(password);

//Check database for existing users.
Cursor user = dbHelper.fetchUser(username, password, confirmpassword, emailid, name, contact);
if (user == null) {
    Toast.makeText(getApplicationContext(), "Database query error",
              Toast.LENGTH_SHORT).show();
} else {
    startManagingCursor(user);

    //Check for duplicate usernames
    if (user.getCount() > 0) {
        Toast.makeText(getApplicationContext(), "The username is already registered",
                  Toast.LENGTH_SHORT).show();
        stopManagingCursor(user);
        user.close();
        return;
    }
    stopManagingCursor(user);
    user.close();
    user = dbHelper.fetchUser(username, password, emailid, name, contact, confirmpassword);
    if (user == null) {
        Toast.makeText(getApplicationContext(), "Database query error",
                  Toast.LENGTH_SHORT).show();
        return;
    } else {
        startManagingCursor(user);

        if (user.getCount() > 0) {
            Toast.makeText(getApplicationContext(), "The username is already registered",
                      Toast.LENGTH_SHORT).show();
            stopManagingCursor(user);
            user.close();
            return;
        }
        stopManagingCursor(user);
        user.close();
    }
    //Create the new username.
    long id = dbHelper.createUser(username, password, emailid, name, contact, confirmpassword);
    if (id > 0) {
        Toast.makeText(getApplicationContext(), "Your username was created",
                  Toast.LENGTH_SHORT).show();
        saveLoggedInUId(id, username, password,confirmpassword, emailid, name,contact);
        Intent i = new Intent(v.getContext(), MainActivity.class);
        startActivity(i);

        finish();
    } else {
        Toast.makeText(getApplicationContext(), "Failt to create new username",
                  Toast.LENGTH_SHORT).show();
            }
          }
     }

private void saveLoggedInUId(long id, String username, String password,String name,String contact,String email,String repass) {
SharedPreferences settings = getSharedPreferences(login.MY_PREFS, 0);
Editor editor = settings.edit();
editor.putLong("uid", id);
editor.putString("username", username);
editor.putString("password", password);
editor.putString("repass", repass);
editor.putString("name", name);
editor.putString("emailid", email);
editor.putString("contact", contact);

editor.commit();
          }
         /**
         * Hashes the password with MD5.  
           *        @param s
        * @return
     */
      private String md5(String s) {
          try {

MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
digest.update(s.getBytes());
byte messageDigest[] = digest.digest();


StringBuffer hexString = new StringBuffer();
for (int i=0; i<messageDigest.length; i++)
    hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
return hexString.toString();

      } catch (NoSuchAlgorithmException e) {
return s;
   }
     }
      }
