package javaapplication1;
/*
This is the registration class
This is the class that has the code to
Add a new employee
This is by entering such information as their
Username
Password Business Name
Type of Business
Email
Contact Number
Address
Employee Status
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Registration extends javax.swing.JFrame {
public static Random rand = new Random((new Date()).getTime());
  Statement stmt;
public Registration() {
    initComponents();
}
@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
private void initComponents() {

    EnterButton = new javax.swing.JButton();
    ClearButton = new javax.swing.JButton();
    UserNameLabel = new javax.swing.JLabel();
    ContactNumberLabel = new javax.swing.JLabel();
    AddressLabel = new javax.swing.JLabel();
    EmailLabel = new javax.swing.JLabel();
    UsernameTextField = new javax.swing.JTextField();
    BusinessNameTextField = new javax.swing.JTextField();
    RegistrationLabel = new javax.swing.JLabel();
    PasswordLabel = new javax.swing.JLabel();
    BusinessNameLabel = new javax.swing.JLabel();
    TypeOfBusinessLabel = new javax.swing.JLabel();
    EmailTextField = new javax.swing.JTextField();
    ContactNumberTextField = new javax.swing.JTextField();
    AddressTextField = new javax.swing.JTextField();
    PasswordTextField = new javax.swing.JPasswordField();
    BusinessTypeComboBox = new javax.swing.JComboBox<>();
    EmployedLabel = new javax.swing.JLabel();
    YesRadioButton = new javax.swing.JRadioButton();
    NoRadioButton = new javax.swing.JRadioButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setAlwaysOnTop(true);
    setMinimumSize(new java.awt.Dimension(93, 432));

    EnterButton.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    EnterButton.setText("Enter");
    EnterButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            EnterButtonActionPerformed(evt);
        }
    });

    ClearButton.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    ClearButton.setText("Clear");
    ClearButton.setToolTipText("");
    ClearButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ClearButtonActionPerformed(evt);
        }
    });

    UserNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    UserNameLabel.setLabelFor(UsernameTextField);
    UserNameLabel.setText("UserName");

    ContactNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    ContactNumberLabel.setText("Contact Number");

    AddressLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    AddressLabel.setLabelFor(BusinessNameTextField);
    AddressLabel.setText("Address");
    AddressLabel.setToolTipText("");

    EmailLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    EmailLabel.setText("Email");

    UsernameTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            UsernameTextFieldActionPerformed(evt);
        }
    });

    BusinessNameTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BusinessNameTextFieldActionPerformed(evt);
        }
    });

    RegistrationLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
    RegistrationLabel.setText("Employee Registration");
    RegistrationLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    RegistrationLabel.setMaximumSize(new java.awt.Dimension(100, 25));

    PasswordLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    PasswordLabel.setText("Password");

    BusinessNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    BusinessNameLabel.setText("Business Name");

    TypeOfBusinessLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    TypeOfBusinessLabel.setText("Type of Business");

    EmailTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            EmailTextFieldActionPerformed(evt);
        }
    });

    ContactNumberTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ContactNumberTextFieldActionPerformed(evt);
        }
    });

    AddressTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            AddressTextFieldActionPerformed(evt);
        }
    });

    BusinessTypeComboBox.setEditable(true);
    BusinessTypeComboBox.setMaximumRowCount(30);
    BusinessTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Laundromat", "Computer Shop" }));
    BusinessTypeComboBox.setMinimumSize(new java.awt.Dimension(95, 20));
    BusinessTypeComboBox.setName(""); // NOI18N
    BusinessTypeComboBox.setPreferredSize(new java.awt.Dimension(900, 900));
    BusinessTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BusinessTypeComboBoxActionPerformed(evt);
        }
    });

    EmployedLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    EmployedLabel.setText("Employed");

    YesRadioButton.setText("Yes");
    YesRadioButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            YesRadioButtonActionPerformed(evt);
        }
    });

    NoRadioButton.setText("No");
    NoRadioButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            NoRadioButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(278, 278, 278)
                    .addComponent(RegistrationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(189, 189, 189)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(BusinessNameLabel)
                        .addComponent(PasswordLabel)
                        .addComponent(UserNameLabel)
                        .addComponent(TypeOfBusinessLabel)
                        .addComponent(EmailLabel)
                        .addComponent(ContactNumberLabel)
                        .addComponent(AddressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(EmployedLabel))
                    .addGap(38, 38, 38)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(BusinessTypeComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ContactNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(EmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(BusinessNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(PasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(YesRadioButton)
                            .addGap(18, 18, 18)
                            .addComponent(NoRadioButton))))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ClearButton)
                        .addComponent(EnterButton))
                    .addGap(177, 177, 177)))
            .addGap(0, 210, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(33, 33, 33)
            .addComponent(RegistrationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(PasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)
                    .addComponent(BusinessNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(BusinessTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(UserNameLabel)
                    .addGap(16, 16, 16)
                    .addComponent(PasswordLabel)
                    .addGap(18, 18, 18)
                    .addComponent(BusinessNameLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TypeOfBusinessLabel)))
            .addGap(6, 6, 6)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(EmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EmailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ContactNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ContactNumberLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(AddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(AddressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(13, 13, 13)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(EmployedLabel)
                .addComponent(YesRadioButton)
                .addComponent(NoRadioButton))
            .addGap(18, 18, 18)
            .addComponent(EnterButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(ClearButton)
            .addContainerGap(79, Short.MAX_VALUE))
    );

    pack();
}// </editor-fold>                        

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
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
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


private void UsernameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                  
    // This is the text field for the username
}                                                 

private void BusinessNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                      
    // This is the text field for the type of business
}                                                     

private void EnterButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
    /* This is the enter button.
       When this is pressed the customers details are entered in the database
   */

    String url = "jdbc:derby://localhost:1527/Customers";
    String username = "username";
    String password = "password";

    String encString = encrypt(PasswordTextField.getText());
    try {
            Connection conn = DriverManager.getConnection(url, username, password);
         if (conn !=null){
            System.out.println("Connected");
            String sql = "INSERT INTO Registration (username, Password, businessName, typeofBusiness, email, contactnumber, address, Employed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, UsernameTextField.getText());
            statement.setString(2, encString = (PasswordTextField.getText()));  
            statement.setString(3, BusinessNameTextField.getText());
            statement.setString(4, BusinessTypeComboBox.getSelectedItem().toString());
            statement.setString(5, EmailTextField.getText());
            statement.setString(6, ContactNumberTextField.getText());
            statement.setString(7, AddressTextField.getText());
            statement.setString(8, EmployStatus);


            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                final JDialog dialog = new JDialog();
                dialog.setAlwaysOnTop(true);    
                JOptionPane.showMessageDialog(dialog, "A new user has been added successfully");
            }
        }

} catch (SQLException ex){
                final JDialog dialog = new JDialog();
                dialog.setAlwaysOnTop(true);    
                JOptionPane.showMessageDialog(dialog, "ERROR!");
    }
}                                           

