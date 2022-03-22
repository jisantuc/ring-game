package io.github.jisantuc.ringgame

import tyrian.Html.*
import tyrian.*

import java.util.UUID
import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object RingGame extends TyrianApp[Msg, Model]:

  def init(flags: Map[String, String]): (Model, Cmd[Msg]) =
    (Model.AddPlayers(Nil, "", 30, false), Cmd.Empty)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    model match {
      case ap @ Model.AddPlayers(_, _, _, _) =>
        msg match
          case AddPlayer() =>
            (
              Model.addPlayer(
                Model.Player(
                  UUID.randomUUID,
                  ap.pendingPlayerName,
                  ap.initialChips
                )
              )(ap),
              Cmd.Empty
            )
          case UpdatePendingPlayerName(s) =>
            (
              ap.copy(pendingPlayerName = s),
              Cmd.Empty
            )
          case RemovePlayer(id) =>
            (
              Model.deletePlayer(id)(ap),
              Cmd.Empty
            )
          case StartGame() =>
            // !! IMPORTANT !!
            // players should all start with the same number of chips,
            // even if the config was set differently at the exact moment
            // they were created
            (model, Cmd.Empty)

          case GoHome() =>
            (Model.AddPlayers(Nil, "", ap.initialChips, false), Cmd.Empty)

          case ShowHelp() =>
            (Model.setShowHelp(!model.showHelp)(model), Cmd.Empty)

          case UpdateChipsPerPlayer(n) =>
            (Model.updateChipsPerPlayer(n)(ap), Cmd.Empty)
    }

  def view(model: Model): Html[Msg] =
    div(`class` := "flex-container flex-column")(
      render.topBar,
      render.help(model.showHelp),
      model match {
        case Model.AddPlayers(players, pendingPlayerName, initialChips, _) =>
          render.gameConfigRender(players, pendingPlayerName, initialChips)
      }
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty
