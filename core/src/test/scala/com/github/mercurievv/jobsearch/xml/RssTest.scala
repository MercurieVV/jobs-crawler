package com.github.mercurievv.jobsearch.xml


import com.github.mercurievv.rss.generated.XMLProtocol
import javax.xml.datatype.{DatatypeFactory, XMLGregorianCalendar}
import scalaxb.{DataTypeFactory, ElemName, Helper, XMLCalendar, XMLFormat}

import scala.xml.{Text, XML}


class RssTest extends org.scalatest.FunSuite {

  test("decode rss") {
    import CustomXmlProtocol._
    scalaxb.fromXML[XMLGregorianCalendar]( new Text("Sat, 30 May 2020 11:37:12 Z"))
    val rss = scalaxb.fromXML[com.github.mercurievv.rss.generated.Rss](XML.loadString(
      """<?xml version="1.0" encoding="utf-8"?>
        | <rss xmlns:a10="http://www.w3.org/2005/Atom" version="2.0">
        |   <channel xmlns:os="http://a9.com/-/spec/opensearch/1.1/">
        |    <title>remote scala jobs - Stack Overflow</title>
        |    <link>https://stackoverflow.com/jobs</link>
        |    <description>remote scala jobs - Stack Overflow</description>
        |    <image>
        |        <url>http://cdn.sstatic.net/Sites/stackoverflow/img/favicon.ico?v=4f32ecc8f43d</url>
        |        <title>remote scala jobs - Stack Overflow</title>
        |        <link>https://stackoverflow.com/jobs</link>
        |    </image>
        |    <os:totalResults>10</os:totalResults>
        |    <item>
        |        <guid isPermaLink="false">396789</guid>
        |        <link>https://stackoverflow.com/jobs/396789/backend-engineer-ascendify?a=294nRAfUVLos&amp;so_medium=Talent&amp;so_source=TalentApi</link>
        |        <a10:author>
        |            <a10:name>Ascendify</a10:name>
        |        </a10:author>
        |        <category>scala</category>
        |        <category>php</category>
        |        <category>java</category>
        |        <category>elasticsearch</category>
        |        <category>postgresql</category>
        |        <title>Backend Engineer at Ascendify () (allows remote)</title>
        |        <description></description>
        |        <pubDate>Sat, 30 May 2020 11:37:12 Z</pubDate>
        |        <a10:updated>2020-05-30T11:37:12Z</a10:updated>
        |    </item>
        |    <item>
        |            <title><![CDATA[Create Logo and Web Pages for New IT Service - Upwork]]></title>
        |            <link>
        |                https://www.upwork.com/jobs/Create-Logo-and-Web-Pages-for-New-Service_%7E0159da9cec51ec4459?source=rss
        |            </link>
        |            <description><![CDATA[]]></description>
        |            <content:encoded><![CDATA[]]></content:encoded>
        |            <pubDate>Sat, 06 Jun 2020 14:52:58 +0000</pubDate>
        |            <guid>
        |                https://www.upwork.com/jobs/Create-Logo-and-Web-Pages-for-New-Service_%7E0159da9cec51ec4459?source=rss
        |            </guid>
        |        </item>
        |
        |
        |</channel></rss>
        |""".stripMargin))
    println(rss)
  }
}
