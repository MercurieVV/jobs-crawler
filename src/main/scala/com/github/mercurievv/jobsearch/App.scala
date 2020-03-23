package com.github.mercurievv.jobsearch

import com.github.mercurievv.jobsearch.persistence.DynamodbJobsStorage
import com.github.mercurievv.jobsearch.stackowerflow.{GetJobsFromStackowerflow, StackowerflowJobsApi}
import org.http4s.client.blaze.BlazeClientBuilder
import zio.ManagedApp
import zio.blocking.Blocking
import zio.console.{Console, putStrLn}

import scala.concurrent.ExecutionContext.global

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/13/2020
  * Time: 5:24 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class App extends ManagedApp {
  import zio._
  import zio.clock.Clock
  import zio.interop.catz._

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] = {
    type AppEnvironment = Clock with Console with Blocking
    ZIO.runtime[AppEnvironment].flatMap { implicit rts =>

      BlazeClientBuilder[AIO](global).resource.use { client =>
        val module = new Module[AIO, List](client)
        module.collectJobs.collectJobsFromServers(???)
        // use `client` here and return an `IO`.
        // the client will be acquired and shut down
        // automatically each time the `IO` is run.
//        ZIO.unit
      }
    }
      .toManaged_
      .foldM(
        err => putStrLn(s"Execution failed with: $err").as(1).toManaged_,
        _ => ZManaged.succeed(0)
      )
  }
}
