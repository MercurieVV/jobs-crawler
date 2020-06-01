package com.github.mercurievv.jobsearch

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 5/30/2020
 * Time: 6:58 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
import java.io.{InputStream, OutputStream}

import cats.arrow.FunctionK
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.github.mercurievv.bulyon.lambdahttp4s.fs2zio.SimpleZioHandler
import fs2._
import org.http4s._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io.{->, /, POST}
import zio.ZIO
import zio.blocking.Blocking
import zio.console.{Console, putStrLn}
import zio.interop.catz._
import zio.interop.catz.implicits._
import cats.implicits._
import cats._
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import com.github.mercurievv.jobsearch.model.Job
import com.github.mercurievv.jobsearch.persistence.DynamodbJobsStorage

import scala.concurrent.ExecutionContext.global

object AppHandler extends RequestStreamHandler {
  import zio._
  import zio.clock.Clock
  import zio.interop.catz._


  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    run()
  }
  val seqToList = λ[Seq ~> List](_.toList)
  val sToFs2: List ~> Stream[AIO, *] = λ[List ~> fs2.Stream[AIO, *]](fs2.Stream.emits(_))

  private val dbclient = {
    val conf = new EndpointConfiguration("http://localhost:8000", "us-east-1")
    AmazonDynamoDBAsyncClientBuilder.standard().withEndpointConfiguration(conf).build()
  }
  val dynDbStorage = new DynamodbJobsStorage(dbclient)
  val saveJobs: List[Job] => AIO[Unit] = s => dynDbStorage.saveJobsToDb (sToFs2(s))

  def run(): ZManaged[zio.ZEnv, Nothing, Int] = {
    type AppEnvironment = Clock with Console with Blocking
    ZIO.runtime[AppEnvironment].flatMap { implicit rts =>

      BlazeClientBuilder[AIO](global).resource.use { client =>
        val module = new Module[AIO, List](client, seqToList, saveJobs)
        module.collectJobs.collectJobsFromServers(module.jobsServers)
      }
    }
      .toManaged_
      .foldM(
        err => putStrLn(s"Execution failed with: $err").as(1).toManaged_,
        _ => ZManaged.succeed(0)
      )
  }
}
