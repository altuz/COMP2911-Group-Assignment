package GameLogic;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffects {
	MediaPlayer thud;
	MediaPlayer move;
	MediaPlayer levelcomplete;
	MediaPlayer mousehover;
	MediaPlayer mouseclicked;
	MediaPlayer endpoint;
	int movecount = 0;
	int endpoints;
	int prev;
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
	
	public MediaPlayer getEndPoint() {
		return this.endpoint;
	}
	
	public int getEndPoints() {
		return this.endpoints;
	}
	
	public int getPrev() {
		return this.prev;
	}
	
	public void setEndPoints(int value) {
		this.endpoints = value;
	}
	
	public void setPrev(int value) {
		this.prev = value;
	}
	public SoundEffects() {
		String file1 = "Move.mp4";
		Media move = new Media(new File(file1).toURI().toString());
		MediaPlayer mediamove = new MediaPlayer(move);
		this.move = mediamove;
		String file2 = "Thump.mp4";
		Media thud = new Media(new File(file2).toURI().toString());
		MediaPlayer mediathud = new MediaPlayer(thud);
		this.thud = mediathud;
		String file3 = "LevelComplete.mp4";
		Media levelcomplete = new Media(new File(file3).toURI().toString());
		MediaPlayer medialevelcomplete = new MediaPlayer(levelcomplete);
		this.levelcomplete = medialevelcomplete;
		String file4 = "mousehover.mp4";
		Media mousehover = new Media(new File(file4).toURI().toString());
		MediaPlayer mediamousehover = new MediaPlayer(mousehover);
		this.mousehover = mediamousehover;
		String file5 = "mouseclicked.mp4";
		Media mouseclicked = new Media(new File(file5).toURI().toString());
		MediaPlayer mediamouseclicked = new MediaPlayer(mouseclicked);
		this.mouseclicked = mediamouseclicked;
		String file6 = "endpoint.mp4";
		Media endpoint = new Media(new File(file6).toURI().toString());
		MediaPlayer mediaendpoint = new MediaPlayer(endpoint);
		this.endpoint = mediaendpoint;
	}
	
	public void soundEffects(GameState state, SoundEffects sound, int prev, int endpoint) {
    	sound.getThud().stop();
    	sound.getMove().stop();
    	sound.getEndPoint().stop();
		if(state.getMoveCount() == movecount) {
			sound.getThud().play();
		} else {
			if(endpoint > prev) {
				sound.getEndPoint().play();
				setPrev(endpoint);
			} else {
				sound.getMove().play();
				movecount = state.getMoveCount();
				System.out.println("prev: " + prev + " endpoint: " + endpoint);
			}
		}
	}
	
	public int countEndPoints(int [][] map) {
		int rows = map.length;
		int cols = map[0].length;
		int point = 0;
		for(int y = 0; y < rows; y++){
			for(int x = 0; x < cols; x++){
				if(map[x][y] == 5) {
					point++;
				}
			}
		}
		return point;
	}
}
