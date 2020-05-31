package com.github.mercurievv

import cats.data.{NonEmptyChain, Validated}
import zio.Task

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/19/2020
 * Time: 6:34 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
package object jobsearch {
  type AIO[+A] = Task[A]
  type Errorable[A] = Validated[NonEmptyChain[String], A]
}
