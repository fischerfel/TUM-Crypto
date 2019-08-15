/// <summary>
/// 读取指定文件块数据Sha1
/// </summary>
/// <param name="fis">
/// @return </param>
private static MessageDigest calSha1(BufferedInputStream fis) {
    MessageDigest sha1 = null;
    try {
        byte[] buffer = new byte[1024];
        int numRead = 0;
        int total = 0;
        sha1 = MessageDigest.getInstance("SHA-1");
        while ((numRead = fis.read(buffer)) > 0) {
            sha1.update(buffer, 0, numRead);
            total += numRead;
            if (total >= BLOCK_SIZE) {
                break;
            }
        }
    } catch (Exception e) {

}
