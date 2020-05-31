package com.github.mercurievv.jobsearch

import com.github.mercurievv.jobsearch.persistence.DynamodbJobsStorage
import com.github.mercurievv.jobsearch.stackowerflow.{GetJobsFromStackowerflow, StackowerflowJobsApi}
import org.http4s.client.blaze.BlazeClientBuilder
import zio.ManagedApp
import zio.blocking.Blocking
import zio.console.{Console, putStrLn}
import cats.implicits._

import scala.concurrent.ExecutionContext.global

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/13/2020
  * Time: 5:24 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
object App extends ManagedApp {
  import zio._
  import zio.clock.Clock
  import zio.interop.catz._

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] = {
    AppHandler.run()
  }
}
