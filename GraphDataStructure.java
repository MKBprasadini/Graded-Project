import java.util.*;


class Graph {
    private final Map<String, Set<String>> adj = new HashMap<>();
    private final Map<String, String> canonical = new HashMap<>();

    public void addLocation(String name) {
        String k = keyOf(name);
        adj.putIfAbsent(k, new HashSet<>());
        canonical.putIfAbsent(k, name);
    }

    public void removeLocation(String name) {
        String k = keyOf(name);
        if (!adj.containsKey(k)) return;
        for (String n : new HashSet<>(adj.get(k))) {
            adj.getOrDefault(n, Collections.emptySet()).remove(k);
        }
        adj.remove(k);
        canonical.remove(k);
    }

    public boolean addRoad(String a, String b) {
        String A = keyOf(a), B = keyOf(b);
        if (!adj.containsKey(A) || !adj.containsKey(B)) return false;
        if (A.equals(B)) return false;
        adj.get(A).add(B);
        adj.get(B).add(A);
        return true;
    }

    public boolean removeRoad(String a, String b) {
        String A = keyOf(a), B = keyOf(b);
        if (!adj.containsKey(A) || !adj.containsKey(B)) return false;
        boolean r = adj.get(A).remove(B);
        r = adj.get(B).remove(A) || r;
        return r;
    }

    public boolean hasLocation(String name) {
        return adj.containsKey(keyOf(name));
    }

    // Returns neighbor keys (lowercase keys) for traversal
    public Set<String> neighborsKeySafe(String key) {
        if (key == null) return Collections.emptySet();
        String k = key.trim().toLowerCase();
        return Collections.unmodifiableSet(adj.getOrDefault(k, Collections.emptySet()));
    }

    // list unique edges as "A <-> B" (display names)
    public List<String> listEdges() {
        List<String> edges = new ArrayList<>();
        for (Map.Entry<String, Set<String>> e : adj.entrySet()) {
            String u = e.getKey();
            for (String v : e.getValue()) {
                if (u.compareTo(v) <= 0) {
                    edges.add(format(u) + " <-> " + format(v));
                }
            }
        }
        Collections.sort(edges);
        return edges;
    }

    public List<String> allLocationsDisplay() {
        List<String> list = new ArrayList<>(canonical.values());
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public String format(String key) {
        return canonical.getOrDefault(key, capitalize(key));
    }

    public Set<String> keys() {
        return Collections.unmodifiableSet(adj.keySet());
    }

    public int size() { return adj.size(); }

    private String keyOf(String name) {
        return name.trim().toLowerCase();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
