object FileUtil {
fun getMD5(path: String): String {
    val file = File(path)
    val inStream = FileInputStream(file)

    return getMD5(inStream)
}

fun getMD5(inputStream: InputStream): String {
    val messagedigest: MessageDigest
    try {
        messagedigest = MessageDigest.getInstance("MD5")

        val buffer = ByteArray(1024 * 1024 * 1)
        var len = inputStream.read(buffer)

        while (len > 0) {
            //该对象通过使用 update（）方法处理数据
            messagedigest.update(buffer, 0, len)

            len = inputStream.read(buffer)
        }

        //对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
        return StringUtils.convertHashToString(messagedigest.digest())
    } catch (e: NoSuchAlgorithmException) {
        Log.e("Error", e.toString())
        e.printStackTrace()
    } catch (e: OutOfMemoryError) {
        Log.e("OutOfMemoryError", e.toString())
        e.printStackTrace()
        throw e
    } finally {
        inputStream.close()
    }
    return ""
}
