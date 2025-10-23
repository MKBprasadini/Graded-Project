class AVLTree {
    private class Node {
        String key;
        Node left, right;
        int height;
        Node(String k) { key = k; height = 1; }
    }

    private Node root;

    public boolean contains(String key) { return containsRec(root, key); }
    private boolean containsRec(Node node, String key) {
        if (node == null) return false;
        int cmp = key.compareToIgnoreCase(node.key);
        if (cmp == 0) return true;
        return cmp < 0 ? containsRec(node.left, key) : containsRec(node.right, key);
    }

    public void insert(String key) { root = insertRec(root, key); }
    private Node insertRec(Node node, String key) {
        if (node == null) return new Node(key);
        int cmp = key.compareToIgnoreCase(node.key);
        if (cmp < 0) node.left = insertRec(node.left, key);
        else if (cmp > 0) node.right = insertRec(node.right, key);
        else return node;
        updateHeight(node);
        return balance(node);
    }

    public void delete(String key) { root = deleteRec(root, key); }
    private Node deleteRec(Node node, String key) {
        if (node == null) return null;
        int cmp = key.compareToIgnoreCase(node.key);
        if (cmp < 0) node.left = deleteRec(node.left, key);
        else if (cmp > 0) node.right = deleteRec(node.right, key);
        else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node succ = minValue(node.right);
                node.key = succ.key;
                node.right = deleteRec(node.right, succ.key);
            }
        }
        if (node == null) return null;
        updateHeight(node);
        return balance(node);
    }

    private Node minValue(Node node) { while (node.left != null) node = node.left; return node; }
    private void updateHeight(Node n) { n.height = 1 + Math.max(height(n.left), height(n.right)); }
    private int height(Node n) { return n == null ? 0 : n.height; }

    private Node rotateRight(Node y) {
        Node x = y.left; Node T2 = x.right;
        x.right = y; y.left = T2;
        updateHeight(y); updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right; Node T2 = y.left;
        y.left = x; x.right = T2;
        updateHeight(x); updateHeight(y);
        return y;
    }

    private Node balance(Node node) {
        int bf = height(node.left) - height(node.right);
        if (bf > 1) {
            if (height(node.left.left) < height(node.left.right)) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1) {
            if (height(node.right.right) < height(node.right.left)) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    public List<String> inOrder() {
        List<String> out = new ArrayList<>();
        inOrderRec(root, out);
        return out;
    }
    private void inOrderRec(Node node, List<String> out) {
        if (node == null) return;
        inOrderRec(node.left, out);
        out.add(node.key);
        inOrderRec(node.right, out);
    }
}