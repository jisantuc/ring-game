package io.github.jisantuc.ringgame

import monocle.syntax.all._
import tyrian.Html.*
import tyrian.*

import java.util.UUID
import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object RingGame extends TyrianApp[Msg, Model]:

  private def homeTransition(initialChips: Int) =
    (Model.AddPlayers(Nil, "", initialChips, false), Cmd.Empty)

  private def toggleShowHelp(m: Model) =
    (Model.setShowHelp(!m.showHelp)(m), Cmd.Empty)

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
                  ap.initialChips,
                  Nil
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
            val equalized = Model.equalizeChips(ap)
            (Model.Play(equalized.players, ap.initialChips, false), Cmd.Empty)

          case GoHome() =>
            homeTransition(ap.initialChips)

          case ShowHelp() =>
            toggleShowHelp(model)

          case UpdateChipsPerPlayer(n) =>
            (Model.updateChipsPerPlayer(n)(ap), Cmd.Empty)
          // AwardChips doesn't make sense in the player form state
          // NoOp should always do nothing
          case NoOp | AwardChips(_, _) => (ap, Cmd.Empty)
      case p @ Model.Play(_, _, _) =>
        msg match {
          case GoHome() =>
            // initialChips doesn't need to be tracked in the play state,
            // but since chips can be neither created nor destroyed, we know
            // that this will divide evenly
            homeTransition(p.players.map(_.chips).sum / p.players.size)
          case ShowHelp() =>
            toggleShowHelp(model)
          case AwardChips(playerId, award) =>
            (
              p.focus(_.players)
                .modify(payout.earnChips(award, playerId, _)),
              Cmd.Empty
            )
          // lol @ scalafmt :(
          case AddPlayer() | UpdatePendingPlayerName(_) | RemovePlayer(
                _
              ) | StartGame() | UpdateChipsPerPlayer(_) | NoOp =>
            (p, Cmd.Empty)
        }
    }

  def view(model: Model): Html[Msg] =
    div(`class` := "flex-container flex-column")(
      render.topBar,
      render.help(model.showHelp),
      model match {
        case Model.AddPlayers(players, pendingPlayerName, initialChips, _) =>
          render.gameConfigRender(players, pendingPlayerName, initialChips)
        case Model.Play(players, initialChips, showHelp) =>
          render.playRender(players, initialChips, showHelp)
      }
    )

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty
