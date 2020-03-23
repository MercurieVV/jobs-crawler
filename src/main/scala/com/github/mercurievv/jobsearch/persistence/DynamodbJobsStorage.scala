package com.github.mercurievv.jobsearch.persistence
import java.time.{ZoneId, ZonedDateTime}

import cats.{Monad, MonoidK}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsyncClientBuilder, AmazonDynamoDBClientBuilder}
import com.github.mercurievv.jobsearch.model.{Company, Id, Job, JobsResource, Tag}
import org.scanamo._
import org.scanamo.syntax._
import org.scanamo.generic.auto._
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._
import org.http4s.Uri
import org.scanamo.ops.ScanamoOpsT
import shapeless.tag
import shapeless.tag.@@
import zio.IO

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/12/2020
  * Time: 4:19 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class DynamodbJobsStorage {
  private val client = {
    val conf = new EndpointConfiguration("http://localhost:8000", "us-east-1")
    AmazonDynamoDBAsyncClientBuilder.standard().withEndpointConfiguration(conf).build()
  }
  private val scanamo = ScanamoZio(client)

  private val table = Table[Job]("Jobs")

  def putNewJobs(jobsResource: JobsResource, jobs: Set[Job]): IO[AmazonDynamoDBException, List[Either[DynamoReadError, Job]]] = {
    val jobsFromOneResource = jobs.filter(_.jobsResource == jobsResource)
    val op = for {
      lastItem <- table
        .filter("jobsResource" -> jobsResource)
        .limit(1)
        .scan()
      _ <- table.putAll(
        jobsFromOneResource.filter(_.created.isAfter(
          lastItem.last.map(_.created).getOrElse(ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT"))))))
    } yield lastItem
    scanamo.exec(op)
  }
}
