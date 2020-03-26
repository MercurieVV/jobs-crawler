package com.github.mercurievv.jobsearch

import com.github.mercurievv.jobsearch.model.{Company, Id, JobsResource, Tag}
import enumeratum.NoSuchMember
import org.http4s.Uri
import org.scanamo.DynamoFormat

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/13/2020
  * Time: 11:50 AM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
package object persistence {
  implicit val jobsResourceFormat: DynamoFormat[JobsResource] =
    DynamoFormat.coercedXmap[JobsResource, String, NoSuchMember[JobsResource]](JobsResource.withName)(_.entryName)
  implicit val urlFormat: DynamoFormat[Uri] =
    DynamoFormat.coercedXmap[Uri, String, RuntimeException](Uri.unsafeFromString)(_.renderString)
  implicit val idFormat: DynamoFormat[Id] = DynamoFormat.iso[Id, String](Id(_), id => id)
  implicit val taggFormat: DynamoFormat[Tag] = DynamoFormat.iso[Tag, String](Tag(_), id => id)
  implicit val companyFormat: DynamoFormat[Company] = DynamoFormat.iso[Company, String](Company(_), id => id)
}
