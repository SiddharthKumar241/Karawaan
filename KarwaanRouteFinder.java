import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class KarwaanRouteFinder {
    private static final Map<String, Integer> locationMap = new LinkedHashMap<>();
    private static final String[] locations = {
        "Main Gate", "Academic Block", "Library", "Hostel A",
        "Hostel B", "Cafeteria", "Auditorium", "CV Raman Block"
    };
    private static final int[][] graph = {
        {0, 4, 0, 0, 0, 7, 0, 0},
        {4, 0, 2, 0, 0, 0, 0, 6},
        {0, 2, 0, 3, 0, 0, 0, 0},
        {0, 0, 3, 0, 1, 0, 0, 0},
        {0, 0, 0, 1, 0, 5, 2, 0},
        {7, 0, 0, 0, 5, 0, 3, 0},
        {0, 0, 0, 0, 2, 3, 0, 4},
        {0, 6, 0, 0, 0, 0, 4, 0}
    };

    static {
        for (int i = 0; i < locations.length; i++) {
            locationMap.put(locations[i].toLowerCase(), i);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Karwaan - Route Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        JLabel title = new JLabel("Karwaan  - Find Your Route", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        panel.add(title);

        JTextField startField = new JTextField();
        JTextField endField = new JTextField();
        startField.setToolTipText("Enter Start Location");
        endField.setToolTipText("Enter End Location");

        panel.add(new JLabel("Start Location:"));
        panel.add(startField);
        panel.add(new JLabel("End Location:"));
        panel.add(endField);

        JButton findButton = new JButton("Find Route");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        panel.add(findButton);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        findButton.addActionListener(e -> {
            String start = startField.getText().trim().toLowerCase();
            String end = endField.getText().trim().toLowerCase();

            if (!locationMap.containsKey(start) || !locationMap.containsKey(end)) {
                resultArea.setText(" Invalid input. Available locations:\n" + String.join(", ", locations));
                return;
            }

            int startIndex = locationMap.get(start);
            int endIndex = locationMap.get(end);

            int[] dist = new int[graph.length];
            int[] prev = new int[graph.length];
            boolean[] visited = new boolean[graph.length];
            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(prev, -1);
            dist[startIndex] = 0;

            for (int i = 0; i < graph.length; i++) {
                int u = -1;
                for (int j = 0; j < graph.length; j++) {
                    if (!visited[j] && (u == -1 || dist[j] < dist[u])) u = j;
                }

                if (dist[u] == Integer.MAX_VALUE) break;
                visited[u] = true;

                for (int v = 0; v < graph.length; v++) {
                    if (graph[u][v] > 0 && dist[u] + graph[u][v] < dist[v]) {
                        dist[v] = dist[u] + graph[u][v];
                        prev[v] = u;
                    }
                }
            }

            List<String> path = new ArrayList<>();
            for (int at = endIndex; at != -1; at = prev[at]) {
                path.add(locations[at]);
            }

            Collections.reverse(path);
            resultArea.setText(" Shortest Path from " + locations[startIndex] + " to " + locations[endIndex] + ":\n\n");
            resultArea.append(String.join(" ➡ ", path));
            resultArea.append("\n\n Total Distance: " + dist[endIndex] + " units");
        });

        frame.setVisible(true);
    }
}
