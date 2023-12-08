package day07

import println
import readLines

data class Card(val char: Char, val jokerIsWildcard: Boolean = false): Comparable<Card> {
    override fun compareTo(other: Card): Int = getOrdering() - other.getOrdering()

    private fun getOrdering(): Int = when (char) {
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'T' -> 10
        'J' -> if (jokerIsWildcard) 1 else 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> throw IllegalArgumentException("Not supported card char $char.")
    }
}

open class HandBid(val hand: List<Card>, val bid: Int): Comparable<HandBid> {
    private val cardCount: Map<Card, Int> = hand.groupingBy { it }.eachCount()
    override fun compareTo(other: HandBid): Int {
        val orderingDiff = getTypeOrdering() - other.getTypeOrdering()
        if (orderingDiff != 0) {
            return orderingDiff
        }

        // Do lexicographically comparison of lists of cards
        for (i in 0..4) {
            if (hand[i] != other.hand[i]) {
                return hand[i].compareTo(other.hand[i])
            }
        }

        // This should not occur. No two hands are the same.
        return 0
    }

    private fun getTypeOrdering(): Int = when {
        isFiveOfAKind() -> 7
        isFourOfAKind() -> 6
        isFullHouse() -> 5
        isThreeOfAKind() -> 4
        isTwoPair() -> 3
        isOnePair() -> 2
        else -> 1
    }

    // Functions below assume they are checked in order. E.g. isThreeOfAKind will also return true for full house,
    // but assumes isFullHouse is checked first.
    open fun isFiveOfAKind(): Boolean {
        return cardCount.containsValue(5)
    }

    open fun isFourOfAKind(): Boolean {
        return cardCount.containsValue(4)
    }

    open fun isFullHouse(): Boolean {
        return cardCount.containsValue(3) && cardCount.containsValue(2)
    }

    open fun isThreeOfAKind(): Boolean {
        return cardCount.containsValue(3)
    }

    open fun isTwoPair(): Boolean {
        return cardCount.count { it.value == 2 } == 2
    }

    open fun isOnePair(): Boolean {
        return cardCount.containsValue(2)
    }
}

class CompositeHandBid(hand: List<Card>, bid: Int) : HandBid(hand, bid) {
    private val childHands = generateChildHands(hand).map { HandBid(it, bid) }

    private fun generateChildHands(hand: List<Card>): List<List<Card>> {
        if (!hand.contains(joker)) {
            return listOf(hand)
        }

        val jokerIdx = hand.indexOf(joker)

        return normalCards.flatMap { card ->
            val firstPart = hand.subList(0, jokerIdx)
            val lastPart = hand.subList(jokerIdx + 1, hand.size)
            val newHand = firstPart + card + lastPart
            generateChildHands(newHand)
        }
    }

    override fun isFiveOfAKind(): Boolean {
        return childHands.any { it.isFiveOfAKind() }
    }

    override fun isFourOfAKind(): Boolean {
        return childHands.any { it.isFourOfAKind() }
    }

    override fun isFullHouse(): Boolean {
        return childHands.any { it.isFullHouse() }
    }

    override fun isThreeOfAKind(): Boolean {
        return childHands.any { it.isThreeOfAKind() }
    }

    override fun isTwoPair(): Boolean {
        return childHands.any { it.isTwoPair() }
    }

    override fun isOnePair(): Boolean {
        return childHands.any { it.isOnePair() }
    }

    companion object {
        val normalCards = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').map { Card(it, true) }
        val joker = Card('J', true)
    }
}

fun toHand(handStr: String, jokerIsWildcard: Boolean): List<Card> {
    check(handStr.length == 5)
    return handStr.map { Card(it, jokerIsWildcard) }
}

fun part1(lines: List<String>): Int {
    val handBids = lines.map {
        val hand = it.substring(0, 5)
        val bid = it.substring(6).toInt()
        HandBid(toHand(hand, false), bid)
    }

    val sortedHandBids = handBids.sorted()

    return sortedHandBids.withIndex().sumOf { (idx, handBid) ->
        (idx + 1) * handBid.bid
    }
}

fun part2(lines: List<String>): Int {
    val handBids = lines.map {
        val hand = it.substring(0, 5)
        val bid = it.substring(6).toInt()
        CompositeHandBid(toHand(hand, true), bid)
    }

    val sortedHandBids = handBids.sorted()

    return sortedHandBids.withIndex().sumOf { (idx, handBid) ->
        (idx + 1) * handBid.bid
    }
}

fun main() {
    val lines = readLines("Day07.txt")

    part1(lines).println()
    part2(lines).println()
}