package io.github.jisantuc.ringgame

import java.util.UUID

sealed abstract class Msg
final case class AddPlayer()                            extends Msg
final case class UpdatePendingPlayerName(value: String) extends Msg
final case class RemovePlayer(id: UUID)                 extends Msg
final case class StartGame()                            extends Msg
