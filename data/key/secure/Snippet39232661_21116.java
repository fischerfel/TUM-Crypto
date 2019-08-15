import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.InvalidKeyException
import java.security.MessageDigest

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.Header
import groovy.json.JsonSlurper

import java.text.SimpleDateFormat

def method = 'GET'
def service = 'ec2'
def host = 'ec2.amazonaws.com'
def region = 'us-east-1'
def endpoint = 'https://ec2.amazonaws.com'
def request_parameters = 'Action=DescribeRegions&Version=2013-10-15'

def hmac_sha256(byte[] secretKey, String data) {
 try {
    Mac mac = Mac.getInstance("HmacSHA256")
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256")
    mac.init(secretKeySpec)
    byte[] digest = mac.doFinal(data.getBytes())
    return digest
   } catch (InvalidKeyException e) {
    throw new RuntimeException("Invalid key exception while converting to HMac SHA256")
  }
}
def hmac_sha256Hex(byte[] secretKey, String data) {
    def result = hmac_sha256(secretKey, data)
    return result.encodeHex()
}
def getSignatureKey(key, dateStamp, regionName, serviceName){
    def kDate = hmac_sha256(('AWS4' + key).getBytes(), dateStamp)
    def kRegion = hmac_sha256(kDate, regionName)
    def kService = hmac_sha256(kRegion, serviceName)
    def kSigning = hmac_sha256(kService, 'aws4_request')
    return kSigning
}
def getHexDigest(text){
    def md = MessageDigest.getInstance("SHA-256")
    md.update(text.getBytes())
    return md.digest().encodeHex()
}

def access_key = 'Access Key'
def secret_key = 'Secret Access Key'
def now = new Date()
def amzFormat = new SimpleDateFormat( "yyyyMMdd'T'HHmmss'Z'" )
def stampFormat = new SimpleDateFormat( "yyyyMMdd" )
def amzDate = amzFormat.format(now)
def dateStamp = stampFormat.format(now)

def canonical_uri = '/' 
def canonical_querystring = request_parameters
def canonical_headers = 'host:' + host + '\n' + 'x-amz-date:' + amzDate + '\n'
def signed_headers = 'host;x-amz-date'

def payload_hash = getHexDigest("")
def canonical_request = method + '\n' + canonical_uri + '\n' + canonical_querystring + '\n' + canonical_headers + '\n' + signed_headers + '\n' + payload_hash

def algorithm = 'AWS4-HMAC-SHA256'
def credential_scope = dateStamp + '/' + region + '/' + service + '/' + 'aws4_request'
def hash_canonical_request = getHexDigest(canonical_request)
def string_to_sign = algorithm + '\n' +  amzDate + '\n' +  credential_scope + '\n' +  hash_canonical_request

def signing_key = getSignatureKey(secret_key, dateStamp, region, service)

def signature = hmac_sha256Hex(signing_key, string_to_sign)
def authorization_header = algorithm + ' ' + 'Credential=' + access_key + '/' + credential_scope + ', ' +  'SignedHeaders=' + signed_headers + ', ' + 'Signature=' + signature
def request_url = endpoint + '?' + canonical_querystring

def httpClient = new HttpClient()
def get = new GetMethod('https://xxxxx.execute-api.us-east-1.amazonaws.com/Method/id000/')
get.setRequestHeader(new Header("x-amz-date", amzDate))
get.setRequestHeader(new Header("Authorization", authorization_header))

int statusCode = httpClient.executeMethod(get)

if(statusCode >= 200 && statusCode < 300){ 
    def slurper = new JsonSlurper() 
    def response = slurper.parseText(get.getResponseBodyAsString()) 
    logger.debug response
    logger.debug response?.Id
}else{
    logger.debug statusCode
}
