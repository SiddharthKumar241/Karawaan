import java.util.*;

class Edge {
    int destination, distance;

    Edge(int d, int dist) {
        destination = d;
        distance = dist;
    }
}

public class VITRoutePlanner {
    int n;
    ArrayList<ArrayList<Edge>> adj;
    static Map<String, Integer> nameToId = new HashMap<>();
    static String[] idToName = new String[50];

    static {
        String[] names = {
                "SJT Tower", "Technology Tower", "Perl Reach", "Park", "Mahatma Gandhi Block",
                "CV Raman Block", "Different Food Court", "Library", "Greenos", "SMV Canteen",
                "Anna Auditorium", "TT Parking", "Main Gate", "Silver Jubilee Tower", "Men's Hostel Block 1",
                "Men's Hostel Block 2", "Ladies Hostel Block A", "Ladies Hostel Block B", "Foodys", "TT Canteen",
                "Academic Block A", "Academic Block B", "Academic Block C", "TT Grounds", "Student Activity Center",
                "Medical Center", "Main Admin Block", "Zen Garden", "Placement Cell", "Energy Park",
                "Indoor Stadium", "Outdoor Ground", "Main Auditorium", "Counseling Center", "MBA Block",
                "School of Mechanical Engg", "School of Computer Science", "Exam Hall Complex", "Guest House",
                "Swimming Pool", "Nescafe", "Dominos", "CCD", "Workshop Block", "Sports Office",
                "Security Office", "Fire Station", "Transport Office", "Lecture Hall Complex"
        };
        for (int i = 0; i < names.length; i++) {
            nameToId.put(names[i].toLowerCase(), i);
            idToName[i] = names[i];
        }
    }

    VITRoutePlanner(int n) {
        this.n = n;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++)
            adj.add(new ArrayList<>());
    }

    void addPath(int from, int to, int dist) {
        adj.get(from).add(new Edge(to, dist));
        adj.get(to).add(new Edge(from, dist));
    }

    List<Integer> shortestPath(int start, int end) {
        int[] dist = new int[n], prev = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[] { start, 0 });

        while (!pq.isEmpty()) {
            int u = pq.poll()[0];
            if (visited[u])
                continue;
            visited[u] = true;
            if (u == end)
                break;

            for (Edge e : adj.get(u)) {
                int v = e.destination, w = e.distance;
                if (!visited[v] && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    prev[v] = u;
                    pq.offer(new int[] { v, dist[v] });
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        if (dist[end] == Integer.MAX_VALUE)
            return path;
        for (int at = end; at != -1; at = prev[at])
            path.add(at);
        Collections.reverse(path);
        return path;
    }

    static String getName(int id) {
        if (id >= 0 && id < idToName.length)
            return idToName[id];
        return "Unknown";
    }

    static void printLocations() {
        int cols = 3;
        for (int i = 0; i < idToName.length; i++) {
            if (idToName[i] != null) {
                System.out.printf("%-35s", "- " + idToName[i]);
                if ((i + 1) % cols == 0)
                    System.out.println();
            }
        }
        System.out.println();
    }

    static void printWithDelay(String text, int delayMillis) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    static void printInteractiveRoute(List<Integer> path) {
        if (path.isEmpty()) {
            System.out.println("No path found.");
            return;
        }

        printWithDelay("Route:", 50);
        printWithDelay("Start: " + getName(path.get(0)), 50);

        for (int i = 1; i < path.size(); i++) {
            printWithDelay("   |", 30);
            printWithDelay("   |", 30);

            if (i == path.size() - 1) {
                printWithDelay("Destination: " + getName(path.get(i)), 50);
            } else {
                printWithDelay(getName(path.get(i)), 50);
            }
        }
    }

    public static void main(String[] args) {
        VITRoutePlanner planner = new VITRoutePlanner(50);

        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            for (int j = i + 1; j < 50; j++) {
                if (rnd.nextInt(100) < 10) {
                    planner.addPath(i, j, 100 + rnd.nextInt(400));
                }
            }
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Available locations:");
        printLocations();
        System.out.println("Enter start location (Suggestions):");
        String startName = sc.nextLine().trim().toLowerCase();

        System.out.println("Available locations:");
        printLocations();
        System.out.println("Enter end location (Suggestions):");
        String endName = sc.nextLine().trim().toLowerCase();

        Integer start = nameToId.get(startName);
        Integer end = nameToId.get(endName);

        if (start == null || end == null) {
            System.out.println("Invalid start or end location.");
            sc.close();
            return;
        }

        List<Integer> path = planner.shortestPath(start, end);
        printInteractiveRoute(path);

        sc.close();
    }
}
