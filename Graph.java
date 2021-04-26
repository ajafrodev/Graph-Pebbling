import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Graph {

    public int[][] G_adj; //adjacency matrix of the graph
    public double[] profit; //profits of each node
    public int n; //number of vertices
    private int[][] rooted; //adjacency matrix that is rooted
    private double[] p; //pebbled profit
    private double[] not_p; //not pebbled profit
    private ArrayList<Integer> pebbled; //pebbled vertices giving max profit

    /**
     * Read and store graph file information. First file line is the number
     * vertices which is store in n. The next n lines is an nxn adjacency matrix.
     * This is stored in the G_adj 2d matrix. Last line is the the profit of each
     * vertex and is stored in the profit array.
     */
    public Graph(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        n = Integer.parseInt(scan.nextLine());
        G_adj =  new int[n][n];
        profit = new double[n];
        for (int i = 0; i < n; i++) {
            String[] line = scan.nextLine().split(" ");
            for (int j = 0; j < line.length; j++) {
                G_adj[i][j] = Integer.parseInt(line[j]);
            }
        }
        String[] last_line = scan.nextLine().split(" ");
        for (int i = 0; i < last_line.length; i++) {
            profit[i] = Double.parseDouble(last_line[i]);
        }
    }

    /**
     * Find the height of the graph given a root vertex.
     */
    public int height(int V) {
        boolean[] visited = new boolean[n];
        return search(visited, V);
    }

    /**
     * Helper function for the height function. It will recursively
     * traverse through the graph and find the max possible height.
     */
    private int search(boolean[] visited, int V) {
        visited[V] = true;
        ArrayList<Integer> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (G_adj[V][i] == 1 && !visited[i]) {
                children.add(i);
            }
        }
        int longest_height = -1;
        for (Integer child : children) {
            int current_height = search(visited, child);
            if (longest_height < current_height) {
                longest_height = current_height;
            }
        }
        return 1 + longest_height;
    }

    /**
     * This function will take in a root vertex and return the list of pebbled
     * vertices that will achieve the maximum profit. First the adjacency matrix
     * will have to be rooted with the given root vertex (direct the graph downwards).
     * Then we will find the max profit achievable and create 2 arrays. Using the
     * information that was previously attained, we will find determine the proper
     * pebbling of the graph that will result in the max profit.
     */
    public ArrayList<Integer> pebbling(int V) {
        pebbled = new ArrayList<>();
        rooted = G_adj;
        boolean[] visited = new boolean[n];
        root(visited, V);
        p = new double[n];
        not_p = new double[n];
        Arrays.fill(p, -1.0);
        Arrays.fill(not_p, -1.0);
        p[V] = pebbled(V);
        not_p[V] = not_pebbled(V);
        if (p[V] > not_p[V]) {
            pebbled.add(V);
            ArrayList<Integer> children = rooted_children(V);
            for (Integer child : children) {
                find_pebbles(child);
            }
        } else {
            find_pebbles(V);
        }
        return pebbled;
    }

    /**
     * Helper function for the pebbling function. This will recursively
     * recreate an adjacency matrix such that it is directed downwards
     * from the given root.
     */
    private void root(boolean[] visited, int V) {
        ArrayList<Integer> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (rooted[V][i] == 1 && !visited[i]) {
                children.add(i);
            }
        }
        for (Integer child : children) {
            rooted[child][V] = 0;
            root(visited, child);
        }
    }

    /**
     * Helper function for the pebbling function. This will recursively find
     * the proper pebbling given pebbled and not pebbled array.
     */
    private void find_pebbles(int V) {
        ArrayList<Integer> children = rooted_children(V);
        for (Integer child : children) {
            if (p[child] > not_p[child]) {
                pebbled.add(child);
                ArrayList<Integer> g_children = rooted_children(child);
                for (Integer g_child : g_children) {
                    find_pebbles(g_child);
                }
            } else {
                find_pebbles(child);
            }
        }
    }

    /**
     * Helper function for the pebbling function. This will
     * determine the profit given a pebbled vertex.
     */
    private double pebbled(int u) {
        double total = 0;
        ArrayList<Integer> children = rooted_children(u);
        if (children.size() == 0) {
            return profit[u];
        }
        for (Integer child : children) {
            if (not_p[child] == -1.0) {
                not_p[child] = not_pebbled(child);
            }
            total += not_p[child];
        }
        return profit[u] + total;
    }

    /**
     * Helper function for the pebbling function. This will
     * determine the profit given a non-pebbled vertex.
     */
    private double not_pebbled(int u) {
        double total = 0;
        ArrayList<Integer> children = rooted_children(u);
        if (children.size() == 0) {
            return 0;
        }
        for (Integer child : children) {
            if (p[child] == -1.0) {
                p[child] = pebbled(child);
            }
            if (not_p[child] == -1.0) {
                not_p[child] = not_pebbled(child);
            }
            total += Math.max(p[child], not_p[child]);
        }
        return total;
    }

    /**
     * General helper function to find the children of a
     * given vertex. This is only applicable to rooted trees.
     * Using this in a non-rooted tree will give the neighbors
     * of the given vertex.
     */
    private ArrayList<Integer> rooted_children(int u) {
        ArrayList<Integer> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (rooted[u][i] == 1) {
                children.add(i);
            }
        }
        return children;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Graph sample = new Graph("input1.txt");
        Graph graph1 = new Graph("task2input1.txt");
        Graph graph2 = new Graph("task2input2.txt");
        Graph graph3 = new Graph("task2input3.txt");
        Graph graph4 = new Graph("task2input4.txt");
        Graph graph5 = new Graph("task2input5.txt");
        System.out.println("sample: " + sample.pebbling(0));
        System.out.println("task2input1: " + graph1.pebbling(0));
        System.out.println("task2input2: " + graph2.pebbling(0));
        System.out.println("task2input3: " + graph3.pebbling(0));
        System.out.println("task2input4: " + graph4.pebbling(0));
        System.out.println("task2input5: " + graph5.pebbling(0));
    }

}

