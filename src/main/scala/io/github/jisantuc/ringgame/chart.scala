package io.github.jisantuc.ringgame

import tyrian.Html
import tyrian.Html.*

object chart {

  private def viewBox(xMax: Int, yMax: Int) =
    attribute("viewBox", s"0 0 $xMax $yMax")

  private def countingLine[T](
      heights: List[Int],
      default: Int,
      domainXMax: Int,
      domainYMax: Int,
      xMax: Int,
      yMax: Int
  ) =
    val nTicks = heights.size.toDouble

    val percentages = heights
      .map(_ / domainYMax.toDouble)
      .zipWithIndex
      .map { case (y, x) =>
        (
          xMax * x / nTicks,
          (yMax - 20) * (1 - y)
        )
      }

    tag[T]("polyline")(
      attribute(
        "points",
        percentages
          .map { case (xTick, height) =>
            s"$xTick,${height}"
          }
          .mkString(" ")
      ),
      attribute(
        "transform",
        "translate(0, 10)"
      )
    )(Nil)

  /** Create an SVG chart of a sequence of integer values
    * @param yMax
    *   a hardcoded maximum value for the y axis
    * @param fillXTo
    *   a hardcoded minimum value for the number of x ticks
    * @param values
    *   the data to plot
    * @param xBuffer
    *   the number of extra x ticks to render
    */
  def countingLineChart[T](
      totalChips: Int,
      initialChips: Int,
      values: List[Int]
  ): Html[T] =
    val chartXMax = 1200
    val chartYMax = 400
    div(`class` := "flex-container flex-row sparkline-container")(
      svg(
        `class` := "line-chart flex-row",
        viewBox(chartXMax, chartYMax)
      )(
        countingLine[T](
          values,
          initialChips,
          values.size,
          totalChips,
          chartXMax,
          chartYMax
        )
      )
    )
}
