import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;


/**
 * Definition for a binary tree node.
 */
class TreeNode {
    
    // **** class members ****
    int         val;
    TreeNode    left;
    TreeNode    right;
    
    // **** constructors ****
    TreeNode() {}
    
    TreeNode(int val) { this.val = val; }
    
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
  
    // **** ****
    @Override
    public String toString() {
        return "" + this.val;
    }
}


/**
 * Generic Pair class.
 * Was not able to get VSCode to import the Pair class.
 */
class Pair<K, V> {

    // **** class members ****
    K key;
    V value;

    // **** constructors ****
    public static <K, V> Pair<K, V> createPair(K key, V value) {
        return new Pair<K, V>(key, value);
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    // **** getter ****
    public K getKey() {
        return key;
    }

    // **** setter ****
    public V getValue() {
        return value;
    }

    // **** ****
    @Override
    public String toString() {
        return "(" + this.getKey() + "," + this.getValue() + ")";
    }
}


/**
 * Compare Pair objects to be sorted by key.
 * 
 * !!! NO LONGER USED IN THE SOLUTION !!!
 */
class PairSortByKey implements Comparator<Pair<Integer, Integer>> 
{ 

    public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b) { 

        int result = 0;

        if (a.getKey() > b.getKey()) {
            result = 1;
        } else if (a.getKey() == b.getKey()) {
            result = 0;
        } else {
            result = -1;
        }

        return result;
    }

} 


/**
 * LeetCode problem 314. Binary Tree Vertical Order Traversal
 * https://leetcode.com/problems/binary-tree-vertical-order-traversal/
 */
public class Solution {


    // **** global variables (used by different implementations) ****
    static List<ArrayList<Pair<Integer, Integer>>> depthLists = null;
    static int minD = Integer.MAX_VALUE;
    static int maxD = Integer.MIN_VALUE;


    /**
     * Compute the horizontal distance from the root for each node.
     * Recursive call.
     * DFS approach.
     */
    static void distAndDepth(TreeNode root, int dist, int depth) {

        // **** base case ****
        if (root == null)
            return;

        // **** update the max and min distances ****
        minD = Math.min(dist, minD);
        maxD = Math.max(dist, maxD);

        // **** visit left node ****
        distAndDepth(root.left, dist - 1, depth + 1);

        // **** add empty lists (if needed) ****
        while (depthLists.size() <= depth)
            depthLists.add(new ArrayList<>());

        // **** get the list associated with the specified depth ****
        ArrayList<Pair<Integer, Integer>> dl = depthLists.get(depth);

        // **** insert pair in the list ****
        dl.add(new Pair<Integer, Integer>(dist, root.val));

        // **** visit right node ****
        distAndDepth(root.right, dist + 1, depth + 1);
    }


    /**
     * Binary tree vertical order traversal.
     * 
     * Runtime: 2 ms, faster than 89.12% of Java online submissions.
     * Memory Usage: 39.9 MB, less than 5.67% of Java online submissions.
     */
    static List<List<Integer>> verticalOrder0(TreeNode root) {

        // **** list of list to return results ****
        List<List<Integer>> lol = new ArrayList<>();

        // **** perform sanity checks ****
        if (root == null)
            return lol;

        // **** initialize global variables ****
        depthLists  = new ArrayList<ArrayList<Pair<Integer, Integer>>>();
        minD        = Integer.MAX_VALUE;
        maxD        = Integer.MIN_VALUE;

        // **** populate the node distances from the root ****
        distAndDepth(root, 0, 1);

        // **** initialize the list of lists ****
        for (int i = minD; i <= maxD; i++) {
            lol.add(new ArrayList<Integer>());
        }

        // **** populate the list of lists ****
        depthLists.forEach( a -> {
            a.forEach( b -> {
                lol.get(b.getKey() + ((minD < 0) ? Math.abs(minD) : 0)).add(b.getValue());
            });
        });

        // **** return list of lists ****
        return lol;
    }


