package io.github.jisantuc.ringgame

import tyrian.Html.*
import tyrian.*

import java.util.UUID
import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object RingGame extends TyrianApp[Msg, Model]:

  def init(flags: Map[String, String]): (Model, Cmd[Msg]) =
    (Model(AddPlayers, Nil, ""), Cmd.Empty)

  def update(msg: Msg, model: Model): (Model, Cmd[Msg]) =
    msg match
      case AddPlayer() =>
        val out = (
          model.copy(
            players = model.players :+ Player(
              UUID.randomUUID,
              model.pendingPlayerName,
              30
            ),
            pendingPlayerName = ""
          ),
          Cmd.Empty
        )
        println(out)
        out
      case UpdatePendingPlayerName(s) =>
        println(s)
        (
          model.copy(pendingPlayerName = s),
          Cmd.Empty
        )
      case RemovePlayer(id) =>
        (model.copy(players = model.players.filterNot(_.id == id)), Cmd.Empty)
      case StartGame() =>
        (model, Cmd.Empty)

  def view(model: Model): Html[Msg] =
    model.gameState match {
      case AddPlayers =>
        render.playerFormRender(model.players, model.pendingPlayerName)
    }

  def subscriptions(model: Model): Sub[Msg] =
    Sub.Empty
