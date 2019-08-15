import java.security.MessageDigest

def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config

    ws(getWsHash(config.length ?: 10)) {
        body()
    }
}

def getWsHash(length) {
    def md5Hash = MessageDigest.getInstance("MD5").digest("${env.JOB_NAME}_${env.EXECUTOR_NUMBER}".bytes).encodeHex().toString()
    return md5Hash.substring(0,10)
}
