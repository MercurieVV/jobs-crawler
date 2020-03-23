package com.github.mercurievv.jobsearch.businesslogic

import cats.Monad
import cats.implicits._

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/19/2020
  * Time: 9:52 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class CollectJobs[F[_], S[_]](jobsOps: JobsStorage[F, S])(implicit F: Monad[F]) {
  def collectJobsFromServers(jobsServers: List[JobsServer[F, S]]): F[Unit] = {
    jobsServers
      .map(collectJobFromServer)
      .sequence_
  }
  private def collectJobFromServer(jobsServer: JobsServer[F, S]) =
  for {
    jobs    <- jobsServer.getJobsFromServer
    newJobs <- F.pure(jobsOps.filterNewJobsOnly(jobs))
    _       <- jobsOps.saveJobsToDb(newJobs)
  } yield ()
}
