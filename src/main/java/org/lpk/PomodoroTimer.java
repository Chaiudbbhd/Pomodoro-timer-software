package org.lpk;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PomodoroTimer {
    private int minutes=25;
    private int seconds=0;
    private Timeline timeline;
    public void startTimer(javafx.scene.control.Label timerLabel) 
    {
        timeline=new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            if(seconds==0) 
            {
                if(minutes==0) 
                {
                    timeline.stop();
                } 
                else 
                {
                    minutes--;
                    seconds=59;
                }
            } 
            else 
            {
                seconds--;
            }
            timerLabel.setText(String.format("%02d:%02d",minutes,seconds));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopTimer() 
    {
        if(timeline!=null) 
        {
            timeline.stop();  
        }
        minutes=25; 
        seconds=0;  
    }
}
