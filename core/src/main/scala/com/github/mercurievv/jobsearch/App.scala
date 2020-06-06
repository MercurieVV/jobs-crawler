package com.github.mercurievv.jobsearch

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/13/2020
  * Time: 5:24 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
object App extends zio.App {
  import zio._

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    AppHandler.run(this)
  }
}
