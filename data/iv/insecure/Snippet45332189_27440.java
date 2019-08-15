import java.security._
import javax.crypto._
import javax.crypto.spec._
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder

object Main {
  private val cryptographyKeySize: Int = 0
  private val cryptographyMethod: String = null
  private val kgen: KeyGenerator = null
  private val secretKeyFieldIndex: Int = 0
  private val myDecryptedSecretFieldIndex: Int = 0

  private val encryptedTextFieldIndex1: Int = 0
  private val encryptedTextFieldIndex2: Int = 0


  private val COL1Index1: Int = 0
  private val COL2Index1: Int = 0
  private val COL1Index2: Int = 0
  private val COL2Index2: Int = 0
  private val symmKey: String = "9d6ea4d3e6f8c4f8"
  private val ivSpec: String = "1c5dd32d7ba54bdd"

  def processRow(smi: StepMetaInterface, sdi: StepDataInterface): Boolean = {
    val r: Array[Any] = getRow()

    if (r == null) {
      setOutputDone()
      false
    } else {
        val outputRowData: Array[Any] = createOutputRow(r, data.outputRowMeta.size())

        try {
          val raw: Array[Byte] = symmKey.getBytes("UTF-8")
          val skeySpec: SecretKeySpec = new SecretKeySpec(raw, "AES")
          val cipher: Cipher = Cipher.getInstance("AES/CBC/ISO10126PADDING")
          val ivParameterSpec: IvParameterSpec = new IvParameterSpec(ivSpec.getBytes("UTF-8"))
          cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec)

          val ecordedValue: Array[Byte] = new BASE64Decoder().decodeBuffer(r[encryptedTextFieldIndex1].asInstanceOf[String])
          val decryped: Array[Byte] = cipher.doFinal(decordedValue)
          outputRowData[myDecryptedSecretFieldIndex] = new String(decrypted)
          outputRowData[COL1Index2] = r[COL1Index1]
          outputRowData[COL2Index2] = r[COL2Index1]
          outputRowData[encryptedTextFieldIndex2] = r[encryptedTextFieldIndex1]

          putRow(data.outputRowMeta, outputRowData)
        } catch {
          case t: Throwable => 
              val exMessage: String = "failed during decryption::" + r[encryptedTextFieldIndex1].asInstanceOf[String]
        }
      true
      }
    }
  }
}
