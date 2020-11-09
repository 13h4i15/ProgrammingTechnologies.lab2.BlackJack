package util

import servermodel.Card
import kotlin.random.Random

fun Random.nextCard(cardsOut: List<Card>): Card {
    val costs = cardsOut.map { it.cost }

    var cost: Int
    do {
        cost = nextInt(1, 11)
    } while (costs.count { it == cost } >= 4)

    return Card(cost)
}