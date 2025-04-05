package org.lpk;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBird extends Application {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PIPE_WIDTH = 70;
    private static final int PIPE_GAP = 150;
    private static final int BIRD_SIZE = 30;

    private double birdY = HEIGHT / 2;
    private double birdVelocity = 0;
    private boolean gameRunning = true;
    private int score = 0;

    private List<Pipe> pipes = new ArrayList<>();
    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas));
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE && gameRunning) {
                birdVelocity = -6; // Bird jumps up
            }
        });

        // Game loop
        new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate > 16_000_000) { // ~60 FPS
                    updateGame();
                    drawGame(gc);
                    lastUpdate = now;
                }
            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("Flappy Bird");
        stage.show();
    }

    private void updateGame() {
        if (!gameRunning) return;

        birdY += birdVelocity;
        birdVelocity += 0.5; // Gravity effect

        if (birdY >= HEIGHT || birdY <= 0) {
            gameRunning = false; // Bird hits the ground or top
        }

        // Move pipes & check collision
        for (Pipe pipe : pipes) {
            pipe.x -= 3;
            if (pipe.x + PIPE_WIDTH < 0) {
                pipe.x = WIDTH;
                pipe.height = random.nextInt(200) + 50;
                score++;
            }

            // Collision detection
            if (birdY < pipe.height || birdY > pipe.height + PIPE_GAP) {
                if (pipe.x < 50 + BIRD_SIZE && pipe.x + PIPE_WIDTH > 50) {
                    gameRunning = false;
                }
            }
        }

        // Generate new pipes
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 200) {
            pipes.add(new Pipe(WIDTH, random.nextInt(200) + 50));
        }
    }

    private void drawGame(GraphicsContext gc) {
        // Background
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Bird
        gc.setFill(Color.YELLOW);
        gc.fillOval(50, birdY, BIRD_SIZE, BIRD_SIZE);

        // Pipes
        gc.setFill(Color.GREEN);
        for (Pipe pipe : pipes) {
            gc.fillRect(pipe.x, 0, PIPE_WIDTH, pipe.height);
            gc.fillRect(pipe.x, pipe.height + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipe.height - PIPE_GAP);
        }

        // Score
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(20));
        gc.fillText("Score: " + score, 10, 20);

        // Game Over Message
        if (!gameRunning) {
            gc.setFont(new Font(40));
            gc.fillText("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    static class Pipe {
        double x;
        int height;

        Pipe(double x, int height) {
            this.x = x;
            this.height = height;
        }
    }
}
