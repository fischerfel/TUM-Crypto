Socket s;
BufferedReader br;
BufferedWriter bw;
TextField text;
Button sendBut, exitBut;
List list;

public client(String st)
{
  super(st);
  setSize(300, 130);

  setLocation(300,0);
  setResizable(true);
  setBackground(new Color(192, 192, 192));
  this.setLayout(new GridLayout(2, 1));

  Panel panels[] = new Panel[2];
  panels[0] = new Panel();
  panels[1] = new Panel();
  panels[0].setLayout(new BorderLayout());
  panels[1].setLayout(new FlowLayout(FlowLayout.LEFT));

  sendBut = new Button("Send");
  exitBut = new Button("Exit");

  sendBut.addActionListener(this);
  exitBut.addActionListener(this);

  list = new List();
  text = new TextField(25);

  panels[0].add(list);
  panels[1].add(text);
  panels[1].add(sendBut);
  panels[1].add(exitBut);     


  add(panels[0]);
  add(panels[1]);

  setVisible(true);

  try
  {
    /* Assuming that this application is run on single
                      machine I've used the default ip i.e., 127.0.0.1. If
                      you want to use it on 2 different machines use the
                      ip that is assigned to the machine on which server
                      applicatin is residing*/

    s = new Socket("127.0.0.1", 1053);
    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    Thread th;
    th = new Thread(this);
    th.start();

  }catch(Exception e){}

}

public static void main(String arg[])
{
  // create an object instance of the class
  // by sending the title as parameter
  new client("Client Application");

}

public void run()
{
  while (true)
  {
    try
    {
      String receive = br.readLine();
      list.addItem(receive);
      byte[] msg1 = receive.getBytes("UTF-8");
      //decrypt
      //Get pri Key
      FileInputStream FISkey1 = new FileInputStream("privatekey.txt");
      ObjectInput oi1 = new ObjectInputStream(FISkey1);  
      Key privateKey = (Key) oi1.readObject(); 
      FISkey1.close();
      Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      //encrypt the public key
      cipher1.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] deciphertext = cipher1.doFinal(msg1);
      String receivePrint = new String(deciphertext, "UTF-8");



      list.addItem(receivePrint);
    }catch (Exception h){
    }
  }
}


public void actionPerformed(ActionEvent ae)
{
  if(ae.getSource().equals(exitBut))
  System.exit(0);
  else
  {
    try
    {
      String s = text.getText();
      byte[] msg = s.getBytes("UTF-8");
      //encrypt
      //Get public Key
      FileInputStream FISkey = new FileInputStream("publickey.txt");
      ObjectInput oi = new ObjectInputStream(FISkey);  
      Key publicKey = (Key) oi.readObject(); 
      FISkey.close();
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      //encrypt the public key
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] ciphertext = cipher.doFinal(msg);
      String send = new String(ciphertext, "UTF-8");
      bw.write(send);
      bw.newLine();
      bw.flush();
      text.setText("");
    }catch(Exception m){}
  }      
}