    /**
     * Binary tree vertical order traversal.
     * Implements a BFS traversal.
     * 
     * Runtime: 4 ms, faster than 34.33% of Java online submissions.
     * Memory Usage: 39.4 MB, less than 5.60% of Java online submissions.
     */
    static List<List<Integer>> verticalOrder(TreeNode root) {

        // **** list of list to return results ****
        List<List<Integer>> lol = new ArrayList<>();
    
        // **** perform sanity checks ****
        if (root == null) {
          return lol;
        }
    
        // **** ****
        Map<Integer, ArrayList<Integer>> hm = new HashMap<>();
        Queue<Pair<TreeNode, Integer>> q = new ArrayDeque<>();

        // **** start with the center column (root node) ****
        int column = 0;

        // **** prime the queue ****
        q.offer(new Pair<>(root, column));
    
        // **** traverse the binary tree by levels ****
        while (!q.isEmpty()) {

            // **** get the next pair (node,column) from the queue ****
            Pair<TreeNode, Integer> pair = q.poll();

            // **** for ease of use ****
            root      = pair.getKey();
            column    = pair.getValue();
    
            // **** ****
            if (root != null) {

                // **** ****
                if (!hm.containsKey(column)) {
                    hm.put(column, new ArrayList<Integer>());
                }

                // **** ****
                hm.get(column).add(root.val);

                // **** push into the queue the left child ****
                q.offer(new Pair<>(root.left, column - 1));

                // **** push into the queue the right child ****
                q.offer(new Pair<>(root.right, column + 1));
            }
        }
    
        // **** columns (not sorted yet) ****
        List<Integer> sortedKeys = new ArrayList<Integer>(hm.keySet());

        // **** sort the columns in ascending order ****
        Collections.sort(sortedKeys);

        // **** traverse the sorted columns adding the associated node values ****
        for (int k : sortedKeys) {
            lol.add(hm.get(k));
        }
    
        // **** return a list of lists ****
        return lol;
    }


    /**
     * Enumerate which child in the node at the head of the queue 
     * (see insertValue function).
     */
    enum Child {
        LEFT,
        RIGHT
    }


    // **** child turn to insert on node at head of queue ****
    static Child  insertChild = null;


    /**
     * This function inserts the next value into the specified BST.
     * This function is called repeatedly from the populateTree method.
     * This function supports 'null' value.
     */
    static TreeNode insertValue(TreeNode root, String strVal, Queue<TreeNode> q) {
    
        // **** node to add to the BST in this pass ****
        TreeNode node = null;
    
        // **** create a node (if needed) ****
        if (!strVal.equals("null"))
            node = new TreeNode(Integer.parseInt(strVal));
    
        // **** check is the BST is empty (this becomes the root node) ****
        if (root == null)
            root = node;
    
        // **** add node to left child (if possible) ****
        else if (insertChild == Child.LEFT) {
        
            // **** add this node as the left child ****
            if (node != null)
                q.peek().left = node; 
            
            // **** for next pass ****
            insertChild = Child.RIGHT;
        }
    
        // **** add node to right child (if possible) ****
        else if (insertChild == Child.RIGHT) {
        
            // **** add this node as a right child ****
            if (node != null)
                q.peek().right = node;
    
            // **** remove node from queue ****
            q.remove();
    
            // **** for next pass ****
            insertChild = Child.LEFT;
        }
    
        // **** add this node to the queue (if NOT null) ****
        if (node != null)
            q.add(node);
        
        // **** return the root of the BST ****
        return root;
    }


    /**
     * This function populates a binary tree in level order as 
     * specified by the data array.
     * This function supports 'null' values.
     */
    static TreeNode populateTree(String[] data) {
    
        // **** root for the BT ****
        TreeNode root = null;
    
        // **** auxiliary queue ****
        Queue<TreeNode> q = new LinkedList<TreeNode>();

        // **** start with the left child ****
        insertChild = Child.LEFT;

        // **** traverse the array of values inserting nodes 
        //      one into the binary tree one at a time ****
        for (String strVal : data)
            root = insertValue(root, strVal, q);
    
        // **** return the root of the populated binary tree ****
        return root;
    }
    

    /**
     * Traverse the specified binary tree displaying the values in depth first
     * search order.
     * This method is used to verify that the binary tree was properly generated.
     */
    static void inOrder(TreeNode root) {
    
        // **** end condition ****
        if (root == null)
            return;
    
        // **** visit the left sub tree ****
        inOrder(root.left);
    
        // **** display the value of this node ****
        System.out.print(root.val + " ");
    
        // **** visit the right sub tree ****
        inOrder(root.right);
    }


