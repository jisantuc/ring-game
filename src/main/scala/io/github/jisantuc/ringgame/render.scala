package io.github.jisantuc.ringgame

// import tyrian.KeyboardEvent
import tyrian.Attr
import tyrian.Html
import tyrian.Html.*

object render {
  private def newPlayerFormLine(pendingPlayerName: String): Html[Msg] =
    div(`class` := "flex-container flex-row padded")(
      input[Msg](
        `class`     := "flex player-input-line big-text padded",
        placeholder := "player name",
        onInput(UpdatePendingPlayerName(_)),
        onKeyUp {
          case event if event.key == "Enter" =>
            AddPlayer()
          case _ => NoOp
        }
      ),
      button(
        `class` := "player-submit-button padded",
        onClick(AddPlayer()),
        disabled(pendingPlayerName.isEmpty)
      )(
        text("➕")
      )
    )

  private def renderPlayerLine(player: Model.Player): Html[Msg] =
    div(`class` := "flex-container flex-row big-text padded")(
      text(player.name),
      button(onClick(RemovePlayer(player.id)))(text("❌"))
    )

  private def initialChipsConfig(chips: Int): Html[Msg] =
    div(`class` := "flex-container flex-row big-text padded")(
      text("Initial chips per player:"),
      div(
        style("max-width", "10rem"),
        `class` := "flex-container flex-row padded"
      )(
        button(
          `class` := "chip-count-config padded",
          onClick(UpdateChipsPerPlayer(-1))
        )("➖"),
        text(chips.toString),
        button(
          `class` := "chip-count-config padded",
          onClick(UpdateChipsPerPlayer(1))
        )("➕")
      )
    )

  def gameConfigRender(
      players: List[Model.Player],
      pendingPlayerName: String,
      initialChips: Int
  ): Html[Msg] =
    div(`class` := "flex-container flex-column padded")(
      initialChipsConfig(initialChips) +:
        players.map(renderPlayerLine) :+
        newPlayerFormLine(pendingPlayerName) :+
        button(
          `class` := "player-form-submit big-text padded",
          onClick(StartGame()),
          disabled(players.size < 3)
        )(
          text("Start")
        )
    )

  def help(v: Boolean): Html[Msg] =
    div(`class` := "help-text big-text padded", hidden(!v))(
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

  def topBar: Html[Msg] = div(`class` := "flex-container flex-row padded")(
    button(`class` := "big-text padded", onClick(GoHome()))("Home"),
    button(`class` := "big-text padded", onClick(ShowHelp()))("❓")
  )

  inline private def playerCard(player: Model.Player): Html[Msg] =
    div(
      `class` := "flex-container flex-column player-card padded"
    )(
      h1(`class` := "big-text padded")(player.name),
      h3(`class` := "big-text padded")(s"${player.chips} chips"),
      button(`class` := "big-text padded", onClick(AwardChips(player.id, 1)))(
        "Award"
      )
    )

  def playRender(players: List[Model.Player], showHelp: Boolean): Html[Msg] =
    div(
      `class` := "flex-container flex-column padded",
      style("flex-wrap", "wrap")
    )(
      players.map(playerCard(_))
    )
}
