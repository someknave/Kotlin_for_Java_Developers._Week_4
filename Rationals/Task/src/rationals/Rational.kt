package rationals

import java.math.BigDecimal
import java.math.BigInteger

data class Rational (val n: BigInteger, val d: BigInteger = BigInteger.ONE): Comparable<Rational> {
    operator fun plus(other: Rational): Rational {
        return (n * other.d + d * other.n) divBy (d * other.d)
    }
    operator fun minus(other: Rational): Rational {
        return (n * other.d - d * other.n) divBy (d * other.d)
    }
    operator fun times(other: Rational): Rational {
        return (n * other.n) divBy (d * other.d)
    }
    operator fun div(other: Rational): Rational {
        return (n * other.d) divBy (d * other.n)
    }
    operator fun unaryMinus(): Rational{
        return Rational(-n, d)
    }
    override fun compareTo(other: Rational): Int {
        return (n * other.d - d * other.n).signum()
    }
    operator fun rangeTo(end: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val endInclusive: Rational = end
            override val start: Rational = this@Rational
        }
    }


    override fun toString(): String {
        return if (d == BigInteger.ONE) n.toString() else "$n/$d"
    }
}

fun String.toRational(): Rational {
    val list = this.split('/')
    return when (list.size) {
        1 -> Rational(list[0].toBigInteger())
        2 -> { val n = list[0].toBigInteger()
            val d = list[1].toBigInteger()
            n divBy d
        }
        else -> throw IllegalArgumentException("Not a fraction.")
    }
}

infix fun <T : Number> T.divBy(other: T): Rational {

    val n = when {
        this is BigInteger -> this
        this is Long -> this.toBigInteger()
        else -> this.toLong().toBigInteger()
    }
    val d = when {
        other is BigInteger -> other
        other is Long -> other.toBigInteger()
        else -> other.toLong().toBigInteger()
    }
    val sign = when {
        d < BigInteger.ZERO -> -BigInteger.ONE
        d > BigInteger.ZERO -> BigInteger.ONE
        else -> throw IllegalArgumentException("Cannot divide by zero")
    }
    val gcd = n.gcd(d)
    return Rational(n * sign / gcd, d * sign / gcd)
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
    val test = "20395802948019459839003802001190283020/32493205934869548609023910932454365628".toRational()
    println("New")
    println(test)
    println(test > 0 divBy 1)
    println(test > third)
    println(test > half)
    println(test > twoThirds)
    println(test in third..half)
    println(test in half..twoThirds)


}