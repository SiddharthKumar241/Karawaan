import java.util.*;

class Edge {
    int destination;
    int distance;

    Edge(int destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
}

public class VITRoutePlanner {

    int numberOfLocations;
    ArrayList<ArrayList<Edge>> adjacencyList;
    static Map<String, Integer> locationNameToId = new HashMap<>();
    static String[] idToLocationName = new String[50];

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
            locationNameToId.put(names[i].toLowerCase(), i);
            idToLocationName[i] = names[i];
        }
    }

    VITRoutePlanner(int numberOfLocations) {
        this.numberOfLocations = numberOfLocations;
        adjacencyList = new ArrayList<>();
        for (int i = 0; i < numberOfLocations; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    void addPath(int from, int to, int distance) {
        adjacencyList.get(from).add(new Edge(to, distance));
        adjacencyList.get(to).add(new Edge(from, distance));
    }

    List<Integer> findShortestPath(int start, int end) {
        int[] dist = new int[numberOfLocations];
        int[] prev = new int[numberOfLocations];
        boolean[] visited = new boolean[numberOfLocations];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        queue.offer(new int[]{start, 0});

        while (!queue.isEmpty()) {
            int[] node = queue.poll();
            int u = node[0];

            if (visited[u]) continue;
            visited[u] = true;

            if (u == end) break;

            for (Edge edge : adjacencyList.get(u)) {
                int v = edge.destination;
                int weight = edge.distance;
                int newDist = dist[u] + weight;

                if (!visited[v] && newDist < dist[v]) {
                    dist[v] = newDist;
                    prev[v] = u;
                    queue.offer(new int[]{v, dist[v]});
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        if (dist[end] == Integer.MAX_VALUE) return path;

        for (int at = end; at != -1; at = prev[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    static String getLocationName(int id) {
        if (id >= 0 && id < idToLocationName.length) return idToLocationName[id];
        return "Unknown";
    }

    public static void main(String[] args) {
        VITRoutePlanner planner = new VITRoutePlanner(50);

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            for (int j = i + 1; j < 50; j++) {
                if (random.nextInt(100) < 10) {
                    planner.addPath(i, j, 100 + random.nextInt(400));
                }
            }
        }

        Scanner scanner = new Scanner(System.in);

        
        for (int i = 0; i < idToLocationName.length; i++) {
            if (idToLocationName[i] != null) {
                System.out.println("- " + idToLocationName[i]);
            }
        }
        System.out.println("Enter start location (Suggestions):");

        String startName = scanner.nextLine().trim().toLowerCase();

        
        for (int i = 0; i < idToLocationName.length; i++) {
            if (idToLocationName[i] != null) {
                System.out.println("- " + idToLocationName[i]);
            }
        }
        System.out.println("Enter end location (Suggestions):");

        String endName = scanner.nextLine().trim().toLowerCase();

        Integer start = locationNameToId.get(startName);
        Integer end = locationNameToId.get(endName);

        if (start == null || end == null) {
            System.out.println("Invalid start or end location.");
            return;
        }

        List<Integer> path = planner.findShortestPath(start, end);
        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.print("Shortest path: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(getLocationName(path.get(i)));
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }
}
