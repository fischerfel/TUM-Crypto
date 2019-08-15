     package pbtest;

 /**
 *
 * @author winLin
  */
  import java.awt.*;
  import java.awt.event.*;
  import javax.swing.*;
    import java.beans.*;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.Random;
    import java.util.logging.Level;
    import java.util.logging.Logger;

  public class PbTest extends JPanel
                         implements ActionListener, 
                                    PropertyChangeListener {

private JProgressBar progressBar;
private JButton startButton;
private JTextArea taskOutput;
private Task task;
private String nameString;
private String s2;
private int timeFlow;

class Task extends SwingWorker<Void, Void> {
    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {

        /*
        other code to test...
        */
        /*int i = (int) Math.random();
        int progress = 0;
        String[] listS = {"rewr","323","erw","ewr243","hjj","zu","55","vb","cve","as354"};
        for(String s : listS) {
        nameString = s;
        //System.out.print(s);
        }
        while(nameString == "" && Math.random() != 5) {
        for(int j = 0; j < nameString.length(); j++)
        progress = j;
        setProgress(progress);
        System.out.print(progress);
        }*/
        /*
        Random random = new Random();
        int progress = 0;
        //Initialize progress property.
        setProgress(0);
        while (progress < 100) {
            //Sleep for up to one second.
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException ignore) {}
            //Make random progress.
            progress += random.nextInt(10);
            setProgress(Math.min(progress, 100));
        */
        boolean done = false;
try {
FileInputStream fis = new FileInputStream(someFile.iso);

MessageDigest md  = MessageDigest.getInstance("MD5");

byte[] b = new byte[1024];
int i;
while((i = fis.read(b)) >= 0) {
 md.update(b, 0, i);
} while(i != -1) 
fis.close();
byte[] ba = md.digest();
       //System.out.print(i);  
for (int j = 0; j < ba.length; j++) {
    String s1 = Integer.toHexString((ba[j] & 255) | 256).substring(1);
    s2 += s1;
// This is the Code which should get the percentage ?
} while(s2 == "") {
   for(int z = 0; z < s2.length(); z++)
    timeFlow = s2.length();
    setProgress(timeFlow);
    //System.out.print(timeFlow);
  }
  System.out.println(s2);
      } catch (IOException | NoSuchAlgorithmException ex) {
 Logger.getLogger(PbTest.class.getName()).log(Level.SEVERE, null, ex);
} return null;
              }          //System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());




    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
        startButton.setEnabled(true);
        setCursor(null); //turn off the wait cursor
        taskOutput.append("Done!\n");
    }
}

public PbTest() {
    super(new BorderLayout());

    //Create the demo's UI.
    startButton = new JButton("Start");
    startButton.setActionCommand("start");
    startButton.addActionListener(this);

    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);

    taskOutput = new JTextArea(5, 20);
    taskOutput.setMargin(new Insets(5,5,5,5));
    taskOutput.setEditable(false);

    JPanel panel = new JPanel();
    panel.add(startButton);
    panel.add(progressBar);

    add(panel, BorderLayout.PAGE_START);
    add(new JScrollPane(taskOutput), BorderLayout.CENTER);
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

}

/**
 * Invoked when the user presses the start button.
 */
public void actionPerformed(ActionEvent evt) {
    startButton.setEnabled(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    //Instances of javax.swing.SwingWorker are not reusuable, so
    //we create new instances as needed.
    task = new Task();
    task.addPropertyChangeListener(this);
    task.execute();
}

/**
 * Invoked when task's progress property changes.
 */
public void propertyChange(PropertyChangeEvent evt) {
    if ("progress" == evt.getPropertyName()) {
        int progress = (Integer) evt.getNewValue();
        progressBar.setValue(progress);
        taskOutput.append(String.format(
                "Completed %d%% of task.\n", task.getProgress()));
    } 
}


/**
 * Create the GUI and show it. As with all GUI code, this must run
 * on the event-dispatching thread.
 */
private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("ProgressBarDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    JComponent newContentPane = new PbTest();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
}

public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            createAndShowGUI();
         }
      });
    }
 }
