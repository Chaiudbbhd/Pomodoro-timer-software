package org.lpk;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MediaPlayer 
{
    private Player player;
    public void playMusic(String filePath) 
    {
        try 
        {
            FileInputStream fileInputStream=new FileInputStream(filePath);
            player=new Player(fileInputStream);
            new Thread(()->{
                try 
                {
                    player.play();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }).start();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    public void stopMusic() 
    {
        if (player!=null) 
        {
            player.close();
        }
    }
    public void openSpotifyPlaylist(Stage stage) 
    {
        Platform.runLater(()->{
            WebView webView=new WebView();
            String spotifyUrl="https://open.spotify.com/embed/playlist/33uWGOoPXMXNjxYoCNkd29?utm_source=generator&theme=0";
            webView.getEngine().load(spotifyUrl);
            Scene scene=new Scene(webView, 400, 500);
            Stage spotifyStage=new Stage();
            spotifyStage.setTitle("Spotify Playlist");
            spotifyStage.setScene(scene);
            spotifyStage.show();
        });
    }
}
