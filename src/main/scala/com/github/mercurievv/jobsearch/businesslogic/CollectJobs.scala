package com.github.mercurievv.jobsearch.businesslogic

import cats._
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import com.github.mercurievv.jobsearch.model.Job
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/19/2020
  * Time: 9:52 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class CollectJobs[F[_]: Monad, S[_]: Monad : FunctorFilter](jobsStorage: JobsStorage[F, S]) {
  private val log: Logger = LoggerFactory.getLogger(classOf[CollectJobs[Nothing, Nothing]])
  def collectJobsFromServers(jobsServers: List[JobsServer[F, S]]): F[Unit] = {
    jobsServers
      .map(collectJobFromServer)
      .sequence_
  }
  private def collectJobFromServer(jobsServer: JobsServer[F, S]) =
  for {
    jobs    <- jobsServer.getJobsFromServer
    _       <- jobsStorage.saveJobsToDb(jobs.mapFilter{
      case Valid(job) => Some(job)
      case Invalid(errors) =>
        log.error(errors.toString)
        None
    })
  } yield ()
}
