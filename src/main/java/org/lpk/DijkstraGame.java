package org.lpk;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class DijkstraGame extends Application {
    private ComboBox<String> startCity, endCity;
    private Label resultLabel;

    private final Map<String, Map<String, Integer>> graph = new HashMap<>();

    @Override
    public void start(Stage stage) {
        initializeGraph();
        startCity = new ComboBox<>();
        endCity = new ComboBox<>();
        startCity.getItems().addAll(graph.keySet());
        endCity.getItems().addAll(graph.keySet());
        Button findPathButton = new Button("Find Shortest Path");
        findPathButton.setOnAction(e -> findShortestPath());
        resultLabel = new Label("Select cities and press 'Find Shortest Path'");
        VBox layout = new VBox(15, new Label("Start City:"), startCity, new Label("End City:"), endCity, findPathButton, resultLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 400, 300);
       stage.setTitle("Dijkstra's Shortest Path Game");
        stage.setScene(scene);
        stage.show();
    }
    private void initializeGraph() 
    {
        graph.put("A", Map.of("B", 2, "C", 5));
        graph.put("B", Map.of("A", 2, "D", 4, "E", 7));
        graph.put("C", Map.of("A", 5, "F", 3));
        graph.put("D", Map.of("B", 4, "E", 1));
        graph.put("E", Map.of("B", 7, "D", 1, "F", 6));
        graph.put("F", Map.of("C", 3, "E", 6));
        for (String node : new ArrayList<>(graph.keySet()))
        {
            for (Map.Entry<String, Integer> edge : graph.get(node).entrySet()) 
            {
                graph.computeIfAbsent(edge.getKey(), k -> new HashMap<>()).put(node, edge.getValue());
            }
        }
    }
    private void findShortestPath() {
        String start = startCity.getValue();
        String end = endCity.getValue();
        if (start == null || end == null || start.equals(end)) 
        {
            resultLabel.setText("Select valid start and end cities.");
            return;
        }
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.add(start);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distances.get(current);
            for (Map.Entry<String, Integer> neighbor : graph.get(current).entrySet()) 
            {
                int newDist = currentDistance + neighbor.getValue();
                if (newDist < distances.get(neighbor.getKey())) 
                {
                    distances.put(neighbor.getKey(), newDist);
                    previous.put(neighbor.getKey(), current);
                    queue.add(neighbor.getKey());
                }
            }
        }
        if (distances.get(end) == Integer.MAX_VALUE) {
            resultLabel.setText("No path found.");
            return;
        }
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        resultLabel.setText("Path: " + String.join(" -> ", path) + "\nDistance: " + distances.get(end));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
