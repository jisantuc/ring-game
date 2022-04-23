package io.github.jisantuc.ringgame

import java.util.UUID

class RingGameTests extends munit.FunSuite {

  private def expectChips(n: Int, players: List[Model.Player]) =
    assertEquals(players.map(_.chips).sum, n)

  private def expectChipsForPlayer(
      n: Int,
      playerId: UUID,
      players: List[Model.Player]
  ) =
    assertEquals(
      players.find(_.id == playerId).map(_.chips),
      Some(n)
    )

  val richPlayers = List(
    Model.Player(UUID.randomUUID, "p1", 30, Nil),
    Model.Player(UUID.randomUUID, "p2", 30, Nil),
    Model.Player(UUID.randomUUID, "p3", 30, Nil)
  )

  val p1 = richPlayers(0)

  test("simple payout test") {
    val postPayout = payout.earnChips(1, p1.id, richPlayers)
    expectChipsForPlayer(32, p1.id, postPayout)
  }

  test("extreme payout test") {
    // 100 chips is greater than anyone's chip count
    val postPayout = payout.earnChips(100, p1.id, richPlayers)
    expectChips(90, postPayout)
    expectChipsForPlayer(90, p1.id, postPayout)
    assertEquals(postPayout.filterNot(_.id == p1.id).map(_.chips), List(0, 0))
  }

  test("bankrupt one player") {
    val players = List(
      Model.Player(UUID.randomUUID, "p1", 1, Nil),
      Model.Player(UUID.randomUUID, "p2", 20, Nil),
      Model.Player(UUID.randomUUID, "p3", 25, Nil)
    )

    val p1 = players(0)
    val p2 = players(1)
    val p3 = players(2)

    val postPayout = payout.earnChips(5, p3.id, players)
    expectChips(46, postPayout)
    expectChipsForPlayer(0, p1.id, postPayout)
    expectChipsForPlayer(15, p2.id, postPayout)
    expectChipsForPlayer(31, p3.id, postPayout)
  }

  test("chip equalization equalizes chip counts") {
    val players = List(
      Model.Player(UUID.randomUUID, "p1", 1, Nil),
      Model.Player(UUID.randomUUID, "p2", 20, Nil),
      Model.Player(UUID.randomUUID, "p3", 25, Nil)
    )
    val equalized =
      Model.equalizeChips(Model.AddPlayers(players, "", 30, false))
    equalized.players.map(player =>
      expectChipsForPlayer(30, player.id, equalized.players)
    )
  }

  test("earning chips preserves history") {
    val players = List(
      Model.Player(UUID.randomUUID, "p1", 1, Nil),
      Model.Player(UUID.randomUUID, "p2", 20, Nil),
      Model.Player(UUID.randomUUID, "p3", 25, Nil)
    )

    val p1 = players(0)
    val p2 = players(1)

    val postPayout  = payout.earnChips(1, p1.id, players)
    val postPayout2 = payout.earnChips(1, p2.id, postPayout)

    // we should append to an empty history
    assertEquals(
      postPayout.map(_.history),
      List(
        List(3),
        List(19),
        List(24)
      )
    )

    // we should append correct values to a nonempty history
    assertEquals(
      postPayout2.map(_.history),
      List(
        List(3, 2),
        List(19, 21),
        List(24, 23)
      )
    )

    // numbers of chips should also be correct
    assertEquals(
      postPayout2.map(_.chips),
      List(
        2,
        21,
        23
      )
    )
  }

}
