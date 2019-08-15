  HResult=-2146233088

  Message=java.security.NoSuchAlgorithmException: class configured for MessageDigest(provider: SUN)cannot be found.

  Message (Exception)=java.security.NoSuchAlgorithmException: class configured for MessageDigest(provider: SUN)cannot be found.

  Source=pdfbox-1.8.7

  Source (Exception)=pdfbox-1.8.7

  StackTrace:

       bei org.apache.pdfbox.pdfwriter.COSWriter.write(PDDocument doc)

       bei org.apache.pdfbox.pdmodel.PDDocument.save(OutputStream output)

       bei org.apache.pdfbox.pdmodel.PDDocument.save(File file)

       bei org.apache.pdfbox.pdmodel.PDDocument.save(String fileName)

       bei PDFAnfertigen.Module1.TextAusPDFAuslesen(String pPDFPfad) in C:\Users\fengels\Documents\Visual Studio 2013\Projects\PDFAnfertigen\PDFAnfertigen\Module1.vb:Zeile 44.

       bei PDFAnfertigen.Module1.Main() in C:\Users\fengels\Documents\Visual Studio 2013\Projects\PDFAnfertigen\PDFAnfertigen\Module1.vb:Zeile 12.

       bei System.AppDomain._nExecuteAssembly(RuntimeAssembly assembly, String[] args)

       bei System.AppDomain.ExecuteAssembly(String assemblyFile, Evidence assemblySecurity, String[] args)

       bei Microsoft.VisualStudio.HostingProcess.HostProc.RunUsersAssembly()

       bei System.Threading.ThreadHelper.ThreadStart_Context(Object state)

       bei System.Threading.ExecutionContext.RunInternal(ExecutionContext executionContext, ContextCallback callback, Object state, Boolean preserveSyncCtx)

       bei System.Threading.ExecutionContext.Run(ExecutionContext executionContext, ContextCallback callback, Object state, Boolean preserveSyncCtx)

       bei System.Threading.ExecutionContext.Run(ExecutionContext executionContext, ContextCallback callback, Object state)

       bei System.Threading.ThreadHelper.ThreadStart()

  InnerException: java.security.NoSuchAlgorithmException

       HResult=-2146233088

       Message=class configured for MessageDigest(provider: SUN)cannot be found.

       Message (Exception)=class configured for MessageDigest(provider: SUN)cannot be found.

       Source=IKVM.OpenJDK.Core

       Source (Exception)=IKVM.OpenJDK.Core

       StackTrace:

            bei sun.security.jca.GetInstance.getInstance(String type, Class clazz, String algorithm)

            bei java.security.Security.getImpl(String , String , String )

            bei java.security.MessageDigest.getInstance(String algorithm)

            bei org.apache.pdfbox.pdfwriter.COSWriter.write(PDDocument doc)

       InnerException: java.lang.ClassNotFoundException

            HResult=-2146233088

            Message=sun.security.provider.MD5

            Message (Exception)=sun.security.provider.MD5

            Source=IKVM.Runtime

            Source (Exception)=IKVM.Runtime

            StackTrace:

                 bei IKVM.NativeCode.java.lang.Class.forName0(String name, Boolean initialize, ClassLoader loader)

                 bei java.lang.Class.forName0(String , Boolean , ClassLoader )

                 bei java.lang.Class.forName(String className, CallerID )

                 bei java.security.Provider.Service.getImplClass()

            InnerException: 