private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
/*This is the Clear button
When it is pressed it will clear all the text fields
*/
UsernameTextField.setText("");
PasswordTextField.setText("");
BusinessNameTextField.setText("");
EmailTextField.setText("");
ContactNumberTextField.setText("");
AddressTextField.setText("");
}                                           

private void EmailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                               
    // This is the email
}                                              

private void ContactNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                       
    // This is the customers contact number 
}                                                      

private void AddressTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                 
    // This is the customers address
}                                                

private void BusinessTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                     
    /* This the drop down list for the type of business
       The user can choose a company already on the lisr or they can enter a new company
    */
}                                                    

private void YesRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
    // This is the yes radio button for if the employee is still employed
    EmployStatus = "Yes";
}                                              

private void NoRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
    // This is the no radio button for if the employee is still employed
    EmployStatus = "No";
}                                             
public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Registration().setVisible(true);
        }
    });
}

// Variables declaration - do not modify                     
private javax.swing.JLabel AddressLabel;
private javax.swing.JTextField AddressTextField;
private javax.swing.JLabel BusinessNameLabel;
private javax.swing.JTextField BusinessNameTextField;
private javax.swing.JComboBox<String> BusinessTypeComboBox;
private javax.swing.JButton ClearButton;
private javax.swing.JLabel ContactNumberLabel;
private javax.swing.JTextField ContactNumberTextField;
private javax.swing.JLabel EmailLabel;
private javax.swing.JTextField EmailTextField;
private javax.swing.JLabel EmployedLabel;
private javax.swing.JButton EnterButton;
private javax.swing.JRadioButton NoRadioButton;
private javax.swing.JLabel PasswordLabel;
private javax.swing.JPasswordField PasswordTextField;
private javax.swing.JLabel RegistrationLabel;
private javax.swing.JLabel TypeOfBusinessLabel;
private javax.swing.JLabel UserNameLabel;
private javax.swing.JTextField UsernameTextField;
private javax.swing.JRadioButton YesRadioButton;
// End of variables declaration                   
private String EmployStatus;
 }
