Error connecting to database [XXXX.com] : org.pentaho.di.core.exception.KettleDatabaseException: 
Error occured while trying to connect to the database

Error connecting to database: (using class org.postgresql.Driver)
The connection attempt failed.


org.pentaho.di.core.exception.KettleDatabaseException: 
Error occured while trying to connect to the database

Error connecting to database: (using class org.postgresql.Driver)
The connection attempt failed.


at org.pentaho.di.core.database.Database.normalConnect(Database.java:374)
at org.pentaho.di.core.database.Database.connect(Database.java:323)
at org.pentaho.di.core.database.Database.connect(Database.java:285)
at org.pentaho.di.core.database.Database.connect(Database.java:275)
at org.pentaho.di.core.database.DatabaseFactory.getConnectionTestReport(DatabaseFactory.java:76)
at org.pentaho.di.core.database.DatabaseMeta.testConnection(DatabaseMeta.java:2455)
at org.pentaho.ui.database.event.DataHandler.testDatabaseConnection(DataHandler.java:511)
at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
at java.lang.reflect.Method.invoke(Unknown Source)
at org.pentaho.ui.xul.impl.AbstractXulDomContainer.invoke(AbstractXulDomContainer.java:329)
at org.pentaho.ui.xul.impl.AbstractXulComponent.invoke(AbstractXulComponent.java:139)
at org.pentaho.ui.xul.impl.AbstractXulComponent.invoke(AbstractXulComponent.java:123)
at org.pentaho.ui.xul.swt.tags.SwtButton.access$500(SwtButton.java:26)
at org.pentaho.ui.xul.swt.tags.SwtButton$4.widgetSelected(SwtButton.java:119)
at org.eclipse.swt.widgets.TypedListener.handleEvent(Unknown Source)
at org.eclipse.swt.widgets.EventTable.sendEvent(Unknown Source)
at org.eclipse.swt.widgets.Widget.sendEvent(Unknown Source)
at org.eclipse.swt.widgets.Display.runDeferredEvents(Unknown Source)
at org.eclipse.swt.widgets.Display.readAndDispatch(Unknown Source)
at org.eclipse.jface.window.Window.runEventLoop(Window.java:820)
at org.eclipse.jface.window.Window.open(Window.java:796)
at org.pentaho.ui.xul.swt.tags.SwtDialog.show(SwtDialog.java:378)
at org.pentaho.ui.xul.swt.tags.SwtDialog.show(SwtDialog.java:304)
at org.pentaho.di.ui.core.database.dialog.XulDatabaseDialog.open(XulDatabaseDialog.java:104)
at org.pentaho.di.ui.core.database.dialog.DatabaseDialog.open(DatabaseDialog.java:51)
at org.pentaho.di.ui.trans.step.BaseStepDialog$3.widgetSelected(BaseStepDialog.java:480)
at org.eclipse.swt.widgets.TypedListener.handleEvent(Unknown Source)
at org.eclipse.swt.widgets.EventTable.sendEvent(Unknown Source)
at org.eclipse.swt.widgets.Widget.sendEvent(Unknown Source)
at org.eclipse.swt.widgets.Display.runDeferredEvents(Unknown Source)
at org.eclipse.swt.widgets.Display.readAndDispatch(Unknown Source)
at org.pentaho.di.ui.trans.steps.tableoutput.TableOutputDialog.open(TableOutputDialog.java:916)
at org.pentaho.di.ui.spoon.delegates.SpoonStepsDelegate.editStep(SpoonStepsDelegate.java:126)
at org.pentaho.di.ui.spoon.Spoon.editStep(Spoon.java:7733)
at org.pentaho.di.ui.spoon.trans.TransGraph.editStep(TransGraph.java:2744)
at org.pentaho.di.ui.spoon.trans.TransGraph.mouseDoubleClick(TransGraph.java:693)
at org.eclipse.swt.widgets.TypedListener.handleEvent(Unknown Source)
at org.eclipse.swt.widgets.EventTable.sendEvent(Unknown Source)
at org.eclipse.swt.widgets.Widget.sendEvent(Unknown Source)
at org.eclipse.swt.widgets.Display.runDeferredEvents(Unknown Source)
at org.eclipse.swt.widgets.Display.readAndDispatch(Unknown Source)
at org.pentaho.di.ui.spoon.Spoon.readAndDispatch(Spoon.java:1169)
at org.pentaho.di.ui.spoon.Spoon.start(Spoon.java:6945)
at org.pentaho.di.ui.spoon.Spoon.main(Spoon.java:553)
at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
at java.lang.reflect.Method.invoke(Unknown Source)
at org.pentaho.commons.launcher.Launcher.main(Launcher.java:134)
Caused by: org.pentaho.di.core.exception.KettleDatabaseException: 
Error connecting to database: (using class org.postgresql.Driver)
The connection attempt failed.

