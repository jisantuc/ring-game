package io.github.jisantuc.ringgame

import tyrian.Html
import tyrian.Html.*

object render {
  private def newPlayerFormLine(pendingPlayerName: String): Html[Msg] =
    div(`class` := "flex-container flex-row")(
      input[Msg](
        `class`     := "grow",
        placeholder := "player name",
        onInput(UpdatePendingPlayerName(_))
      ),
      button(
        onClick(AddPlayer()),
        disabled(pendingPlayerName.isEmpty),
        style("margin-left", "0.25rem")
      )(
        text("➕")
      )
    )

  private def renderPlayerLine(player: Player): Html[Msg] =
    div(`class` := "flex-container flex-row")(
      text(player.name),
      button(onClick(RemovePlayer(player.id)))(text("❌"))
    )

  def playerFormRender(
      players: List[Player],
      pendingPlayerName: String
  ): Html[Msg] =
    div(`class` := "flex-container flex-column")(
      players.map(renderPlayerLine) :+
        newPlayerFormLine(pendingPlayerName) :+
        button(
          `class` := "player-form-submit",
          onClick(StartGame()),
          disabled(players.isEmpty)
        )(
          text("Start")
        )
    )
}
