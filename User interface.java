public class SmartCityPlanner {
    private static final Scanner IN = new Scanner(System.in);
    private static final Graph graph = new Graph();
    private static final AVLTree tree = new AVLTree();
    private static final LocationManager manager = new LocationManager(graph, tree);

    public static void main(String[] args) {
        System.out.println(" Smart City Route Planner ");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> doAddLocation();
                case 2 -> doRemoveLocation();
                case 3 -> doAddRoad();
                case 4 -> doRemoveRoad();
                case 5 -> doDisplayConnections();
                case 6 -> doDisplayLocations();
                case 7 -> { System.out.println("Goodbye!"); running = false; }
                default -> System.out.println(" Invalid option — enter 1-7.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Add a new location");
        System.out.println("2. Remove a location");
        System.out.println("3. Add a road between locations");
        System.out.println("4. Remove a road");
        System.out.println("5. Display all connections (and traversals)");
        System.out.println("6. Display all locations (AVL tree in-order)");
        System.out.println("7. Exit");
    }

    private static void doAddLocation() {
        String name = readNonEmptyString("Enter new location name: ");
        manager.addLocation(name);
    }

    private static void doRemoveLocation() {
        String name = readNonEmptyString("Enter location name to remove: ");
        manager.removeLocation(name);
    }

    private static void doAddRoad() {
        String a = readNonEmptyString("Enter first location: ");
        String b = readNonEmptyString("Enter second location: ");
        manager.addRoad(a, b);
    }

    private static void doRemoveRoad() {
        String a = readNonEmptyString("Enter first location: ");
        String b = readNonEmptyString("Enter second location: ");
        manager.removeRoad(a, b);
    }

    private static void doDisplayConnections() {
        List<String> edges = graph.listEdges();
        if (edges.isEmpty()) System.out.println("No roads in the network.");
        else {
            System.out.println("\n--- All Roads ---");
            edges.forEach(System.out::println);
        }

        if (graph.size() == 0) return;

        System.out.print("\nWould you like a traversal demo? (y/n): ");
        String ans = IN.nextLine().trim().toLowerCase();
        if (!ans.equals("y")) return;

        String start = readNonEmptyString("Enter start location for traversal: ");
        if (!graph.hasLocation(start)) { System.out.println(" Start location not found."); return; }

        System.out.println("Choose traversal: 1) BFS (queue)   2) DFS (stack)");
        int t = readInt("Enter 1 or 2: ");
        String startKey = start.trim().toLowerCase();
        if (t == 1) {
            List<String> order = bfs(startKey);
            System.out.println("BFS order: " + order);
        } else if (t == 2) {
            List<String> order = dfs(startKey);
            System.out.println("DFS order: " + order);
        } else {
            System.out.println(" Invalid traversal choice.");
        }
    }

    private static void doDisplayLocations() {
        List<String> locs = tree.inOrder();
        if (locs.isEmpty()) System.out.println("No locations added yet.");
        else {
            System.out.println("\n--- Locations (AVL in-order) ---");
            locs.forEach(System.out::println);
        }
    }

    // BFS using neighbor keys
    private static List<String> bfs(String startKey) {
        List<String> order = new ArrayList<>();
        Queue<String> q = new LinkedList<>();
        Set<String> vis = new HashSet<>();
        q.add(startKey); vis.add(startKey);
        while (!q.isEmpty()) {
            String u = q.poll();
            order.add(graph.format(u));
            for (String vKey : graph.neighborsKeySafe(u)) {
                if (!vis.contains(vKey)) {
                    vis.add(vKey); q.add(vKey);
                }
            }
        }
        return order;
    }

    // DFS using stack
    private static List<String> dfs(String startKey) {
        List<String> order = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        Set<String> vis = new HashSet<>();
        stack.push(startKey);
        while (!stack.isEmpty()) {
            String u = stack.pop();
            if (vis.contains(u)) continue;
            vis.add(u);
            order.add(graph.format(u));
            List<String> neigh = new ArrayList<>(graph.neighborsKeySafe(u));
            Collections.sort(neigh); Collections.reverse(neigh);
            for (String v : neigh) if (!vis.contains(v)) stack.push(v);
        }
        return order;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim();
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println(" Please enter a valid integer."); }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println(" Input cannot be empty.");
        }
    }
}