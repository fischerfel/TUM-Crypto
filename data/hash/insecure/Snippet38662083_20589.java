static void sendPart(existingBucketName, keyName, multipartRepsonse, partNum,
                     sendBuffer, partSize, vertx, partETags, s3, req, resultClosure)
{

    // Create request to upload a part.
    MessageDigest md = MessageDigest.getInstance("MD5")
    byte[] digest = md.digest(sendBuffer.bytes)
    InputStream inputStream = new ByteArrayInputStream(sendBuffer.bytes)
    UploadPartRequest uploadRequest = new UploadPartRequest()
        .withBucketName(existingBucketName).withKey(keyName)
        .withUploadId(multipartRepsonse.getUploadId()).withPartNumber(partNum)
        .withInputStream(inputStream)
        .withMD5Digest(Base64.getEncoder().encodeToString(digest))
        .withPartSize(partSize)

    // Upload part and add response to our list.
    vertx.executeBlocking({ future ->

            try {
                println("Sending chunk for ${keyName}")
                PartETag eTag = s3.uploadPart(uploadRequest).getPartETag()
                partETags.add(eTag);
                req.response().write("Sending Chunk\n")
            } catch(Exception e) {
            }

            def result = "success!"

            future.complete(result)
        }, resultClosure)
}
