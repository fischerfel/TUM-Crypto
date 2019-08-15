package com.terradatum.query

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import com.nitro.scalaAvro.runtime.GeneratedMessage
import com.terradatum.diagnostics.AkkaLogging
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import org.apache.avro.io.EncoderFactory
import org.elasticsearch.search.SearchHit

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/*
* The original implementation of this helper relied exclusively on using the Header Avro record and inception to create
* the header. That didn't work for us because somehow erroneous bytes were injected into the output.
*
* Specifically:
* 1. 0x08 prepended to the magic
* 2. 0x0020 between the header and the sync marker
*
* Rather than continue to spend a large number of hours trying to troubleshoot why the Avro library was producing such
* erroneous output, we build the Avro Container File using a combination of our own code and Avro library code.
*
* This means that Terradatum code is responsible for the Avro Container File header (including magic, file metadata and
* sync marker) and building the blocks. We only use the Avro library code to build the binary encoding of the Avro
* records.
*
* @see https://avro.apache.org/docs/1.8.1/spec.html#Object+Container+Files
*/
object AvroContainerFileHelpers {

  val magic: ByteBuffer = {
    val magicBytes = "Obj".getBytes ++ Array[Byte](1.toByte)
    val mg = ByteBuffer.allocate(magicBytes.length).put(magicBytes)
    mg.position(0)
    mg
  }

  def makeSyncMarker(): Array[Byte] = {
    val digester = MessageDigest.getInstance("MD5")
    digester.update(s"${UUID.randomUUID}@${System.currentTimeMillis()}".getBytes)
    val marker = ByteBuffer.allocate(16).put(digester.digest()).compact()
    marker.position(0)
    marker.array()
  }

  /*
  * Note that other implementations of avro container files, such as the javascript library
  * mtth/avsc uses "inception" to encode the header, that is, a datum following a header
  * schema should produce valid headers. We originally had attempted to do the same but for
  * an unknown reason two bytes wore being inserted into our header, one at the very beginning
  * of the header before the MAGIC marker, and one right before the syncmarker of the header.
  * We were unable to determine why this wasn't working, and so this solution was used instead
  * where the record/map is encoded per the avro spec manually without the use of "inception."
  */
  def header(schema: Schema, syncMarker: Array[Byte]): Array[Byte] = {
    def avroMap(map: Map[String, ByteBuffer]): Array[Byte] = {
      val mapBytes = map.flatMap {
        case (k, vBuff) =>
          val v = vBuff.array()
          val byteStr = k.getBytes()
          Varint.encodeLong(byteStr.length) ++ byteStr ++ Varint.encodeLong(v.length) ++ v
      }
      Varint.encodeLong(map.size.toLong) ++ mapBytes ++ Varint.encodeLong(0)
    }

    val schemaBytes = schema.toString.getBytes
    val schemaBuffer = ByteBuffer.allocate(schemaBytes.length).put(schemaBytes)
    schemaBuffer.position(0)
    val metadata = Map("avro.schema" -> schemaBuffer)
    magic.array() ++ avroMap(metadata) ++ syncMarker
  }

  def block(binaryRecords: Seq[Array[Byte]], syncMarker: Array[Byte]): Array[Byte] = {
    val countBytes = Varint.encodeLong(binaryRecords.length.toLong)
    val sizeBytes = Varint.encodeLong(binaryRecords.foldLeft(0)(_+_.length).toLong)

    val buff: ArrayBuffer[Byte] = new scala.collection.mutable.ArrayBuffer[Byte]()

    buff.append(countBytes:_*)
    buff.append(sizeBytes:_*)
    binaryRecords.foreach { rec =>
      buff.append(rec:_*)
    }
    buff.append(syncMarker:_*)

    buff.toArray
  }

