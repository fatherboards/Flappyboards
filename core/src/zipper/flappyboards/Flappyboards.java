package zipper.flappyboards;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import java.util.Random;


public class Flappyboards extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	Texture bird;
	Texture[] people;
	Texture topPipe;
	Texture bottomPipe;
	Rectangle topPipeRect;
	Rectangle bottomPipeRect;
	Rectangle birdRect;
	Random rand = new Random();
	Sound[] winSound;
	float birdY = 200;
	float pipesX=800;
	int birdDelta = 1;
	boolean playing = true;
	int pipePerson = rand.nextInt(12);
	int score = 0;
	OrthographicCamera camera;
	@Override
	public void create () {
		winSound = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Cena.mp3")), Gdx.audio.newSound(Gdx.files.internal("Gelato.mp3")), Gdx.audio.newSound(Gdx.files.internal("Khaled.mp3")), Gdx.audio.newSound(Gdx.files.internal("Patrick.mp3")), Gdx.audio.newSound(Gdx.files.internal("Robotics.mp3")), Gdx.audio.newSound(Gdx.files.internal("Spanking.mp3")), Gdx.audio.newSound(Gdx.files.internal("Yeah.mp3")), Gdx.audio.newSound(Gdx.files.internal("What.mp3"))};
		people = new Texture[]{new Texture("andre.PNG"), new Texture("berger.PNG"), new Texture("billini.PNG"), new Texture("brett.png"), new Texture("brody.PNG"), new Texture("carsen.PNG"), new Texture("christian.PNG"), new Texture("gabriel.PNG"), new Texture("jackson.PNG"), new Texture("revilla.PNG"), new Texture("vega.PNG"), new Texture("zipper.PNG")};
		batch = new SpriteBatch();
		bird = new Texture("fatherboardslogo.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);


		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 25;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890:";

		font = generator.generateFont(parameter);
		generator.dispose();
		font.setColor(Color.CYAN);
	}

	@Override
	public void render () {
		if(playing) {
			playing = playing();
		}
		else {
			lost();
		}
	}

	private boolean playing() {
		camera.update();
		Gdx.gl.glClearColor(0, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		topPipe = people[pipePerson];
		bottomPipe = people[pipePerson];
		if(Gdx.input.isTouched()) {
			birdDelta = 7;
			if(birdY==24) {
				birdY = 26;
			}
		}
		if(birdY <25) {
			birdDelta=0;
			birdY = 24;
		}
		else if(birdY > 440) {
			birdDelta = 0;
			birdY=439;
		}
		else {
			birdDelta-=1;
		}
		birdY+=birdDelta;
		pipesX-=5;
		if(pipesX<310) pipesX-=8;
		birdRect = new Rectangle(400, birdY, 75, 75);
		topPipeRect = new Rectangle(pipesX,330,50,150);
		bottomPipeRect = new Rectangle(pipesX,0,50,150);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bird, 400, birdY, 75, 75);
		batch.draw(topPipe, pipesX, topPipeRect.y, topPipeRect.width, topPipeRect.height);
		batch.draw(bottomPipe, pipesX, bottomPipeRect.y, bottomPipeRect.width, bottomPipeRect.height);
		if(pipesX < 324 && pipesX > 319) {
			score++;
			winSound[rand.nextInt(8)].play();
		}
		if(pipesX <-50) {
			pipePerson = rand.nextInt(12);
			pipesX = 800;
		}
		font.setColor(Color.RED);
		font.draw(batch, "SCORE: " + score, 75, 470, 0, Align.center, true);
		font.setColor(Color.CYAN);
		batch.end();
		if(birdRect.overlaps(topPipeRect) || birdRect.overlaps(bottomPipeRect)) {
			return false;
		}
		return true;
	}

	private void lost() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bird, 200, 25, 400, 400);
		font.draw(batch, "CLICK THE LOGO TO PLAY AGAIN",410, 440, 0, Align.center, true);
		font.draw(batch, "SCORE: " + score, 125, 75, 0, Align.center, true);
		batch.end();
		if (Gdx.input.isTouched()) {
			if((new Rectangle(500,300,400,400)).overlaps(new Rectangle(Gdx.input.getX()-500,Gdx.input.getY()-150,400,400))) {
				score = 0;
				pipesX = 800;
				playing = playing();
			}
		}
	}

}
