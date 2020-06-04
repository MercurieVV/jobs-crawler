package com.github.mercurievv.jobsearch.xml

import com.github.mercurievv.rss.generated.XMLProtocol
import javax.xml.datatype.{DatatypeFactory, XMLGregorianCalendar}
import scalaxb.{ElemName, Helper, XMLFormat}

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
      val FORMATER = "EEE, dd MMM yyyy HH:mm:ss 'Z'"

      val format = new SimpleDateFormat(FORMATER)
      val date = format.parse(seq.text)
      import java.util.GregorianCalendar
      val gregory = new GregorianCalendar
      gregory.setTime(date)
      Right(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory)) }
    catch
      { case e: Exception => Left(e.toString) }

    def writes(obj: XMLGregorianCalendar, namespace: Option[String], elementLabel: Option[String],
               scope: scala.xml.NamespaceBinding, typeAttribute: Boolean): scala.xml.NodeSeq =
      Helper.stringToXML(obj.toXMLFormat, namespace, elementLabel, scope)
  }

}
