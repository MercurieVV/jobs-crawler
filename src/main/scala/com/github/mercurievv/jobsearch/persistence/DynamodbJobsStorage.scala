package com.github.mercurievv.jobsearch.persistence
import java.time.{ZoneId, ZonedDateTime}

import cats.effect.Async
import cats.kernel.Eq
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException
import com.github.mercurievv.jobsearch.AIO
import com.github.mercurievv.jobsearch.businesslogic.JobsStorage
import com.github.mercurievv.jobsearch.model.{Job, JobsResource}
import fs2._
import org.scanamo._
import org.scanamo.generic.auto._
import zio.IO
import zio.interop.catz._

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/12/2020
  * Time: 4:19 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class DynamodbJobsStorage[F[_]: Async](client: AmazonDynamoDBAsync) extends JobsStorage[F, Stream[F, *]] {
  private val scanamo: ScanamoCats[F] = ScanamoCats[F](client)

  private val table = Table[Job]("Jobs")

  import cats.implicits._
  implicit val eeq: Eq[JobsResource] = Eq.by[JobsResource, String](_.toString)

  override def saveJobsToDb(jobs: Stream[F, Job]): F[Unit] = {
    val ops = for {
      byJob <- jobs.groupAdjacentBy(_.jobsResource)
    } yield saveJobsOps(byJob._1, byJob._2.toList.toSet)

    for {
      opsp <- ops.compile.last
      _    <- scanamo.exec(opsp.get)
    } yield ()
  }

  private def saveJobsOps(jobsResource: JobsResource, jobs: Set[Job]) = {
    for {
      lastJob <- table
        .filter("jobsResource" -> jobsResource)
        .limit(1)
        .scan
      jobsIO = jobs.filter(
        _.created.isAfter(
          lastJob.last.map(_.created).getOrElse(ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneId.of("UTC")))))
      _ <- table.putAll(jobsIO)
    } yield ()
  }
}
