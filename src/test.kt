import java.util.*


interface Circular<T> : Iterable<T> {
    fun state(): T
    fun inc()
    fun isZero(): Boolean   // `true` in exactly one state
    fun hasNext(): Boolean  // `false` if the next state `isZero()`

    override fun iterator() : Iterator<T> {
        return object : Iterator<T> {
            var started = false

            override fun next(): T {
                if(started) {
                    inc()
                } else {
                    started = true
                }

                return state()
            }

            override fun hasNext() = this@Circular.hasNext()
        }
    }
}

class Ring(val size: Int) : Circular<Int> {
    private var state = 0

    override fun state() = state
    override fun inc() {state = (1 + state) % size}
    override fun isZero() = (state == 0)
    override fun hasNext() = (state != size - 1)

    init {
        assert(size > 0)
    }
}

abstract class CircularList<E, H: Circular<E>>(val size: Int) : Circular<List<E>> {
    protected abstract val state: List<H>  // state.size == size

    override fun inc() {
        state.forEach {
            it.inc()
            if(! it.isZero()) return
        }
    }

    override fun isZero() = state.all {it.isZero()}
    override fun hasNext() = state.any {it.hasNext()}
}

abstract class IntCombinations(size: Int) : CircularList<Int, Ring>(size)

class Permutations(n: Int) : IntCombinations(n) {
    override val state = mutableListOf<Ring>()

    init {
        for(i in n+1 downTo 1) {
            state += Ring(i)
        }
    }

    override fun state(): List<Int> {
        val items = (0 until size+1).toCollection(LinkedList())
        return state.map {ring -> items.removeAt(ring.state())}
    }
}


fun main() {

    for(configuration in Permutations(4)) {
        println(configuration)
    }
}
