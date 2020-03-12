package com.github.mercurievv.http4s.coders.scalaxb

import cats.Monad
import cats.implicits._
import cats.effect.ConcurrentEffect
import fs2.Chunk
import org.http4s.headers.`Content-Type`
import org.http4s._
import scalaxb.XMLFormat

import scala.xml.NamespaceBinding

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/5/2020
  * Time: 6:09 AM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
object Http4sScalaxbEncodersDecoders {
  implicit def xmlDecoder[F[_], T](
    implicit format: XMLFormat[T],
    F: Monad[F],
    C: ConcurrentEffect[F]
  ): EntityDecoder[F, T] = EntityDecoder.decodeBy(MediaType.application.xml) { decodeXml }

  def decodeXml[T, F[_]](m: Media[F])(
    implicit format: XMLFormat[T],
    C: ConcurrentEffect[F]
  ): DecodeResult[F, T] = {
    DecodeResult {
      fs2.Stream
        .resource(
          fs2.io
            .toInputStreamResource(m.body)
            .map(xml.XML.load)
        )
        .compile
        .last
        .map(
          _.toRight(MalformedMessageBodyFailure("Invalid XML: empty body"))
            .flatMap(scalaxb.fromXMLEither[T](_).leftMap(MalformedMessageBodyFailure(_)))
        )
    }
  }

  implicit def xmlEncoder[F[_], T](elementLabel: String, scope: NamespaceBinding)(
    implicit format: scalaxb.CanWriteXML[T],
    C: ConcurrentEffect[F]
  ): EntityEncoder[F, T] =
    EntityEncoder
      .simple[F, T]() { t =>
        val xml = scalaxb
          .toXML(t, elementLabel, scope)
          .toString()
        Chunk.bytes(xml.getBytes())
      }
      .withContentType(`Content-Type`(MediaType.application.xml))

}
