public class LoginActivity extends Activity{

    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.s_login);
    }

    public void tryLogin(View v){
        EditText un = (EditText) findViewById(R.id.et_li_username);
        EditText pw = (EditText) findViewById(R.id.et_li_password);

        String username = un.getText().toString();
        String password = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pw.getText().toString().getBytes(), 0, pw.getText().toString().length());
            password = new BigInteger(1, md.digest()).toString(16);         
        } catch (NoSuchAlgorithmException e) {
            ToastHandler.showToast(e.getMessage(), this);
        }

        ToastHandler.writeToLog("now exevuting get");
        String url = RestHandler.REST_URL + "arg=0&un=" + username + "&pw=" + password + "&" + RestHandler.API_KEY;

        String[] response = RestHandler.executeGET(url, this);  // Line 43
        String formattedResponse = "";



        if(response != null && response[1] != null){
            ToastHandler.writeToLog(response[1]);
            if(response[0].equals("json")){

            }else{
                String temp = response[1].trim();
                formattedResponse = temp.substring(1, temp.length()-1);
            }
        }else{
            ToastHandler.showToast("Fehler beim Einloggen!", this);
        }       

        if(formattedResponse == "Livia" || formattedResponse == "Luki" || formattedResponse.equals("Maki") 
                || formattedResponse=="Nadine" || formattedResponse=="Roberto"){
            loginUser(formattedResponse);
        }else{
            ToastHandler.showToast("Falsche Benutzerangaben!", this);
        }
    }

    private void loginUser(String username){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        //finish();    
    }

}
