package FrontEnd;

import java.io.File;
import java.time.Duration;

import BackEnd.GameState;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffects {
	MediaPlayer music;
	MediaPlayer thud;
	MediaPlayer move;
	MediaPlayer levelcomplete;
	MediaPlayer mousehover;
	MediaPlayer mouseclicked;
	int movecount = 0;
	public MediaPlayer getThud() {
		return this.thud;
	}
	
	public MediaPlayer getMove() {
		return this.move;
	}
	
	public MediaPlayer getLevelComplete() {
		return this.levelcomplete;
	}
	
	public MediaPlayer getMouseHover() {
		return this.mousehover;
	}
	
	public MediaPlayer getMouseClicked() {
		return this.mouseclicked;
	}
	
	public MediaPlayer getMusic() {
		return this.music;
	}
	
	public void setMoveCount(int newcount) {
		this.movecount = newcount;
	}
	public SoundEffects() {
		String file = "src/Resources/music.mp4";
		Media music = new Media(new File(file).toURI().toString());
		MediaPlayer mediamusic = new MediaPlayer(music);
		this.music = mediamusic;
		mediamusic.setCycleCount(MediaPlayer.INDEFINITE);
		mediamusic.play();
		String file1 = "src/Resources/Move.mp4";
		Media move = new Media(new File(file1).toURI().toString());
		MediaPlayer mediamove = new MediaPlayer(move);
		this.move = mediamove;
		String file2 = "src/Resources/Thump.mp4";
		Media thud = new Media(new File(file2).toURI().toString());
		MediaPlayer mediathud = new MediaPlayer(thud);
		this.thud = mediathud;
		String file3 = "src/Resources/LevelComplete.mp4";
		Media levelcomplete = new Media(new File(file3).toURI().toString());
		MediaPlayer medialevelcomplete = new MediaPlayer(levelcomplete);
		this.levelcomplete = medialevelcomplete;
		String file4 = "src/Resources/mousehover.mp4";
		Media mousehover = new Media(new File(file4).toURI().toString());
		MediaPlayer mediamousehover = new MediaPlayer(mousehover);
		this.mousehover = mediamousehover;
		String file5 = "src/Resources/mouseclicked.mp4";
		Media mouseclicked = new Media(new File(file5).toURI().toString());
		MediaPlayer mediamouseclicked = new MediaPlayer(mouseclicked);
		this.mouseclicked = mediamouseclicked;
	}
	
	public void soundEffects(GameState state, SoundEffects sound) {
    	sound.getThud().stop();
    	sound.getMove().stop();
		if(state.getMoveCount() == movecount) {
			sound.getThud().play();
		} else {
			sound.getMove().play();
			setMoveCount(state.getMoveCount());
		}
	}
	
	public void muteSound(MediaPlayer thud, MediaPlayer move, MediaPlayer levelcomplete, 
			MediaPlayer mousehover, MediaPlayer mouseclicked) {
		thud.setVolume(0);
		move.setVolume(0);
		levelcomplete.setVolume(0);
		mousehover.setVolume(0);
		mouseclicked.setVolume(0);
	}
	
	public void unmute(MediaPlayer thud, MediaPlayer move, MediaPlayer levelcomplete, 
			MediaPlayer mousehover, MediaPlayer mouseclicked) {
		thud.setVolume(100);
		move.setVolume(100);
		levelcomplete.setVolume(100);
		mousehover.setVolume(100);
		mouseclicked.setVolume(100);
	}
	
	public void mutemusic(MediaPlayer music) {
		music.setVolume(0);
	}
	
	public void unmutemusic(MediaPlayer music) {
		music.setVolume(100);
	}
}