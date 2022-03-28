package io.github.jisantuc.ringgame

import monocle.Lens
import monocle.syntax.all._

import java.util.UUID

sealed abstract class Model extends Product with Serializable {
  val showHelp: Boolean
  val players: List[Model.Player],
}

object Model {
  final case class AddPlayers(
      players: List[Player],
      pendingPlayerName: String,
      initialChips: Int,
      showHelp: Boolean
  ) extends Model

  final case class Play(
      players: List[Player],
      showHelp: Boolean
  ) extends Model

  final case class Player(
      id: UUID,
      name: String,
      chips: Int
  )

  final case class PendingPlayer(
      name: String
  )

  private def showHelpLens: Lens[Model, Boolean] = Lens[Model, Boolean] {
    _.showHelp
  } { b =>
    {
      case ap @ AddPlayers(_, _, _, _) =>
        ap.copy(showHelp = b)
      case p @ Play(_, _) => p.copy(showHelp = b)
    }
  }

  def setShowHelp(b: Boolean)(m: Model) = showHelpLens.replace(b)(m)

  def addPlayer(player: Player)(m: AddPlayers) =
    m.focus(_.players).modify(_ :+ player)

  def deletePlayer(playerId: UUID)(m: AddPlayers) =
    m.focus(_.players).modify(_.filterNot(_.id == playerId))

  def updateChipsPerPlayer(n: Int)(m: AddPlayers) =
    m.focus(_.initialChips).modify(_ + n)

  def equalizeChips(m: AddPlayers): AddPlayers =
    m.focus(_.players).each.modify(_.copy(chips = m.initialChips))

}
