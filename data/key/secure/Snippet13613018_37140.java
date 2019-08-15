44. public void onClick(View arg0) {
45. 
46.   sp=this.getSharedPreferences("AccessApp", MODE_WORLD_READABLE);
47.   
48.    
49.    
50.    
51.   byte[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5 };
52.    
53.    
54.   try {
55.        String decryptedUser = decrypt(user, key);  
56.         user = sp.getString("USERNAME_KEY", decryptedUser);
57.          
58.   }
59. catch (Exception e) {
60.    // TODO Auto-generated catch block
61.   e.printStackTrace();
62.   }   
63.   try {
64.        String decryptedPass = decrypt(pass, key);  
65.        pass = sp.getString("PASSWORD_KEY", decryptedPass);
66.         
67.
68.   } catch (Exception e) {
69.     // TODO Auto-generated catch block
70.    e.printStackTrace();
71.   }
72.   
73.   if(lBttn.equals(arg0)){
74.     
75.      if((uname.getText().toString().equals(user))&& 
76.        (pword.getText().toString().equals(pass)))
77.       
78.            {
79.          Toast.makeText(this, "You are Logged In", 20000).show();
80.                 
81.               Intent intent;
82.                intent=new Intent(this,details.class);
83.                startActivity(intent);
84.              flag=1;
85.            }




135.         public static String decrypt(String encryptedText, byte[ ] key) throws Exception   {
136.    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
137.    Cipher cipher = Cipher.getInstance("AES");
138.    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
139.    byte[] toDecrypt = Base64.decode(encryptedText);
140.    byte[] encrypted = cipher.doFinal(toDecrypt);
141.    return new String(encrypted);
142.   }
143.  }
