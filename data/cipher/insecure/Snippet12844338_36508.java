public void onClick(View arg0) {
    user=rName.getText().toString().trim();
    pass=rPwd.getText().toString().trim();

    if(arg0==regBttn){
        if((user.length()!=0))
        {
            if((pass.length()!=0))
            {

                sp=getSharedPreferences("AccessApp",MODE_WORLD_WRITEABLE);
                Editor myEditor=sp.edit();

                try {
                    myEditor.putString("USERNAME_KEY", user);
                    byte[ ] superSecretKeyBytes = Base64.decode(user);
                    byte[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };
                    for (int i = 0; i &lt; pass.length(); i++) {
                        key[i] = superSecretKeyBytes[i];
                    }
                    } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    myEditor.putString("PASSWORD_KEY", pass);
                    byte[ ] superSecretKeyBytes = Base64.decode(pass);
                    byte[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };
                    for (int i = 0; i &lt; pass.length(); i++) {
                        key[i] = superSecretKeyBytes[i];
                    }
                    } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                myEditor.commit();
                Toast.makeText(this, "Registration is successfull",10000).show();
                i=new Intent(this,AccessApp.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Please Enter password", 10000).show();
            }
        }
        else{
            Toast.makeText(this,"Please Enter Username",10000).show();
        }
    }

    else if(arg0==rtnBttn){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
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

public String encrypt(String toencrypt, byte key[]) throws Exception {
    SecretKeySpec secret = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] encryptedbytes = cipher.doFinal(toencrypt.getBytes());
    String encrypted = Base64.encodeBytes(encryptedbytes);
    return encrypted;

}
