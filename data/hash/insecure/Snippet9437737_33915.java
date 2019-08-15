package com.krathsilvercloud.app;

import java.net.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import android.util.Log;
import android.widget.Toast;

public class Discoverer extends Thread {
private static final String TAG = "Discovery";
private static final String REMOTE_KEY = "b0xeeRem0tE!";
private static final int DISCOVERY_PORT = 1989;
private static final int TIMEOUT_MS = 5000;
private InetAddress addr;


private static final String mChallenge = "wuffwuff";
private WifiManager mWifi;

interface DiscoveryReceiver {
void addAnnouncedServers(InetAddress[] host, int port[]);
}

Discoverer(WifiManager wifi) {
mWifi = wifi;
try {
    addr = getBroadcastAddress();
} catch (IOException e) {
    Log.e(TAG, "Could not get bind address", e);
}
}

public String DiscoverRun() {
try {


    DatagramSocket socket = new DatagramSocket(DISCOVERY_PORT);
  socket.setBroadcast(true);
  socket.setSoTimeout(TIMEOUT_MS);


  sendDiscoveryRequest(socket);
  return listenForResponses(socket);

} catch (IOException e) {
  Log.e(TAG, "Could not send discovery request", e);
  return null;
}
}


private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {
String data = String.format(mChallenge);
Log.d(TAG, "Sending data " + data);

DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
    addr, DISCOVERY_PORT);
socket.send(packet);
}


private InetAddress getBroadcastAddress() throws IOException {
DhcpInfo dhcp = mWifi.getDhcpInfo();
if (dhcp == null) {
  Log.d(TAG, "Could not get dhcp info");
  return null;
}

int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
byte[] quads = new byte[4];
for (int k = 0; k < 4; k++)
  quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
return InetAddress.getByAddress(quads);
}


private String listenForResponses(DatagramSocket socket) throws IOException {
byte[] buf = new byte[1024];
try {
  while (true) {
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);
    Log.d(TAG, packet.getAddress().getHostAddress());
    String s = new String(packet.getData(), 0, packet.getLength());
    Log.d(TAG, "Received response " + s);


     String IPAddress2 = new String(packet.getAddress().getHostAddress());

    return IPAddress2;

  }
} catch (SocketTimeoutException e) {
  Log.d(TAG, "Receive timed out");
  return null;
}
}


private String getSignature(String challenge) {
MessageDigest digest;
byte[] md5sum = null;
try {
  digest = java.security.MessageDigest.getInstance("MD5");
  digest.update(challenge.getBytes());
  digest.update(REMOTE_KEY.getBytes());
  md5sum = digest.digest();
} catch (NoSuchAlgorithmException e) {
  e.printStackTrace();
}

StringBuffer hexString = new StringBuffer();
for (int k = 0; k < md5sum.length; ++k) {
  String s = Integer.toHexString((int) md5sum[k] & 0xFF);
  if (s.length() == 1)
    hexString.append('0');
  hexString.append(s);
}
return hexString.toString();
}

public static void main(String[] args) {
new Discoverer(null).start();
while (true) {
}
}
}
