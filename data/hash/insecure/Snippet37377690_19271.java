String login;
String password;


login = JOptionPane.showInputDialog("Login : ");
password = JOptionPane.showInputDialog("Password : ");

MessageDigest m; 


try 
{ 
m = MessageDigest.getInstance("MD5");
m.update(login.getBytes(),0,login.length()); 
m.update(password.getBytes(),0,password.length());
BigInteger login1 = new BigInteger(1, m.digest()); 
BigInteger password1 = new BigInteger(1, m.digest());

login = String.format("%1$032X", login1); 
password = String.format("%1$032X", password1); 

JOptionPane.showMessageDialog(null,"Login : " + login + 
                "\nPassword : " + password);

//System.out.println("login : "+ login); 
//System.out.println("password : " + password);
} 