at org.pentaho.di.core.database.Database.connectUsingClass(Database.java:510)
at org.pentaho.di.core.database.Database.normalConnect(Database.java:358)
... 50 more
Caused by: org.postgresql.util.PSQLException: The connection attempt failed.
at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:152)
at org.postgresql.core.ConnectionFactory.openConnection(ConnectionFactory.java:66)
at org.postgresql.jdbc2.AbstractJdbc2Connection.<init>(AbstractJdbc2Connection.java:125)
at org.postgresql.jdbc3.AbstractJdbc3Connection.<init>(AbstractJdbc3Connection.java:30)
at org.postgresql.jdbc3g.AbstractJdbc3gConnection.<init>(AbstractJdbc3gConnection.java:22)
at org.postgresql.jdbc4.AbstractJdbc4Connection.<init>(AbstractJdbc4Connection.java:32)
at org.postgresql.jdbc4.Jdbc4Connection.<init>(Jdbc4Connection.java:24)
at org.postgresql.Driver.makeConnection(Driver.java:393)
at org.postgresql.Driver.connect(Driver.java:267)
at java.sql.DriverManager.getConnection(Unknown Source)
at java.sql.DriverManager.getConnection(Unknown Source)
at org.pentaho.di.core.database.Database.connectUsingClass(Database.java:490)
... 51 more
Caused by: javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException:     PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
at com.sun.net.ssl.internal.ssl.Alerts.getSSLException(Unknown Source)
at com.sun.net.ssl.internal.ssl.SSLSocketImpl.fatal(Unknown Source)
at com.sun.net.ssl.internal.ssl.Handshaker.fatalSE(Unknown Source)
at com.sun.net.ssl.internal.ssl.Handshaker.fatalSE(Unknown Source)
at com.sun.net.ssl.internal.ssl.ClientHandshaker.serverCertificate(Unknown Source)
at com.sun.net.ssl.internal.ssl.ClientHandshaker.processMessage(Unknown Source)
at com.sun.net.ssl.internal.ssl.Handshaker.processLoop(Unknown Source)
at com.sun.net.ssl.internal.ssl.Handshaker.process_record(Unknown Source)
at com.sun.net.ssl.internal.ssl.SSLSocketImpl.readRecord(Unknown Source)
at com.sun.net.ssl.internal.ssl.SSLSocketImpl.performInitialHandshake(Unknown Source)
at com.sun.net.ssl.internal.ssl.SSLSocketImpl.writeRecord(Unknown Source)
at com.sun.net.ssl.internal.ssl.AppOutputStream.write(Unknown Source)
at java.io.BufferedOutputStream.flushBuffer(Unknown Source)
at java.io.BufferedOutputStream.flush(Unknown Source)
at org.postgresql.core.PGStream.flush(PGStream.java:523)
at org.postgresql.core.v3.ConnectionFactoryImpl.sendStartupPacket(ConnectionFactoryImpl.java:259)
at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:105)
... 62 more
Caused by: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
at sun.security.validator.PKIXValidator.doBuild(Unknown Source)
at sun.security.validator.PKIXValidator.engineValidate(Unknown Source)
at sun.security.validator.Validator.validate(Unknown Source)
at com.sun.net.ssl.internal.ssl.X509TrustManagerImpl.validate(Unknown Source)
at com.sun.net.ssl.internal.ssl.X509TrustManagerImpl.checkServerTrusted(Unknown Source)
at com.sun.net.ssl.internal.ssl.X509TrustManagerImpl.checkServerTrusted(Unknown Source)
... 75 more
Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
at sun.security.provider.certpath.SunCertPathBuilder.engineBuild(Unknown Source)
at java.security.cert.CertPathBuilder.build(Unknown Source)
... 81 more

Hostname       : XXXX.com
Port           : 5432
Database name  : XXXX
