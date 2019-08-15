public class PdfSignatureContainerExt implements IExternalSignatureContainer {

private MySignUtil mySignUtil;
public PdfSignatureContainerExt(MySignUtil mySignUtil){
    this.mySignUtil= mySignUtil;
}


@Override
public byte[] sign(InputStream data) throws GeneralSecurityException {

        byte[] dataBytes = streamToBytes(data);
        //change here
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA1" , "BC");
        byte[] hash = messageDigest.digest(dataBytes);

        return  mySignUtil.signP7DetachData(hash);      
}

@Override
public void modifySigningDictionary(PdfDictionary signDic) {
    signDic.put(PdfName.Filter, PdfName.Adobe_PPKLite);
    //change here
    signDic.put(PdfName.SubFilter, PdfName.Adbe_pkcs7_sha1);
}}
