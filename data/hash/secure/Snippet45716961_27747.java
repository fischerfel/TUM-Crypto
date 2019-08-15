   Exception in thread "main" com.jscape.inet.sftp.SftpException: SHA256 MessageDigest not available
    at com.jscape.inet.sftp.SftpException.wrap(Unknown Source)
    at com.jscape.inet.sftp.Sftp.a(Unknown Source)
    at com.jscape.inet.sftp.Sftp.connect(Unknown Source)
    at com.bcs.renewals.Test.main(Test.java:19)
Caused by: com.jscape.util.o.b: SHA256 MessageDigest not available
    at com.jscape.util.o.b.a(Unknown Source)
    at com.jscape.util.o.c.a(Unknown Source)
    at com.jscape.inet.sftp.Sftp.b(Unknown Source)
    ... 2 more
Caused by: com.jscape.util.l.a.h: SHA256 MessageDigest not available
    at com.jscape.inet.ssh.protocol.v2.connection.SessionConnector.connect(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.connection.SessionConnector.connect(Unknown Source)
    at com.jscape.inet.sftp.SftpFileService3.actualStart(Unknown Source)
    ... 4 more
Caused by: com.jscape.util.l.a.b: SHA256 MessageDigest not available
    at com.jscape.util.l.a.b.a(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.transport.TransportConnection.a(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.transport.TransportConnection.exchangeKeys(Unknown Source)
    ... 7 more
Caused by: java.security.NoSuchAlgorithmException: SHA256 MessageDigest not available
    at sun.security.jca.GetInstance.getInstance(GetInstance.java:159)
    at java.security.Security.getImpl(Security.java:695)
    at java.security.MessageDigest.getInstance(MessageDigest.java:167)
    at com.jscape.a.f.a(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.transport.keyexchange.DiffieHellmanGroupKeyExchange.createHash(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.transport.keyexchange.DiffieHellmanGroupClientKeyExchange.exchangeKeys(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.transport.TransportConnection.handle(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.messages.SshMsgKexInit.accept(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.messages.SshMsgKexInit.accept(Unknown Source)
    at com.jscape.inet.ssh.protocol.v2.transport.TransportConnection.c(Unknown Source)
    ... 9 more
