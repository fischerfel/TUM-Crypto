    package progressbartest;

    import java.awt.BorderLayout;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.beans.PropertyChangeEvent;
    import java.beans.PropertyChangeListener;
    import java.io.BufferedReader;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileReader;
    import java.io.IOException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import javax.swing.JButton;
    import javax.swing.JFileChooser;
    import javax.swing.JFrame;
    import javax.swing.JLabel;
    import javax.swing.JPanel;
    import javax.swing.JProgressBar;
    import javax.swing.JTextField;
    import javax.swing.SwingWorker;

    public class ProgressBarTest implements ActionListener,   PropertyChangeListener{
     JFrame f;
     JPanel p;
     BorderLayout bdl;
    JButton cf;
    JTextField tf1;
    JLabel ls;
    JProgressBar progressBar;
    JFileChooser fc;
    File sf;
    FileInputStream fis;
    String hexRes1;
    String hexRes2 = "";
    private Task task;
    private int progress;
    String selection;

    public static void main(String[] args) {
     new ProgressBarTest().startApp();// TODO code application logic here
     }

    private void startApp() {
     f = new JFrame();
     p = new JPanel();
     bdl = new BorderLayout();
     cf = new JButton("Check-File");
     ls = new JLabel();
     tf1 = new JTextField();
     progressBar = new JProgressBar(0, 100);
     progressBar.setValue(0);
     progressBar.setStringPainted(true);

     p.setLayout(bdl);
     p.add(BorderLayout.NORTH, cf);
     p.add(BorderLayout.CENTER, ls);
     p.add(BorderLayout.WEST, tf1);
     p.add(BorderLayout.SOUTH, progressBar);

     fc = new JFileChooser();
     cf.addActionListener(this);

     f.getContentPane().add(p);
     //f.pack();
     f.setTitle("ProgressTest");
     f.setSize(950,350);
     f.setResizable(false);
     f.setVisible(true);
     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) throws NullPointerException {
//System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
     //choose a file when the button cf is clicked
     if (e.getSource() == cf) { 
      int returnVal = fc.showOpenDialog(p);
      fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);   
      if (returnVal == JFileChooser.APPROVE_OPTION)    
       sf = fc.getSelectedFile();
      //empty the values
       tf1.setText("");
       hexRes2 = "";
     //start the task to calculate the file
      task = new Task();
      task.addPropertyChangeListener(this);
      task.execute();
      ls.setText("Wait !");
    }   
   } 


   class Task extends SwingWorker<Void, Void> {
    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() throws InterruptedException, IOException {
     //progressbar variables to go thru the file
     setProgress(0);
     int progress = 0;
     BufferedReader br = new BufferedReader(new FileReader(sf));
     String line = br.readLine();
     long readLength = 0;
     long totalLength = sf.length();
     double lengthPerPercent = 100.0 / totalLength;
     //Thread.sleep(1000);
     //while the line of the file isn't empty execute
     try {
      while (line != null) {
      //Thread.sleep(100);
      fis = new FileInputStream(sf);
      //digest calculation
      MessageDigest md  = MessageDigest.getInstance("MD5");

      byte[] b = new byte[1024];
      int i;
      while((i = fis.read(b)) >= 0) {
       md.update(b, 0, i);
      } while(i != -1)  
         fis.close();
         byte[] ba = md.digest();

      for (int j = 0; j < ba.length; j++) {
       hexRes1 = Integer.toHexString((ba[j] & 255) | 256).substring(1);
       hexRes2 += hexRes1;
      } 
     // calculate progress.
     readLength += line.length();
     progress = (int) (lengthPerPercent * readLength);
     setProgress(Math.min(progress, 100));
     line = br.readLine();
    } //printing the value
     tf1.setText(hexRes2); 

   } catch (IOException | NoSuchAlgorithmException ex) {
     Logger.getLogger(ProgressBarTest.class.getName()).log(Level.SEVERE, null, ex);
  } catch (NullPointerException ex) {f.repaint(); 
   } finally {fis.close();}
    return null;
       //System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
  }
 }
   /*
   * Executed in event dispatching thread
   */
   public void done() {
    progressBar.setValue(0);
    ls.setText("Done!\n");
   }
   /**
     * Invoked when task's progress property changes.
    */
    public void propertyChange(PropertyChangeEvent evt) {
     if (evt.getPropertyName().equals("progress")) {
     int progress = (Integer) evt.getNewValue();
     progressBar.setValue(progress);
    }
   }      
  }
