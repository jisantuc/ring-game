package io.github.jisantuc.ringgame

import java.util.UUID

final case class Model(
    gameState: GameState,
    players: List[Player],
    pendingPlayerName: String
)

final case class Player(
    id: UUID,
    name: String,
    chips: Int
)

final case class PendingPlayer(
    name: String
)
