import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import javax.swing.JFormattedTextField;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;
public class aa extends javax.swing.JFrame {
private JButton jButton1;
private JButton jButton2;
private JLabel jLabel2;
private JLabel jLabel3;
private JButton jButton3;
private JPasswordField jPasswordField1;
private JLabel jLabel1;
private JTextField jTextField1;
private JTextField jTextField3;

/**
* Auto-generated main method to display this JFrame
*/
public static void main(String[] args) {
aa inst = new aa();
inst.setVisible(true);
}

public aa() {
super();
initGUI();
}

private void initGUI() {
try {
setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
this.getContentPane().setLayout(null);
this.setTitle("Program Enkripsi Dekripsi");
{
jButton1 = new JButton();
this.getContentPane().add(jButton1);
jButton1.setText("Enkripsi");
jButton1.setBounds(70, 88, 58, 24);
jButton1.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent evt) {

//TODO add your code for jButton10.actionPerformed
saveFile();

}
});
}
{
jTextField1 = new JTextField();
this.getContentPane().add(jTextField1);
jTextField1.setBounds(71, 17, 141, 19);
}
{
jLabel1 = new JLabel();
this.getContentPane().add(jLabel1);
jLabel1.setText("Input");
jLabel1.setBounds(8, 10, 60, 30);
}
{
jButton2 = new JButton();
this.getContentPane().add(jButton2);
jButton2.setText("Browse");
jButton2.setBounds(218, 17, 65, 19);
jButton2.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent evt){
//TODO add your code for jButton1.actionPerformed
loadFile();
}
});
}
{
jLabel2 = new JLabel();
this.getContentPane().add(jLabel2);
jLabel2.setText("Password");
jLabel2.setBounds(7, 42, 60, 30);
}
{
jPasswordField1 = new JPasswordField();
this.getContentPane().add(jPasswordField1);
jPasswordField1.setBounds(71, 47, 141, 19);
}
{
jButton3 = new JButton();
this.getContentPane().add(jButton3);
jButton3.setText("Dekripsi");
jButton3.setBounds(150, 89, 62, 24);
jButton3.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent evt) {
saveFile2();
}
});
}
{
jLabel3 = new JLabel();
this.getContentPane().add(jLabel3);
jLabel3.setBounds(68, 112, 148, 30);
jLabel3.setFont(new java.awt.Font("Dialog",0,10));
jLabel3.setForeground(new java.awt.Color(0,0,255));
}

pack();
this.setSize(299, 170);
} catch (Exception e) {
e.printStackTrace();
}
}

private void loadFile ( ) {
JFileChooser chooser = new JFileChooser( );
int result = chooser.showOpenDialog(this);
if (result == JFileChooser.CANCEL_OPTION) return;
try {
jTextField1.setText(chooser.getSelectedFile().getAbsolutePath());
jTextField3.setText(chooser.getSelectedFile().getName());
}
catch (Exception e) {

}
}

private void saveFile( ) {
File plain = new File(jTextField1.getText());
Key key;
//copy = chooser.getSelectedFile();
File copy = new File(jTextField1.getText()+".dika");
try {
KeyGenerator generator = KeyGenerator.getInstance("Blowfish");
// initialization of keygenerator with PRNG

byte[] seed = jPasswordField1.getText().getBytes();
generator.init(new SecureRandom(seed));
// generating key
key = generator.generateKey();
// creating of file
File kunci =new File(jTextField1.getText()+".key");
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(kunci));
// writing key to a file
out.writeObject(key);
// closing of stream
out.close();

Cipher cipher = Cipher.getInstance("Blowfish/EBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key);
FileInputStream fis = new FileInputStream(plain);
FileOutputStream fos = new FileOutputStream(copy);
CipherOutputStream out2 = new CipherOutputStream(fos, cipher);
byte[] buffer = new byte[1024];
while (fis.read(buffer)>=0) {
out2.write(buffer);
}
kunci.delete();
plain.delete();
JOptionPane.showMessageDialog(this,"File Berhasil dienkripsi.");

}catch(Exception e) {

JOptionPane.showMessageDialog(this, e);
JOptionPane.showMessageDialog(this,"File Gagal dienkripsi.");


}finally {

}
}

private void saveFile2( ) {

//JFileChooser chooser = new JFileChooser( );
//chooser.showSaveDialog(this);
File plain = new File(jTextField1.getText());
Key key;
//copy = chooser.getSelectedFile();
File copy = new File(jTextField1.getText().replaceAll(".dika",""));

try {
KeyGenerator generator = KeyGenerator.getInstance("Blowfish");
// initialization of keygenerator with PRNG
byte[] dika = jPasswordField1.getText().getBytes();
generator.init(new SecureRandom(dika));
// generating key
key = generator.generateKey();
File kunci =new File(jTextField1.getText().replaceAll(".dika","")+".key");
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(kunci));
// writing key to a file
out.writeObject(key);
// closing of stream
out.close();

Cipher cipher = Cipher.getInstance("Blowfish/EBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, key);
FileInputStream fis = new FileInputStream(plain);
FileOutputStream fos = new FileOutputStream(copy);
CipherOutputStream out2 = new CipherOutputStream(fos, cipher);
byte[] buffer = new byte[1024];
while (fis.read(buffer)>=0) {
out2.write(buffer);
}
fos.close();
fis.close();
kunci.delete();
plain.delete();
JOptionPane.showMessageDialog(this,"File berhasil di dekripsi.");
}catch(Exception e) {
JOptionPane.showMessageDialog(this, e);
JOptionPane.showMessageDialog(this,"File gagal di dekripsi.");
} finally {

}

}

}