    /**
     * Traverse the specified binary tree displaying the values in breadth-first 
     * order.
     * This method is used to verify that the binary tree was properly generated.
     */
    static void bfs(TreeNode root) {

        // **** perform sanity checks ****
        if (root == null)
            return;

        // **** initialize queues ****
        Queue<TreeNode> q       = new LinkedList<TreeNode>();
        Queue<TreeNode> nextQ   = new LinkedList<TreeNode>();

        // **** prime the queue ****
        q.add(root);

        // **** loop displaying and inserting nodes into the queue ****
        while (!q.isEmpty()) {

            // **** get the next node from the queue ****
            TreeNode node = q.remove();

            // **** display the node value ****
            System.out.print(node.val + " ");

            // **** push into the next queue the left child (if needed) ****
            if (node.left != null)
                nextQ.add(node.left);

            // **** push into the next queue the right child (if needed) ****
            if (node.right != null)
                nextQ.add(node.right);
        
            // **** switch queues ****
            if (q.isEmpty()) {

                // **** mark end of level ****
                System.out.println();

                // **** point to the next queue ****
                q = nextQ;

                // **** clear the next queue ****
                nextQ  = new LinkedList<TreeNode>();
            }
        }
    }


    /**
     * BFS approach.
     * Uses a hash map.
     * 
     * Runtime: 2 ms, faster than 89.12% of Java online submissions.
     * Memory Usage: 39.5 MB, less than 5.67% of Java online submissions
     */
    static List<List<Integer>> verticalOrder2(TreeNode root) {

        // **** list of list to return results ****
        List<List<Integer>> lol = new ArrayList<>();
    
        // **** perform sanity checks ****
        if (root == null) {
          return lol;
        }
    
        // **** ****
        Map<Integer, ArrayList<Integer>> hm = new HashMap<>();
        Queue<Object[]> q = new ArrayDeque<>();

        // **** keep track of min and max distances ****
        int minDist = Integer.MAX_VALUE;
        int maxDist = Integer.MIN_VALUE;

        // **** prime the queue ****
        q.offer(new Object[]{root, 0});
    
        // **** traverse the binary tree by levels ****
        while (!q.isEmpty()) {

            // **** get the next object from the queue ****
            Object[] objs = q.poll();

            // **** for ease of use ****
            TreeNode node   = (TreeNode)objs[0];
            int dist        = (int)objs[1];

            // **** update min and max distances ****
            minDist = Math.min(dist, minDist);
            maxDist = Math.max(dist, maxDist);

            // **** ****
            ArrayList<Integer> distList = hm.getOrDefault(dist, new ArrayList<Integer>());

            // **** ****
            distList.add(node.val);

            // **** ****
            hm.put(dist, distList);

            // **** push left child into the queue (if needed) ****
            if (node.left != null)
                q.offer(new Object[]{node.left, dist - 1});

            // **** push right node into the queue (if needed) ****
            if (node.right != null)
                q.offer(new Object[]{node.right, dist + 1});
        }

        // **** ****
        for (int i = minDist; i <= maxDist; i++) {
            lol.add(hm.get(i));
        }

        // **** return a list of lists ****
        return lol;
    }


    /**
     * Test scaffolding
     * Not part of solution
     */
    public static void main(String[] args) {
        
        // **** open scanner ****
        Scanner sc = new Scanner(System.in);

        // **** read and split the data for the binary tree ****
        String[] data = sc.nextLine().trim().split(",");

        // **** close scanner ****
        sc.close();

        // **** binary tree ****
        TreeNode bt = null;

        // **** populate the binary tree ****
        bt = populateTree(data);

        // ???? ????
        System.out.print("main <<< inOrder: ");
        inOrder(bt);
        System.out.println();

        // ???? ????
        System.out.println("main <<< bfs: ");
        bfs(bt);

        // **** display results ****
        System.out.println("main <<<  verticalOrder: " + verticalOrder(bt));

        // **** display results ****
        System.out.println("main <<< verticalOrder2: " + verticalOrder2(bt));

        // **** display results ****
        System.out.println("main <<< verticalOrder0: " + verticalOrder0(bt));

    }
}