  def encodeBlock[T](schema: Schema, records: Seq[GenericRecord], syncMarker: Array[Byte]): Array[Byte] = {
    //block(records.map(encodeRecord(schema, _)), syncMarker)
    val writer = new GenericDatumWriter[GenericRecord](schema)
    val out = new ByteArrayOutputStream()
    val binaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
    records.foreach(record => writer.write(record, binaryEncoder))
    binaryEncoder.flush()
    val flattenedRecords = out.toByteArray
    out.close()

    val buff: ArrayBuffer[Byte] = new scala.collection.mutable.ArrayBuffer[Byte]()

    val countBytes = Varint.encodeLong(records.length.toLong)
    val sizeBytes = Varint.encodeLong(flattenedRecords.length.toLong)

    buff.append(countBytes:_*)
    buff.append(sizeBytes:_*)
    buff.append(flattenedRecords:_*)
    buff.append(syncMarker:_*)

    buff.toArray
  }

  def encodeRecord[R <: GeneratedMessage with com.nitro.scalaAvro.runtime.Message[R]: ClassTag](
      entity: R
  ): Array[Byte] =
    encodeRecord(entity.companion.schema, entity.toMutable)

  def encodeRecord(schema: Schema, record: GenericRecord): Array[Byte] = {
    val writer = new GenericDatumWriter[GenericRecord](schema)
    val out = new ByteArrayOutputStream()
    val binaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
    writer.write(record, binaryEncoder)
    binaryEncoder.flush()
    val bytes = out.toByteArray
    out.close()
    bytes
  }
}

/**
  * Encoding of integers with variable-length encoding.
  *
  * The avro specification uses a variable length encoding for integers and longs.
  * If the most significant bit in a integer or long byte is 0 then it knows that no
  * more bytes are needed, if the most significant bit is 1 then it knows that at least one
  * more byte is needed. In signed ints and longs the most significant bit is traditionally
  * used to represent the sign of the integer or long, but for us it's used to encode whether
  * more bytes are needed. To get around this limitation we zig-zag through whole numbers such that
  * negatives are odd numbers and positives are even numbers:
  *
  * i.e. -1, -2, -3 would be encoded as 1, 3, 5, and so on
  * while 1,  2,  3 would be encoded as 2, 4, 6, and so on.
  *
  * More information is available in the avro specification here:
  * @see http://lucene.apache.org/core/3_5_0/fileformats.html#VInt
  *      https://developers.google.com/protocol-buffers/docs/encoding?csw=1#types
  */
object Varint {

  import scala.collection.mutable

  def encodeLong(longVal: Long): Array[Byte] = {
    val buff = new ArrayBuffer[Byte]()
    Varint.zigZagSignedLong(longVal, buff)
    buff.toArray[Byte]
  }

  def encodeInt(intVal: Int): Array[Byte] = {
    val buff = new ArrayBuffer[Byte]()
    Varint.zigZagSignedInt(intVal, buff)
    buff.toArray[Byte]
  }

  def zigZagSignedLong[T <: mutable.Buffer[Byte]](x: Long, dest: T): Unit = {
    // sign to even/odd mapping: http://code.google.com/apis/protocolbuffers/docs/encoding.html#types
    writeUnsignedLong((x << 1) ^ (x >> 63), dest)
  }

  def writeUnsignedLong[T <: mutable.Buffer[Byte]](v: Long, dest: T): Unit = {
    var x = v
    while ((x & 0xFFFFFFFFFFFFFF80L) != 0L) {
      dest += ((x & 0x7F) | 0x80).toByte
      x >>>= 7
    }
    dest += (x & 0x7F).toByte
  }

  def zigZagSignedInt[T <: mutable.Buffer[Byte]](x: Int, dest: T): Unit = {
    writeUnsignedInt((x << 1) ^ (x >> 31), dest)
  }

  def writeUnsignedInt[T <: mutable.Buffer[Byte]](v: Int, dest: T): Unit = {
    var x = v
    while ((x & 0xFFFFF80) != 0L) {
      dest += ((x & 0x7F) | 0x80).toByte
      x >>>= 7
    }
    dest += (x & 0x7F).toByte
  }
}
