@Path("/upload")
public class ImageTransferHandler {

@POST
@Path("/jsonString")
@Consumes("application/json")
public Response upload (ImageData jsonString){

    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Image.class, new ImageDeserializer());

    Gson gson = builder.create();

    //Image image = gson.fromJson(jsonString, Image.class);
    ImageData image = jsonString;

    System.out.println("sys out : Name: " + image.getImageName() + " \nEncoded Image String : " + image.getEncodedImageString());

    // Decode the base64 String and get the byte array
    byte[] imageBytes = image.getEncodedImageString().getBytes();

    byte[] decodedBytes = Base64.decodeBase64(imageBytes);

    try {

        // Convert the bytes to an image and store
        ImageConverter ic = new ImageConverter();

        ic.bytesToImage(decodedBytes, "C:\\Users\\yomal.ds\\Desktop\\test\\output.jpg");

    } catch (Exception e) {
        // TODO Auto-generated catch block

        e.printStackTrace();
    }

    try{
        /**
         * Checksum Code Start
         * */
        MessageDigest md1 = MessageDigest.getInstance("SHA-256");
        md1.update(imageBytes);
        byte[] mdbytes1 = md1.digest();

        StringBuffer hexedHashB64 = new StringBuffer();

        for (int i = 0; i < mdbytes1.length; i++) {
            hexedHashB64.append(Integer.toString((mdbytes1[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("HEXED HASH (Base64) : " + hexedHashB64.toString());
        /**
         * Checksum Code End
         * */


        /**
         * Checksum Code Start
         * */
        MessageDigest md2 = MessageDigest.getInstance("SHA-256");
        md2.update(decodedBytes);
        byte[] mdbytes2 = md2.digest();

        StringBuffer hexedHash = new StringBuffer();

        for (int i = 0; i < mdbytes2.length; i++) {
            hexedHash.append(Integer.toString((mdbytes2[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("HEXED HASH (Original) : " + hexedHash.toString());
        /**
         * Checksum Code End
         * */

    } catch (NoSuchAlgorithmException e) {
        System.err.println("Algorithm is not correct");
        e.printStackTrace();
    }
    return Response.status(200).entity(image.toString()).build();
}   

}
