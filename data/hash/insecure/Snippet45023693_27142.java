public class LoginActivity extends AppCompatActivity{
    //private Button login, register;
    private EditText etUser, etPass;
    private DatabaseHelper dbHelper;
    private Session session;
    private static final String SALT = "50C7BC9D21";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        session = new Session(this);
        etUser = (EditText)findViewById(R.id.etUsername);
        etPass = (EditText)findViewById(R.id.etPassword);
        //login = (Button)findViewById(R.id.btnLogin);
        //register = (Button)findViewById(R.id.btnRegister);
        //login.setOnClickListener(this);
        //register.setOnClickListener(this);

        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    /*
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnRegister:
                //register();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            default:

        }
    }*/

    /**
     * METHOD THAT WILL BE EXECUTED ONCE THE EXIT BUTTON IS CLICKED
     * ITS SOLO PURPOSE IS TO TERMINATE THE APPLICATION.
     * */
    public void exitApp(View v){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * METHOD THAT WILL BE EXECUTED ONCE THE REGISTER BUTTON IS CLICKED
     * ITS SOLO PURPOSE IS TO TAKE THE USER TO THE REGISTER WINDOW.
     * */
    public void newUser(View v){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    /**
     * METHOD THAT WILL BE EXECUTED ONCE THE LOGIN BUTTON IS CLICKED
     * ITS SOLO PURPOSE IT TO COMPARE THE INFORMATION ENTERED IN THE EDITTEXT BOXES
     * AND SEE IF ITS EQUAL WITH THE INFORMATION STORED IN THE DATABASE.
     * */
    public void loginUser(View v){
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();

        String passwordToHash = pass;
        String generatedPassword = null;
        String finalpw = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("SHA1");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
            finalpw = generatedPassword + SALT;
            Toast.makeText(this, finalpw, Toast.LENGTH_SHORT).show();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        System.out.println(generatedPassword);

        //THIS WAS FIXED AROUND BECAUSE THE OTHER WAY FETCHING AND EQUALLING WITH DB AN USER COULD PASS BY WITHOUT REGESTERING.
        if(dbHelper.getUser(user,finalpw)){
            session.setLoggedin(true);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), R.string.wrong_info,Toast.LENGTH_SHORT).show();
        }
    }
}
