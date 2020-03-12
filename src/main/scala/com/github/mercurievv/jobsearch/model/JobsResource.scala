package com.github.mercurievv.jobsearch.model

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/12/2020
 * Time: 2:29 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
import enumeratum._

sealed trait JobsResource extends EnumEntry {}

object JobsResource extends Enum[JobsResource]  {
  val values = findValues

  case object StackOverflow extends JobsResource
  case object Upwork extends JobsResource

}
