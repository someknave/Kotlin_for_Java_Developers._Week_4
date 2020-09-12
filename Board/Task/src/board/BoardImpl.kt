package board

import board.Direction.*
import javax.swing.CellEditor

fun createSquareBoard(width: Int): SquareBoard {
    val cells = Array(width) {Array<Cell>(width) {Cell(0,0)} }
    for (i in 1..width) {
        for (j in 1..width) {
            cells[i-1][j-1] = Cell(i, j)
        }
    }
    return SquareBoardImpl(width, cells)

}
fun <T> createGameBoard(width: Int): GameBoard<T> {
    val cells = Array(width) { Array<Cell>(width) { Cell(0, 0) } }
    val map = mutableMapOf<Cell, T?>()
    for (i in 1..width) {
        for (j in 1..width) {
            cells[i - 1][j - 1] = Cell(i, j)
            map[cells[i - 1][j - 1]] = null
        }
    }
    return GameBoardImpl(width, cells, map)


}


open class SquareBoardImpl(override val width: Int, open val cells: Array<Array<Cell>> ) : SquareBoard {
    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return cells.getOrNull(i-1)?.getOrNull(j-1)
    }

    override fun getCell(i: Int, j: Int): Cell {
        return cells[i-1][j-1]
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.map {cells.getOrNull(i-1)?.getOrNull(it-1)}.filterNotNull()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.map {cells.getOrNull(it-1)?.getOrNull(j-1)}.filterNotNull()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> cells.getOrNull(this.i - 2)?.getOrNull(this.j - 1)
            DOWN -> cells.getOrNull(this.i)?.getOrNull(this.j - 1)
            LEFT -> cells.getOrNull(this.i - 1)?.getOrNull(this.j - 2)
            RIGHT -> cells.getOrNull(this.i - 1)?.getOrNull(this.j)
        }
    }

}
class GameBoardImpl<T>(override val width: Int, override val cells: Array<Array<Cell>>,
                       val cellContents: MutableMap<Cell, T?>) : SquareBoardImpl(width, cells),
        GameBoard<T> {
    override fun get(cell: Cell): T? {
        return cellContents[cell]
    }

    override fun set(cell: Cell, value: T?) {
        cellContents[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Set<Cell> {
        return cellContents.filterValues { predicate(it) }.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).toList().getOrNull(0)
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return filter(predicate).isNotEmpty()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return filter(predicate) == cellContents.keys
    }
}