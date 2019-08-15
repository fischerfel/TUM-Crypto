public class RSAAPI extends JFrame implements ActionListener{
JButton encrypt,decrypt,clear,exit;
JLabel plainlbl,encrlbl,decrlbl;
JTextArea   plainText,encrText,decrText;

static KeyPair kp;
static KeyPairGenerator kpg;
static Cipher cipher;
static PublicKey publicKey;
static PrivateKey privateKey;

public RSAAPI()
{
    setLayout(null);

    encrypt=new JButton("ENCRYPT");
    decrypt=new JButton("DECRYPT");
    clear=new JButton("CLEAR");
    exit=new JButton("EXIT");

    plainText=new JTextArea();
    encrText=new JTextArea();
    decrText=new JTextArea();

    plainlbl=new JLabel("PLAIN-TEXT");
    encrlbl=new JLabel("ENCRYPTED-TEXT");
    decrlbl=new JLabel("DECRYPTED-TEXT");

    plainlbl.setBounds(30,20,120,15);
    plainText.setBounds(30,40,400,150);

    encrypt.setBounds(180,195,90,15);
    encrlbl.setBounds(30,215,120,15);
    encrText.setBounds(30,235,400,150);

    decrypt.setBounds(180,390,90,15);
    decrlbl.setBounds(30,410,120,15);
    decrText.setBounds(30,430,400,150);

    clear.setBounds(80,585,90,15);
    exit.setBounds(250,585,90,15);      

    add(encrypt);
    add(decrypt);
    add(clear);
    add(plainlbl);
    add(decrlbl);
    add(encrlbl);
    add(exit);
    add(plainText);
    add(encrText);
    add(decrText);

    encrypt.addActionListener(this);
    decrypt.addActionListener(this);
    clear.addActionListener(this);
    exit.addActionListener(this);

    setSize(500,650);
    setVisible(true);
    decrypt.setEnabled(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

public void actionPerformed(ActionEvent ae)
{
    byte[] inData,outData;
    if(ae.getSource()==encrypt)
    {
        try{
            inData= plainText.getText().getBytes();
            outData=RSAAPI.encryptData(inData);
            encrText.setLineWrap(true);
            encrText.setText(new String(outData));
            decrypt.setEnabled(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    else if(ae.getSource()==decrypt)
    {
        try{
            inData= encrText.getText().getBytes();
            outData=RSAAPI.decryptData(inData);
            decrText.setLineWrap(true);
            decrText.setText(new String(outData));
            decrypt.setEnabled(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    else if(ae.getSource()==clear)
    {
        plainText.setText(null);
        encrText.setText(null);
        decrText.setText(null);
        decrypt.setEnabled(false);
    }
    else if(ae.getSource()==exit)
    {
        System.exit(0);
    }
}

public static byte[] encryptData(byte[] inData) throws Exception
{
    publicKey=kp.getPublic();
    cipher=Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE,publicKey);
    return cipher.doFinal(inData);
}

public static byte[] decryptData(byte[] inData) throws Exception
{
    privateKey=kp.getPrivate();
    cipher=Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE,privateKey);
    return cipher.doFinal(inData);      
}

public static void main(String[] args) throws Exception
{
    kpg=KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    kp=kpg.genKeyPair();
    new RSAAPI();
}
