import java.lang.IllegalStateException
import java.util.PriorityQueue
import kotlin.reflect.KFunction1


class AStarSearchQueue<T>(private val estimateDistanceFunction: KFunction1<T, Int>) {
    private val open: PriorityQueue<T> = PriorityQueue(compareBy { estimateDistanceFunction(it) })
    private val fLookup: MutableMap<Int, StepsAndEstimatePair> = mutableMapOf()
    private val closed: MutableMap<Int, StepsAndEstimatePair> = mutableMapOf()
    private var lastItem: T? = null
    private var lastStepsAndEstimatePair: StepsAndEstimatePair? = null

    fun enqueue(node: T, steps: Int) {
        if (node.hashCode() in fLookup && fLookup.getValue(node.hashCode()).f() < steps + estimateDistanceFunction(node)) {
            return
        }

        if (node.hashCode() in closed && closed.getValue(node.hashCode()).f() < steps + estimateDistanceFunction(node)) {
            return
        }

        if (node.hashCode() !in fLookup) {
            open.add(node)
            fLookup[node.hashCode()] = StepsAndEstimatePair(steps, estimateDistanceFunction(node))
        }

        val blah = StepsAndEstimatePair(1, 2)
    }

    fun isNotEmpty(): Boolean {
        return open.isNotEmpty()
    }

    fun open(): T {
        if (lastItem != null || lastStepsAndEstimatePair != null) {
            throw IllegalStateException("Last item was not closed, call .close(item) before .open()")
        }

        val item = open.poll()
        lastItem = item
        lastStepsAndEstimatePair = fLookup.remove(item.hashCode())
        return item
    }

    fun getLastItemStepCount(): Int {
        return lastStepsAndEstimatePair?.g ?: throw IllegalStateException("No current item, use .open() first")
    }

    fun getLastItemEstimate(): Int {
        return lastStepsAndEstimatePair?.h ?: throw IllegalStateException("No current item, use .open() first")
    }

    fun run(initialState: T, nextStatesFunction: KFunction1<T, List<T>>, endConditionFunction: KFunction1<T, Boolean>): AStarSearchQueueResult<T> {

        enqueue(initialState, 0)
        while (isNotEmpty()) {
            val test = open()

            for (nextState in nextStatesFunction(test)) {
                if (endConditionFunction(test)) {
                    clear()
                    return AStarSearchQueueResult(nextState, getLastItemStepCount() + 1)
                }
                enqueue(nextState, getLastItemStepCount() + 1)
            }
            close(test)
        }

        throw IllegalStateException("Unable to find solution for search!")
    }

    fun close(node: T) {
        lastItem = null
        closed[node.hashCode()] = lastStepsAndEstimatePair?.copy()
                ?: throw IllegalStateException("No current item, use .open() first ")
        lastStepsAndEstimatePair = null
    }

    fun queueSize(): Int {
        return open.size
    }

    fun clear() {
        open.clear()
        fLookup.clear()
        closed.clear()
        lastItem = null
        lastStepsAndEstimatePair = null
    }

    data class AStarSearchQueueResult<T>(val state: T, val steps: Int)

    private data class StepsAndEstimatePair(val g: Int, val h: Int) {
        fun f() = g + h
    }
}