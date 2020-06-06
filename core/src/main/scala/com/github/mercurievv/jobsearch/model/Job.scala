package com.github.mercurievv.jobsearch.model

import java.time.ZonedDateTime

import org.http4s.Uri
import org.http4s.blaze.http.Url

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/10/2020
  * Time: 5:05 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
case class Job(jobsResource: JobsResource,
               id: Id,
               link: Uri,
               title: String,
               description: String,
               tags: Seq[Tag],
               company: Company,
               created: ZonedDateTime,
               // updated: ZonedDateTime
)
