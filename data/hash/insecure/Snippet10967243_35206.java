import java.security.MessageDigest
import com.microsoft.windowsazure.services.core.storage.utils.Base64;
import com.google.common.io.Files

String putFile(String remoteFolder, String filePath){
    File fileReference = new File (filePath)
    // the user is already authentificated and the container is not null
    CloudBlockBlob blob = container.getBlockBlobReference(remoteFolderName+"/"+filePath);
    FileInputStream fis = new FileInputStream(fileReference)
    if(blob){
        BlobProperties props = blob.getProperties()

        MessageDigest md5digest = MessageDigest.getInstance("MD5")
        String md5 = Base64.encode(Files.getDigest(fileReference, md5digest))

        props.setContentMD5(md5)
        blob.setProperties(props)
        blob.upload(fis, fileReference.length())
        return fileReference.getName()
   }else{
        //ErrorHandling
        return ""
   }
}
