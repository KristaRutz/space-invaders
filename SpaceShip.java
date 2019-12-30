import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;

import javax.swing.JLabel;

/**
 * The user's ship icon that moves and tries to kill invaders, firing bullets
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class SpaceShip extends ActiveObject {

	/** constants and instance variables */

	// the dimensions of the rocketship image
	private static final int WIDTH = 18;

	// Border of where ship can go
	private static final int EDGE = 5;

	// the initial Y location of the spaceship from bottom
	private static final int Y = 400;

	// milliseconds to pause between movements
	private static final int PAUSE_TIME = 25;

	// the distance for the ship to move
	private static final int MOVE = 5;

	// the directions as integer values
	private static final int RIGHT = 6;
	private static final int LEFT = 4;

	// EXTRA CREDIT: the minimum time between shots in milliseconds
	private static final long SHOT_MIN_TIME = 500;

	// the dimensions of the you lose text
	private static final int TEXT_HEIGHT = 48;
	private static final int TEXT_WIDTH = 260;

	// the standard number of lives
	private static final int LIVES = 3;

	// the image of a space ship
	private Image shipImg;

	// EXTRA CREDIT: the sound when a ship loses a life
	private AudioClip sound;

	// the visible image of the rocket ship on the canvas
	private VisibleImage ship;

	// the army of invaders
	private Invaders theArmy;

	// the game that is controlling the ship
	private SpaceInvadersController game;

	// time projectile launches by keeping track of time of last shot
	private long lastShot = 0;

	// EXTRA CREDIT: the number of lives a spaceship has
	private int lives = LIVES;

	// EXTRA CREDIT: the message about lives
	private String livesMessage;

	// EXTRA CREDIT: the display of lives in a GUI
	private JLabel livesDisplay;

	// the text display of the lose message
	private Text youLose;

	// the drawing canvas to create the ship
	private DrawingCanvas canvas;

	/**
	 * The constructor for a space ship
	 * 
	 * @param sound
	 *            the noise the ship makes when shot
	 * @param shipImg
	 *            the image of what a ship looks like
	 * @param game
	 *            the controller of this spaceship
	 * @param canvas
	 *            the canvas
	 */
	public SpaceShip(AudioClip sound, Image shipImg,
			SpaceInvadersController game, DrawingCanvas canvas) {

		// set parameters as instance variables
		this.canvas = canvas;
		this.game = game;
		this.sound = sound;
		this.shipImg = shipImg;

		// create a new VisibleImage of the shipImg at the bottom center
		ship = new VisibleImage(shipImg, canvas.getWidth() / 2, Y, canvas);

		// EXTRA CREDIT: display that three lives remain
		livesMessage = new String("Lives: " + lives);
		livesDisplay = new JLabel(livesMessage);

		// begin the ship's movement
		start();
	}

	/**
	 * The ship moves given that an arrow key is pressed down
	 * 
	 * EXTRA CREDIT: Ship has continuous, smooth movements.
	 */
	public void run() {

		while (true) {

			// if the direction is 4, move left until the edge
			if (game.getDirection() == LEFT && ship.getX() > EDGE) {

				// move left and pause
				ship.move(-5, 0);
				pause(PAUSE_TIME);
			}

			// else if the direction is 6, move right until the edge
			else if (game.getDirection() == RIGHT
					&& ship.getX() + WIDTH < canvas.getWidth() - EDGE) {

				// move right and pause
				ship.move(5, 0);
				pause(PAUSE_TIME);
			}

			// if not moving in either direction, pause the ship
			else {
				pause(PAUSE_TIME);
			}
		}
	}

	/**
	 * Pass in a target for the ship to aim at
	 * 
	 * @param invaders
	 *            the army of Invaders
	 */
	public void setTarget(Invaders invaders) {
		theArmy = invaders;
	}

	/**
	 * The ship launches a projectile when called
	 */
	public void shoot() {

		// EXTRA CREDIT: Only allow a shot if minimum time has elapsed
		if (System.currentTimeMillis() > lastShot + SHOT_MIN_TIME
				&& !theArmy.isGameOver()) {

			// get x location of the spaceship
			int location = (int) (ship.getX() + WIDTH / 2);

			// create a new DefenseProjectile object passing location and
			// invaders as a parameter
			new DefenseProjectile(location, theArmy, canvas);

			// update the time of last shot
			lastShot = System.currentTimeMillis();
		}
	}

	/**
	 * When the ship is killed fully, the game is over.
	 */
	public void killShip() {

		if (!theArmy.isGameOver()) {
			// EXTRA CREDIT: Signal with a noise when the ship is hit
			sound.play();

			// EXTRA CREDIT: Give the ship 2 extra lives
			// reduce the number of lives and update the display
			lives--;
			livesMessage = "Lives: " + lives;
			livesDisplay.setText(livesMessage);

			// If the ship was on it's last life...
			if (lives < 1) {

				// remove the ship image from the canvas
				ship.removeFromCanvas();

				// call the end game in the invader method
				theArmy.endGame(true);

				// create text to say you lose, format text
				youLose = new Text("GAME OVER",
						(canvas.getWidth() - TEXT_WIDTH) / 2,
						canvas.getHeight(), canvas);
				youLose.setColor(Color.ORANGE);
				youLose.setFontSize(TEXT_HEIGHT);
				youLose.setFont("Courier");

				// EXTRA CREDIT: move the text up the screen
				while (youLose.getY() > -TEXT_HEIGHT) {
					youLose.move(0, -MOVE);
					pause(PAUSE_TIME * 2);
				}

				// EXTRA CREDIT: remove the text when it reaches the top
				youLose.removeFromCanvas();

				// EXTRA CREDIT: allow game to restart
				game.allowRestart();

			}
		}
	}

	/**
	 * gets the VisibleImage of the ship
	 * 
	 * @return the ship VisibleImage
	 */
	public VisibleImage getShip() {
		return ship;
	}

	/**
	 * EXTRA CREDIT: The GUI display of the number of lives
	 * 
	 * @return a JLabel showing lives left
	 */
	public JLabel getLives() {
		return livesDisplay;
	}

	/**
	 * EXTRA CREDIT: Allows game to restart if user loses
	 */
	public void resetLives() {

		// reset the lives to the standard and update the display
		lives = LIVES;
		livesMessage = "Lives: " + lives;
		livesDisplay.setText(livesMessage);

		// create a new ship image in the bottom center
		ship = new VisibleImage(shipImg, canvas.getWidth() / 2, Y, canvas);

		// set game over to false so regular play can continue
		theArmy.endGame(false);
	}
}