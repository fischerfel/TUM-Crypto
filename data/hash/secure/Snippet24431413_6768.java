Exception in thread "AWT-EventQueue-0" java.security.ProviderException: Error parsing configuration
at sun.security.pkcs11.Config.getConfig(Config.java:71)
at sun.security.pkcs11.SunPKCS11.<init>(SunPKCS11.java:110)
at sun.security.pkcs11.SunPKCS11.<init>(SunPKCS11.java:86)
at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
at sun.security.jca.ProviderConfig$4.run(ProviderConfig.java:262)
at java.security.AccessController.doPrivileged(Native Method)
at sun.security.jca.ProviderConfig.doLoadProvider(ProviderConfig.java:244)
at sun.security.jca.ProviderConfig.getProvider(ProviderConfig.java:224)
at sun.security.jca.ProviderList.getProvider(ProviderList.java:215)
at sun.security.jca.ProviderList.getService(ProviderList.java:313)
at sun.security.jca.GetInstance.getInstance(GetInstance.java:140)
at java.security.Security.getImpl(Security.java:659)
at java.security.MessageDigest.getInstance(MessageDigest.java:129)
at java.io.ObjectStreamClass.computeDefaultSUID(ObjectStreamClass.java:1771)
at java.io.ObjectStreamClass.access$100(ObjectStreamClass.java:51)
at java.io.ObjectStreamClass$1.run(ObjectStreamClass.java:204)
at java.security.AccessController.doPrivileged(Native Method)
at java.io.ObjectStreamClass.getSerialVersionUID(ObjectStreamClass.java:201)
at java.io.ObjectStreamClass.writeNonProxy(ObjectStreamClass.java:675)
at java.io.ObjectOutputStream.writeClassDescriptor(ObjectOutputStream.java:649)
at java.io.ObjectOutputStream.writeNonProxyDesc(ObjectOutputStream.java:1263)
at java.io.ObjectOutputStream.writeClassDesc(ObjectOutputStream.java:1212)
at java.io.ObjectOutputStream.writeNonProxyDesc(ObjectOutputStream.java:1275)
at java.io.ObjectOutputStream.writeClassDesc(ObjectOutputStream.java:1212)
at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1408)
at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1159)
at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1535)
at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1496)
at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1413)
at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1159)
at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:329)
at chess.ChessGame.saveGame(ChessGame.java:256)
at chess.ChessGame.access$2(ChessGame.java:239)
at chess.ChessGame$3.actionPerformed(ChessGame.java:133)
at javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2028)
at javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2351)
at javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:387)
at javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:242)
at javax.swing.AbstractButton.doClick(AbstractButton.java:389)
at javax.swing.plaf.basic.BasicMenuItemUI.doClick(BasicMenuItemUI.java:809)
at com.apple.laf.AquaMenuItemUI.doClick(AquaMenuItemUI.java:137)
at javax.swing.plaf.basic.BasicMenuItemUI$Handler.mouseReleased(BasicMenuItemUI.java:850)
at java.awt.Component.processMouseEvent(Component.java:6414)
at javax.swing.JComponent.processMouseEvent(JComponent.java:3275)
at java.awt.Component.processEvent(Component.java:6179)
at java.awt.Container.processEvent(Container.java:2084)
at java.awt.Component.dispatchEventImpl(Component.java:4776)
at java.awt.Container.dispatchEventImpl(Container.java:2142)
at java.awt.Component.dispatchEvent(Component.java:4604)
at java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4618)
at java.awt.LightweightDispatcher.processMouseEvent(Container.java:4279)
at java.awt.LightweightDispatcher.dispatchEvent(Container.java:4209)
at java.awt.Container.dispatchEventImpl(Container.java:2128)
at java.awt.Window.dispatchEventImpl(Window.java:2492)
at java.awt.Component.dispatchEvent(Component.java:4604)
at java.awt.EventQueue.dispatchEventImpl(EventQueue.java:717)
at java.awt.EventQueue.access$400(EventQueue.java:82)
at java.awt.EventQueue$2.run(EventQueue.java:676)
at java.awt.EventQueue$2.run(EventQueue.java:674)
at java.security.AccessController.doPrivileged(Native Method)
at java.security.AccessControlContext$1.doIntersectionPrivilege(AccessControlContext.java:86)
at java.security.AccessControlContext$1.doIntersectionPrivilege(AccessControlContext.java:97)
at java.awt.EventQueue$3.run(EventQueue.java:690)
at java.awt.EventQueue$3.run(EventQueue.java:688)
at java.security.AccessController.doPrivileged(Native Method)
at java.security.AccessControlContext$1.doIntersectionPrivilege(AccessControlContext.java:86)
at java.awt.EventQueue.dispatchEvent(EventQueue.java:687)
at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:296)
at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:211)
at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:201)
at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:196)
at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:188)
at java.awt.EventDispatchThread.run(EventDispatchThread.java:122)
Caused by: sun.security.pkcs11.ConfigurationException: Unknown keyword '⌊⌠䍯湦楧畲慴楯渠晩汥⁴漠慬汯眠瑨攠卵湐䭃匱ㄠ灲潶楤敲⁴漠畴楬楺攊⌠瑨攠卭慲瑃慲摓敲癩捥猠慮搠潵爠捲祰瑯杲慰桩挠晲慭敷潲欬⁩映楴⁩猠慶慩污扬攊⌊੮慭攠㴠䑡牷楮ਊ摥獣物灴楯渠㴠卵湐䭃匱ㄠ慣捥獳楮朠䵡挠体⁘⁓浡牴䍡牤卥牶楣敳ਊ汩扲慲礠㴠⽵獲⽬楢數散⽓浡牴䍡牤卥牶楣敳⽰正猱ㄯ瑯步湤偋䍓ㄱ⹳漊੨慮摬敓瑡牴異䕲牯牳‽⁩杮潲敁汬ਊ慴瑲楢畴敳‽⁣潭灡瑩扩汩瑹�', line 1
    at sun.security.pkcs11.Config.parse(Config.java:425)
    at sun.security.pkcs11.Config.<init>(Config.java:194)
    at sun.security.pkcs11.Config.getConfig(Config.java:67)
    ... 75 more
