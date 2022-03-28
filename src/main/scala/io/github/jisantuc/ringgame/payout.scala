package io.github.jisantuc.ringgame

import monocle.syntax.all._

import java.util.UUID

object payout {
  def earnChips(
      chipsEarned: Int,
      earningPlayer: UUID,
      players: List[Model.Player]
  ): List[Model.Player] =
    val pot = players.foldLeft(0)((acc, player) =>
      if player.id == earningPlayer then acc
      else acc + (chipsEarned `min` player.chips)
    )
    players.map { player =>
      if player.id == earningPlayer then player.focus(_.chips).modify(_ + pot)
      else player.focus(_.chips).modify(chips => (chips - chipsEarned) `max` 0)
    }
}
