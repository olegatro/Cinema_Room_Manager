package cinema

const val EMPTY_SEAT = "S"
const val CHOSEN_SEAT = "B"

fun getGrid(rowCount: Int, seatCount: Int): MutableList<MutableList<String>> {
    val grid: MutableList<MutableList<String>> = mutableListOf()

    repeat(rowCount) {
        val row: MutableList<String> = mutableListOf()

        repeat(seatCount) {
            row.add(EMPTY_SEAT)
        }

        grid.add(row)
    }

    return grid
}

fun getGridAsString(grid: MutableList<MutableList<String>>): String {
    var result = "Cinema:\n"
    result += " "

    // head
    for (i in grid.first().indices) {
        result += " ${i + 1}"
    }
    result += "\n"

    // body
    for (i in grid.indices) {
        val row = grid[i]

        result += "${i + 1} " + row.joinToString(" ")

        if (i != grid.lastIndex) {
            result += "\n"
        }
    }

    return result
}

fun getSeatPrice(grid: MutableList<MutableList<String>>, rowNumber: Int, seatNumber: Int): Int {
    return when {
        grid.size * grid.first().size <= 60 -> 10
        rowNumber <= grid.size / 2 -> 10
        else -> 8
    }
}

fun setChosenSeat(
    grid: MutableList<MutableList<String>>,
    rowNumber: Int,
    seatNumber: Int
): MutableList<MutableList<String>> {
    grid[rowNumber - 1][seatNumber - 1] = CHOSEN_SEAT
    return grid
}

fun isSeatEmpty(
    grid: MutableList<MutableList<String>>,
    rowNumber: Int,
    seatNumber: Int
): Boolean {
    return grid[rowNumber - 1][seatNumber - 1] == EMPTY_SEAT
}

fun isSeatValid(
    grid: MutableList<MutableList<String>>,
    rowNumber: Int,
    seatNumber: Int
): Boolean {
    return rowNumber > grid.size || seatNumber > grid.first().size
}

fun outputStatistics(grid: MutableList<MutableList<String>>) {
    var ticketCount: Int = 0
    var ticketPercentage: Double = 0.0
    var current: Int = 0
    var total: Int = 0

    for (rowIndex in grid.indices) {
        val row = grid[rowIndex]

        for (seatIndex in row.indices) {
            val seat = row[seatIndex]

            if (seat == CHOSEN_SEAT) {
                current += getSeatPrice(grid, rowIndex + 1, seatIndex + 1)
                ticketCount++
            }
        }
    }

    ticketPercentage = (ticketCount.toDouble() / (grid.size.toDouble() * grid.first().size.toDouble())) * 100

    if (grid.size * grid.first().size <= 60) {
        total = grid.size * grid.first().size * 10
    } else {
        total = (grid.size / 2) * grid.first().size * 10
        total += (grid.size - (grid.size / 2)) * grid.first().size * 8
    }

    println("Number of purchased tickets: $ticketCount")
    println("Percentage: ${"%.2f".format(ticketPercentage)}%")
    println("Current income: \$$current")
    println("Total income: \$$total")
}

fun main() {
    println("Enter the number of rows:")
    val rowCount: Int = readln().toInt()

    println("Enter the number of seats in each row:")
    val seatCount: Int = readln().toInt()

    var grid: MutableList<MutableList<String>> = getGrid(rowCount, seatCount)

    while (true) {
        println()
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")

        val selectedMenu: Int = readln().toInt()

        if (selectedMenu == 0) {
            break
        }

        if (selectedMenu == 1) {
            println()
            println(getGridAsString(grid))
            continue
        }

        if (selectedMenu == 3) {
            println()
            outputStatistics(grid)
            continue
        }

        while (true) {
            println()
            println("Enter a row number:")
            val rowNumber: Int = readln().toInt()

            println("Enter a seat number in that row:")
            val seatNumber: Int = readln().toInt()

            if (isSeatValid(grid, rowNumber, seatNumber)) {
                println("Wrong input!")
                continue
            }

            if (!isSeatEmpty(grid, rowNumber, seatNumber)) {
                println("That ticket has already been purchased!")
                continue
            }

            grid = setChosenSeat(grid, rowNumber, seatNumber)

            println()
            println("Ticket price: $${getSeatPrice(grid, rowNumber, seatNumber)}")
            break
        }
    }
}