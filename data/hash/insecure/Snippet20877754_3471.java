import java.awt.*;
import javax.swing.*;
import java.security.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Verification extends JFrame implements ActionListener { // Start main class

JPanel p;
JTextField t;
JLabel l, l1;
JButton b;

public Verification() { // Start constructor
super("Verification");
p = new JPanel();
add(p);
p.setLayout(null);
p.setBackground(Color.pink);
l = new JLabel("Enter Code");
l.setBounds(20, 50, 100, 20);
l.setFont(new Font("arial", Font.BOLD, 15));
p.add(l);
l1 = new JLabel("");
l1.setBounds(50, 110, 200, 40);
l1.setFont(new Font("arial", Font.BOLD, 20));
p.add(l1);
t = new JTextField();
t.setBounds(110, 50, 250, 20);
p.add(t);
b = new JButton("Login");
b.setBounds(370, 50, 100, 20);
p.add(b);
b.addActionListener(this);
setSize(500, 190);
setVisible(true);
setLocation(400, 200);
setResizable(false);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
} // End constructor

public void RSA() { //Start RSA
  byte[] n = {6, 1, 3, 4, 8, 9, 0, 5, 11, 24, 12, 57, 12, 78};
  for (int i = 0; i < n.length; i++) { // Start for
    n[i] = (byte) (Math.random() * 80);
    System.out.print(n[i]);
  } // End for
  System.out.println("\n");
} // End RSA

public static String MD5(String input) { // Start MD5
 byte[] n = {6, 1, 3, 4, 8, 9, 0, 5, 11, 24, 12, 57, 12, 78};
 try { // Start try1
    for (int i = 0; i < n.length; i++) { // Start for
        n[i] = (byte) (Math.random() * 80);
        System.out.print(n[i]);
    } // End for
    n = input.getBytes("UTF-8");
  } // End try1
  catch (UnsupportedEncodingException e) {
    n = input.getBytes();
  }

  String result = null;
  char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
      '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  try { // Start try2
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(n);
      byte temp[] = md.digest();
      char str[] = new char[16 * 2];
      int k = 0;
      for (int i = 0; i < 16; i++) { // Start for
        byte byte0 = temp[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
     } // End for
      result = new String(str);
   } // End try2
  catch (Exception e) {
    e.printStackTrace();
   }
   return result;
 } // End MD5

 public void actionPerformed(ActionEvent e) { // Start actionPerformed
  } // End actionPerformed

public static void main(String[] args) { // Start main method
  Verification a = new Verification();
  System.out.println("The RSA Code is : \n");
  a.RSA();
  System.out.println("The MD5 on RSA Code is : \n");
  System.out.println(MD5("Javarmi.com"));
 } // End main method
} // End main class
