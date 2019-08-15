   package javaapplication1;
   import java.awt.*;
   import java.awt.event.*;
   import java.sql.DriverManager;
   import javax.swing.*;
   import java.sql.*;
   //import java.awt.EventQueue;

   //import java.io.IOException;
   import java.security.MessageDigest;
   import java.security.NoSuchAlgorithmException;
   import java.util.Date;
   import java.util.Random;
   //import sun.misc.BASE64Decoder;
   //import sun.misc.BASE64Encoder;

   public class FireBirdTest1 {
      public static Random rand = new Random((new Date()).getTime());

      private JFrame mainFrame;
      private JPanel controlPanel, controlPanel2;
      private GridBagConstraints gbc;

      Connection con;
      Statement stmt;
      private boolean loggedIn = false;
      private String user_name;

      public FireBirdTest1(){
         prepareGUI();
      }

      private void prepareGUI(){
  mainFrame = new JFrame("Java Swing Examples");
  mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  mainFrame.setLayout(new GridLayout(2, 1));
  mainFrame.setLocationRelativeTo(null);
  mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  controlPanel = new JPanel();
  controlPanel.setLayout(new GridBagLayout());

  controlPanel2 = new JPanel();
  controlPanel2.setLayout(new GridBagLayout());

  gbc = new GridBagConstraints();

  mainFrame.add(controlPanel);
      }
        public static String encrypt(String passwordToHash){
            String generatedPassword = null;
           try {
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Add password bytes to digest
        md.update(passwordToHash.getBytes());
        //Get the hash's bytes 
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,         
    16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();
    } 
    catch (NoSuchAlgorithmException e) 
    {
        e.printStackTrace();
    }
    return generatedPassword;
}

      protected void showTextFieldDemo(){
  JLabel  namelabel= new JLabel("User Name: ", JLabel.RIGHT);
  JLabel  passwordLabel = new JLabel("Password: ", JLabel.CENTER);

  final JTextField userText = new JTextField(15);
  final JPasswordField passwordText = new JPasswordField(16);      
  //String encString = encrypt(passwordText.getText());

  JButton loginButton = new JButton("Login");
  loginButton.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {     
        try{
            String url = "jdbc:derby://localhost:1527/Customers";
            String username = "username";
            String password = "password";
            //String database = "Firebird";
            con = DriverManager.getConnection(url,username,password);
            stmt = con.createStatement();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Problem Connecting to               Database");
        }
            String encString = encrypt(passwordText.getText());

            /*String sql = "INSERT INTO users(name, password) VALUES('"+userText.getText()+"', '"+encString+"')";
            stmt.executeUpdate(sql);*/
        try{
            String sql = "SELECT password FROM EmployeeRegistration WHERE username = '"+userText.getText()+"'";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()){
                String password = rs.getString("password");
                //JOptionPane.showMessageDialog(null, password);
                //JOptionPane.showMessageDialog(null, encString);
                if(encString.equals(password)){
                    //JOptionPane.showMessageDialog(null, "You are now logged in.");
                       loggedIn = true;
                   }
                else{
                    JOptionPane.showMessageDialog(null, "Wrong Password");
                }   
            }
            else{
                JOptionPane.showMessageDialog(null, "Incorect Login");
            }
            if(loggedIn){
                  controlPanel.setVisible(false);
                  mainFrame.remove(controlPanel);
                  TheMainMenu main_menu = new TheMainMenu();
                  main_menu.setVisible(true);
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Problem executing SQL Query.");
        }
     }
  }); 

  controlPanel.add(namelabel, gbc);
  controlPanel.add(userText, gbc);
  controlPanel.add(passwordLabel, gbc);       
  controlPanel.add(passwordText, gbc);
  controlPanel.add(loginButton, gbc);
  mainFrame.setVisible(true); 
  //controlPanel.remove(namelabel);
      }
   }
