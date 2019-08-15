@Api(value = "uploadmultipart", basePath = "/services", description = "Multipart Consumption Service")
@Service("uploadMultipartService")
@Path("upload")
public class MultipartService extends AbstractService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipartService.class);

    @ApiOperation(httpMethod = "POST", value = "Post operation to upload a file and retrieve checksum.")
    @POST
    @Path("multipart")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonparam", value = "UploadMultipartRequest in json", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "attachmentparam", value = "an attachment for SHA1 Checksum", required = true, dataType = "File", paramType = "form")
    })
    public Response uploadMultipart(
            @ApiParam @Multipart(value = "jsonparam", type=MediaType.APPLICATION_JSON) UploadMultipartRequest jsondata,
            @ApiParam @Multipart(value = "attachmentparam", type=MediaType.APPLICATION_OCTET_STREAM) Attachment attachment,
            @Context HttpHeaders headers)
            throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchAlgorithmException, IOException {
        UploadMultipartResponse uploadMultipartResponse = new UploadMultipartResponse();

        uploadMultipartResponse.setUploaderName(jsondata.getUploaderName());
        uploadMultipartResponse.setFileChecksum(jsondata.getFileFormat());

        LOGGER.info("content-type" + attachment.getContentType());
        LOGGER.info("content-id" + attachment.getContentId());
        LOGGER.info("content-disposition" + attachment.getContentDisposition());

        MessageDigest md = MessageDigest.getInstance("SHA1");
        InputStream is = attachment.getObject(InputStream.class);

        StringBuffer sb = getSHA1Checksum(md, is);

        uploadMultipartResponse.setFileChecksum(sb.toString());

        return Response.status(Response.Status.OK).entity(uploadMultipartResponse).build();
    }
}
