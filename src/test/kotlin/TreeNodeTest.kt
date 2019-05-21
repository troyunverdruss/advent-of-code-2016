import TreeNode
import org.testng.Assert.*
import org.testng.annotations.Test

class TreeNodeTest {
    @Test
    fun `create simple tree and test basic ops`() {
        val root = TreeNode<String>("root", null)
        val a = TreeNode("a", root)
        val b = TreeNode("b", root)
        val c = TreeNode("c", b)

        assertNull(root.parent)
        assertEquals(a.parent, root)
        assertEquals(b.parent, root)
        assertEquals(c.parent, b)

        assertEquals(a.root(), root)
        assertEquals(b.root(), root)
        assertEquals(c.root(), root)

        assertEquals(root.depth, 0)
        assertEquals(a.depth, 1)
        assertEquals(b.depth, 1)
        assertEquals(c.depth, 2)

        assertEquals(root.children, setOf(a,b))
        assertEquals(b.children, setOf(c))

        assertEquals(a.siblings(), setOf(b))

        assertEquals(root.descendants(), setOf(a, b, c))
        assertEquals(a.descendants(), setOf<TreeNode<String>>())
        assertEquals(b.descendants(), setOf(c))

        assertEquals(a.ancestors(), setOf(root))
        assertEquals(b.ancestors(), setOf(root))
        assertEquals(c.ancestors(), setOf(b, root))
    }
}