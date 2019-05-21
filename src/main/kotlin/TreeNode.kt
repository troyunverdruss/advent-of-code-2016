data class TreeNode<T>(val data: T, var parent: TreeNode<T>?) {
    val children: MutableSet<TreeNode<T>> = mutableSetOf()
    val depth: Int = parent?.depth?.plus(1) ?: 0

    init {
        parent?.let {
            parent!!.children.add(this)
        }
    }

    fun root(): TreeNode<T> {
        return parent?.root() ?: this
    }

    fun ancestors(): Set<TreeNode<T>> {
        parent?.let {
            return parent!!.ancestors().plus(this.parent!!)
        }
        return setOf()
    }

    fun descendants(): Set<TreeNode<T>> {
        return children.plus(children.flatMap { it.descendants() })
    }

    fun siblings(): Set<TreeNode<T>> {
        return parent?.children?.minus(this) ?: setOf()
    }
}