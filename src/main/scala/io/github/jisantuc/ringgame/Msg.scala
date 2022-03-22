package io.github.jisantuc.ringgame

import java.util.UUID

sealed abstract class Msg
final case class AddPlayer()                            extends Msg
final case class UpdatePendingPlayerName(value: String) extends Msg
final case class RemovePlayer(id: UUID)                 extends Msg
final case class StartGame()                            extends Msg
final case class GoHome()                               extends Msg
final case class ShowHelp()                             extends Msg
final case class UpdateChipsPerPlayer(diff: Int)        extends Msg
