package com.github.mercurievv.jobsearch.xml

import java.text.DateFormat
import java.time.{LocalDateTime, OffsetDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.util.{Date, Locale}

import com.github.mercurievv.rss.generated.XMLProtocol
import javax.xml.datatype.{DatatypeFactory, XMLGregorianCalendar}
import scalaxb.{ElemName, Helper, XMLFormat}

import scala.util.Try

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 6/2/2020
  * Time: 1:31 AM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
object CustomXmlProtocol extends XMLProtocol {
  override implicit lazy val __CalendarXMLFormat: XMLFormat[XMLGregorianCalendar] = new XMLFormat[XMLGregorianCalendar] {
    def reads(seq: scala.xml.NodeSeq, stack: List[ElemName]): Either[String, XMLGregorianCalendar] = try {
      import java.text.SimpleDateFormat
      //Sat, 30 May 2020 11:37:12 Z
      //Sat, 06 Jun 2020 15:18:24 +0000

      val FORMATER = "EEE, dd MMM yyyy HH:mm:ss 'Z'"

      Try(ZonedDateTime.parse(seq.text, DateTimeFormatter.RFC_1123_DATE_TIME))
        .orElse(Try(LocalDateTime.parse(seq.text, DateTimeFormatter.ofPattern(FORMATER, Locale.ENGLISH)).atZone(ZoneId.of("UTC"))))
        .map(zdt => {
          val date = Date.from(zdt.toInstant)
          import java.util.GregorianCalendar
          val gregory = new GregorianCalendar
          gregory.setTime(date)
          DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory)
        })
        .toEither
        .left
        .map(_.getMessage)
    }
    def writes(obj: XMLGregorianCalendar, namespace: Option[String], elementLabel: Option[String],
               scope: scala.xml.NamespaceBinding, typeAttribute: Boolean): scala.xml.NodeSeq =
      Helper.stringToXML(obj.toXMLFormat, namespace, elementLabel, scope)
  }

}
