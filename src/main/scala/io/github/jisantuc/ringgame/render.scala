package io.github.jisantuc.ringgame

import tyrian.Attr
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

  private def renderPlayerLine(player: Model.Player): Html[Msg] =
    div(`class` := "flex-container flex-row")(
      text(player.name),
      button(onClick(RemovePlayer(player.id)))(text("❌"))
    )

  private def initialChipsConfig(chips: Int): Html[Msg] =
    div(`class` := "flex-container flex-row")(
      text("Initial chips per player:"),
      div(style("max-width", "60px"), `class` := "flex-container flex-row")(
        button(onClick(UpdateChipsPerPlayer(-1)))("-"),
        text(chips.toString),
        button(onClick(UpdateChipsPerPlayer(1)))("+")
      )
    )

  def gameConfigRender(
      players: List[Model.Player],
      pendingPlayerName: String,
      initialChips: Int
  ): Html[Msg] =
    div(`class` := "flex-container flex-column")(
      initialChipsConfig(initialChips) +:
        players.map(renderPlayerLine) :+
        newPlayerFormLine(pendingPlayerName) :+
        button(
          `class` := "player-form-submit",
          onClick(StartGame()),
          disabled(players.size < 3)
        )(
          text("Start")
        )
    )

  def help(v: Boolean): Html[Msg] =
    div(`class` := "help-text", hidden(!v))(
      p(
        text("""
    | A ring game is a game of pool played with more than two people in
    | which each player attempts to achieve some goal, and players who achieve
    | the goal are rewarded with chips from each other player.
    |
    | You can see an example banks ring game""".trim.stripMargin ++ " "),
        a(href := "https://www.youtube.com/watch?v=itlmp8Bajus")(
          text("at the 2022 Derby City Classic")
        ),
        text("""
        | . In the example, players are playing 9 ball banks -- each player who successfully makes a
        | called bank is given a chip by each other player, with the number of chips per bank
        | increasing each round. You can read more about different ring game versions
        | on""".trim.stripMargin ++ " "),
        a(
          href := "http://www.billiardsforum.com/pool-rules/billiards-ring-game-rules"
        )(
          text("this Billiards Forum page")
        ),
        text(".")
      )
    )

  def topBar: Html[Msg] = div(`class` := "flex-container flex-row")(
    button(onClick(GoHome()))("Home"),
    button(onClick(ShowHelp()))("❓")
  )
}
