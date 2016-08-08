import java.util.*;

class SuffixTreeNode {
    /*
     * node is 0 if it is root, positive if leaf, negative if internal node
     */
    int node;

    /*
     * edge connecting two nodes, denoted by start and end positions.
     */
    int start;
    int end;

    Map<Character, SuffixTreeNode> children;
    SuffixTreeNode parent;

    public SuffixTreeNode(int node, int start, int end, SuffixTreeNode parent) {
        this.node = node;
        this.start = start;
        this.end = end;
        this.children = new HashMap<Character, SuffixTreeNode>();
        this.parent = parent;
    }
    
    public int edgeLength() {
        return end - start + 1;
    }
}
