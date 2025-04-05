package org.lpk;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PomodoroApp extends Application 
{
    private Label timerLabel;
    private Button startButton,stopButton,selectTask,setTimerButton;
    private PomodoroTimer timer;
    private MediaPlayer mediaPlayer;
    private int focusTime=25; //those both are deafault timers
    private int breakTime=5;  
    @Override
    public void start(Stage stage) 
    {
        timerLabel=new Label(focusTime + ":00");
        timerLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
        startButton=createStyledButton("Start");
        stopButton=createStyledButton("Stop");
        selectTask=createStyledButton("Select Task");
        setTimerButton=createStyledButton("Set Focus/Break Time");
        timer=new PomodoroTimer();
        mediaPlayer=new MediaPlayer();
        setTimerButton.setOnAction(event->setTimer());
        startButton.setOnAction(event->selectTaskBeforeStart());
        stopButton.setOnAction(event->{
            timer.stopTimer();
            mediaPlayer.stopMusic();
        });
        selectTask.setOnAction(event->selectTask());
        VBox layout=new VBox(20,timerLabel,setTimerButton,startButton,stopButton,selectTask);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #2c3e50; -fx-border-radius: 10px;");
        Scene scene=new Scene(layout, 400, 400);
        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.show();
    }
    private Object selectTask() 
    {
		// TODO Auto-generated method stub
		return null;
	}
	private Button createStyledButton(String text) 
	{
        Button button=new Button(text);
        button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px;"
        );
        button.setOnMouseEntered(e->button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-background-color: #2980b9; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px;"
        ));
        button.setOnMouseExited(e->button.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px;"
        ));
        return button;
    }

    private void setTimer() 
    {
        TextInputDialog focusInput=new TextInputDialog(String.valueOf(focusTime));
        focusInput.setTitle("Set Timer");
        focusInput.setHeaderText("Enter Focus Time (in minutes):");
        Optional<String> focusResult=focusInput.showAndWait();
        focusResult.ifPresent(value->{
            try 
            {
                focusTime=Integer.parseInt(value);
                timerLabel.setText(focusTime + ":00");
            } 
            catch (NumberFormatException e) 
            {
            	
                showAlert("Invalid Input", "Please enter a valid number.");
            }
        });
        TextInputDialog breakInput=new TextInputDialog(String.valueOf(breakTime));
        breakInput.setTitle("Set Timer");
        breakInput.setHeaderText("Enter Break Time (in minutes):");
        Optional<String> breakResult=breakInput.showAndWait();
        breakResult.ifPresent(value->{
            try 
            {
                breakTime=Integer.parseInt(value);
            } 
            catch (NumberFormatException e) 
            {
                showAlert("Invalid Input","Please enter a valid number.");
            }
        });
    }
    private void selectTaskBeforeStart() 
    {
        List<String> options=Arrays.asList("Select Music", "Select Game", "Set Reminder/Task");
        ChoiceDialog<String> dialog=new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle("Task Selection");
        dialog.setHeaderText("Choose a task before starting:");
        dialog.setContentText("Options:");

        Optional<String> result=dialog.showAndWait();
        result.ifPresent(choice->{
            switch (choice) 
            {
                case "Select Music":
                    openMusicSelection();
                    startTimer();
                    break;
                case "Select Game":
                    openGameSelection();
                    break;
                case "Set Reminder/Task":
                    setReminder();
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        });
    }
    private void openMusicSelection() {
        mediaPlayer.openSpotifyPlaylist(new Stage());
    }

    private void openGameSelection() 
    {
        List<String> games=Arrays.asList("Flappy Bird", "Dijkstra's Shortest Path Game");
        ChoiceDialog<String> gameDialog=new ChoiceDialog<>(games.get(0), games);
        gameDialog.setTitle("Game Selection");
        gameDialog.setHeaderText("Choose a game to play:");
        gameDialog.setContentText("Games:");

        Optional<String> gameResult=gameDialog.showAndWait();
        gameResult.ifPresent(gameChoice->{
            switch (gameChoice) 
            {
                case "Flappy Bird":
                    System.out.println("Starting Flappy Bird...");
                    FlappyBird flappyBird=new FlappyBird();
                    Stage gameStage=new Stage();
                    flappyBird.start(gameStage);
                    break;
                case "Dijkstra's Shortest Path Game":
                    System.out.println("Starting Dijkstra's Algorithm Game...");
                    DijkstraGame dijkstraGame=new DijkstraGame();
                    Stage dijkstraStage=new Stage();
                    dijkstraGame.start(dijkstraStage);
                    break;
                default:
                    System.out.println("Invalid game selection.");
            }
        });
    }
    private void setReminder() 
    {
        TextInputDialog reminderDialog=new TextInputDialog();
        reminderDialog.setTitle("Set Reminder");
        reminderDialog.setHeaderText("Enter your task or reminder:");
        Optional<String> reminderResult=reminderDialog.showAndWait();
        reminderResult.ifPresent(task->{
            showAlert("Reminder Set", "Your task has been recorded: " + task);
            startTimer();
        });
    }
    private void startTimer() 
    {
        timer.startTimer(timerLabel);
    }
    private void showAlert(String title, String content) 
    {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
