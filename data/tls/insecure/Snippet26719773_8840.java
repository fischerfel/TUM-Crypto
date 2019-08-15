FBConsoleChatApp.java


 package com.fb.xmppchat.app;

import android.util.Log;
import android.widget.Toast;

import com.fb.xmppchat.helper.CustomSASLDigestMD5Mechanism;
import com.fb.xmppchat.helper.FBMessageListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

import javax.net.ssl.SSLContext;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public class FBConsoleChatApp {

public static final String FB_XMPP_HOST = "chat.facebook.com";
public static final int FB_XMPP_PORT = 5222;

private ConnectionConfiguration config;
 private XMPPConnection connection;
private BidiMap friends = new DualHashBidiMap();
private FBMessageListener fbml;

public void connect() throws XMPPException {
   ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com", 5222);
     config.setSASLAuthenticationEnabled(true);
     config.setSecurityMode(SecurityMode.required);
     config.setRosterLoadedAtLogin(true);
     config.setTruststorePath("/system/etc/security/cacerts.bks");
     config.setTruststorePassword("changeit");
     config.setTruststoreType("bks");
     config.setSendPresence(false);
     try {
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init(null, MemorizingTrustManager.getInstanceList(this),
           new java.security.SecureRandom());
         config.setCustomSSLContext(sc);
     } catch (GeneralSecurityException e) {
         Log.w("TAG", "Unable to use MemorizingTrustManager", e);
     }
     XMPPConnection xmpp = new XMPPConnection(config);
     try {
         xmpp.connect();
         xmpp.login("facebookusername", "****"); // Here you have to used only
         facebookusername from facebookusername@chat.facebook.com
         Roster roster = xmpp.getRoster();
         Collection<RosterEntry> entries = roster.getEntries();
         System.out.println("Connected!");
         System.out.println("\n\n" + entries.size() + " buddy(ies):");
         // shows first time onliners---->
         String temp[] = new String[50];
         int i = 0;
         for (RosterEntry entry : entries) {
             String user = entry.getUser();
             Log.i("TAG", user);
         }
     } catch (XMPPException e) {
         xmpp.disconnect();
         e.printStackTrace();
     }

     }

   public void disconnect() {
  if ((connection != null) && (connection.isConnected())) {
     Presence presence = new Presence(Presence.Type.unavailable);
     presence.setStatus("offline");
     connection.disconnect(presence);
      }
   }

 public boolean login(String userName, String password) 
  throws XMPPException {
  if ((connection != null) && (connection.isConnected())) {
     connection.login(userName, password);
     return true;
     }
   return false;
  }

  public String readInput() throws IOException {
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  return br.readLine();
    }

  public void showMenu() {
  System.out.println("Please select one of the following menu.");
  System.out.println("1. List of Friends online");
  System.out.println("2. Send Message");
  System.out.println("3. EXIT");
  System.out.print("Your choice [1-3]: ");
   }

  public void getFriends() {
  if ((connection != null) && (connection.isConnected())) {
     Roster roster = connection.getRoster();
     int i = 1;
     for (RosterEntry entry : roster.getEntries()) {
        Presence presence = roster.getPresence(entry.getUser());
        if ((presence != null) 
           && (presence.getType() != Presence.Type.unavailable)) {
           friends.put("#" + i, entry);
           System.out.println(entry.getName() + "(#" + i + ")");
           i++;
        }
     }
     fbml.setFriends(friends);
      }
   }

  public void sendMessage() throws XMPPException
 , IOException {
  System.out.println("Type the key number of your friend (e.g. #1)
  and the text that you wish to    send !");
  String friendKey = null;
  String text = null;
  System.out.print("Your friend's Key Number: ");
  friendKey = readInput();
  System.out.print("Your Text message: ");
  text = readInput();
  sendMessage((RosterEntry) friends.get(friendKey), text);
  }

  public void sendMessage(final RosterEntry friend, String text) 
 throws XMPPException {
  if ((connection != null) && (connection.isConnected())) {
     ChatManager chatManager = connection.getChatManager();
     Chat chat = chatManager.createChat(friend.getUser(), fbml);
     chat.sendMessage(text);
     System.out.println("Your message has been sent to "
        + friend.getName());
  }
 }

public static void main(String[] args) {
  if (args.length == 0) {
    System.err.println("Usage: java FBConsoleChatApp [username_facebook] [password]");
    System.exit(-1);
  }

  String username = args[0];
  String password = args[1];

  FBConsoleChatApp app = new FBConsoleChatApp();

  try {
     app.connect();
     if (!app.login(username, password)) {
        System.err.println("Access Denied...");
        System.exit(-2);
     }
     app.showMenu();
     String data = null;
     menu:
     while((data = app.readInput().trim()) != null) {
        if (!Character.isDigit(data.charAt(0))) {
           System.out.println("Invalid input.Only 1-3 is allowed !");
           app.showMenu();
           continue;
        }
        int choice = Integer.parseInt(data);
        if ((choice != 1) && (choice != 2) && (choice != 3)) {
           System.out.println("Invalid input.Only 1-3 is allowed !");
           app.showMenu();
           continue;
        }
        switch (choice) {
           case 1: app.getFriends();
                   app.showMenu();
                   continue menu;
           case 2: app.sendMessage();
                   app.showMenu();
                   continue menu;
           default: break menu;
        }
     }
     app.disconnect();
  } catch (XMPPException e) {
    if (e.getXMPPError() != null) {
       System.err.println("ERROR-CODE : " + e.getXMPPError().getCode());
       System.err.println("ERROR-CONDITION : " + e.getXMPPError().getCondition());
       System.err.println("ERROR-MESSAGE : " + e.getXMPPError().getMessage());
       System.err.println("ERROR-TYPE : " + e.getXMPPError().getType());
    }
    app.disconnect();
  } catch (IOException e) {
    System.err.println(e.getMessage());
    app.disconnect();
  }
  }
} 


FBMessageListener.java

package com.fb.xmppchat.helper;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.MapIterator;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

public class FBMessageListener implements MessageListener, Runnable {

   private FBMessageListener fbml = this;
  private XMPPConnection conn;
  private BidiMap friends;

  public FBMessageListener(XMPPConnection conn) {
  this.conn = conn;
  new Thread(this).start();
   }

   public void setFriends(BidiMap friends) {
  this.friends = friends;
   }

   public void processMessage(Chat chat, Message message) {
   System.out.println();
   MapIterator it = friends.mapIterator();
   String key = null;
   RosterEntry entry = null;
   while (it.hasNext()) {
     key = (String) it.next();
     entry = (RosterEntry) it.getValue();
     if (entry.getUser().equalsIgnoreCase(chat.getParticipant())) {
        break;
     }
   }
   if ((message != null) && (message.getBody() != null)) {
     System.out.println("You've got new message from " + entry.getName() 
        + "(" + key + ") :");
     System.out.println(message.getBody());
     System.out.print("Your choice [1-3]: ");
   }
  }

  public void run() {
   conn.getChatManager().addChatListener(
     new ChatManagerListener() {
        public void chatCreated(Chat chat, boolean createdLocally) {
           if (!createdLocally) {
              chat.addMessageListener(fbml);
           }
        }
     }
   );
  }
}


CustomSASLDigestMD5Mechanism.java



package com.fb.xmppchat.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
  import org.jivesoftware.smack.sasl.SASLMechanism;

 public class CustomSASLDigestMD5Mechanism extends SASLMechanism {

   public CustomSASLDigestMD5Mechanism(SASLAuthentication saslAuthentication) {
  super(saslAuthentication);
  }

   @Override
   public void authenticate(String username, String host, String password)
   throws IOException, XMPPException {
   this.authenticationId = username;
   this.password = password;
   this.hostname = host;

  String[] mechanisms = { getName() };
  Map<String, String> props = new HashMap<String, String>();
  sc = de.measite.smack.Sasl.createSaslClient(mechanisms, null, "xmpp", host, props, this);
  super.authenticate();
  }


   public void authenticate(String username, String host, org.apache.harmony.javax.
security.auth.callback.CallbackHandler cbh)
 throws IOException, XMPPException {
  String[] mechanisms = { getName() };
  Map<String, String> props = new HashMap<String, String>();
  sc = org.apache.harmony.javax.security.sasl.Sasl.createSaslClient(mechanisms, null, "xmpp",  
     host, props, cbh);
  super.authenticate();
  }

 protected String getName() {
 return "DIGEST-MD5";
 }
}
