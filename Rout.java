class LocationManager {
    private final Graph graph;
    private final AVLTree tree;

    public LocationManager(Graph g, AVLTree t) { this.graph = g; this.tree = t; }

        if (name == null || name.trim().isEmpty()) {
            System.out.println(" Location name cannot be empty.");
            return;
        }
        if (tree.contains(name) || graph.hasLocation(name)) {
            System.out.println(" Location already exists.");
            return;
        }
        tree.insert(name);
        graph.addLocation(name);
        System.out.println(" Location added: " + name);
    }

    public void removeLocation(String name) {
        if (!tree.contains(name) || !graph.hasLocation(name)) {
            System.out.println(" Location not found.");
            return;
        }
        tree.delete(name);
        graph.removeLocation(name);
        System.out.println(" Location removed: " + name);
    }

    public void addRoad(String a, String b) {
        if (!tree.contains(a) || !tree.contains(b)) {
            System.out.println(" Both locations must exist before adding a road.");
            return;
        }
        if (graph.addRoad(a, b)) System.out.println(" Road added between " + a + " and " + b);
        else System.out.println(" Failed to add road (may already exist or invalid).");
    }

    public void removeRoad(String a, String b) {
        if (graph.removeRoad(a, b)) System.out.println(" Road removed between " + a + " and " + b);
        else System.out.println(" Road not found.");
    }
}
