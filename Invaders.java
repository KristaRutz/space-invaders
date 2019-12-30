import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;

/**
 * The attacking army of all of the aliens
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class Invaders extends ActiveObject {

	/** constants and instance variables */

	// number of rows and columns of aliens in army
	private static final int NUM_ROWS = 4;
	private static final int NUM_COLS = 7;

	// the vertical and horizontal spacing of aliens
	private static final int HORIZ_SPACE = 48;
	private static final int VERT_SPACE = 32;

	// EXTRA CREDIT: the time between alien mother ship saucers in milliseconds
	private static final long SAUCER_TIME = 30000;

	// EXTRA CREDIT: int used to calculate the points a mother ship is worth
	private static final int SAUCER_POINTS = -26;

	// creates chance of shots being fired from army
	private static final int SHOT = 16;
	private static final int ALIEN_SHOT = 12;
	private static final int SAUCER_SHOT = 2;

	// the speed of the invader class
	private static final int PAUSE_TIME = 30;

	// the distance moved each time
	private static final int MOVE = 2;
	private static final int VERT_MOVE = 7;

	// how far the invaders stay from the edge of canvas
	private static final int EDGE = 10;

	// the borders of the canvas as a function of canvas width
	private int rightBorder;
	private int leftBorder = EDGE;

	// whether or not the army should be moving
	private boolean gameOver = false;

	// a 2d array of aliens that make up the army of invaders
	private Alien[][] theArmy;

	// EXTRA CREDIT: the image of the mother ship
	private Image mother;

	// EXTRA CREDIT: the sounds when alien or mother ship are shot
	private AudioClip alienShot, motherShot;

	// the ship target
	private SpaceShip ship;

	// the score board
	private ScoreKeeper score;

	// EXTRA CREDIT: the mother ship
	private MotherShip saucer;

	// EXTRA CREDIT: whether the mother ship has been killed or not
	private boolean killedSaucer = false;

	// whether the invader army is moving right or left
	private boolean moveRight = true;

	// random int generators to generate random shooting
	private RandomIntGenerator randomRow, randomCol, randomShot;

	// the canvas
	private DrawingCanvas canvas;

	/**
	 * The constructor method of Invaders creates a 2d array of aliens and
	 * begins movement
	 * 
	 * @param score
	 *            the score keeper
	 * @param alien
	 *            the image of an alien in the army
	 * @param alienShot
	 *            the noise when an alien is shot
	 * @param mother
	 *            the army's mother saucer
	 * @param motherShot
	 *            the noise when a mother saucer is shot
	 * @param ship
	 *            the target of the invaders
	 * @param canvas
	 *            the canvas
	 */
	public Invaders(ScoreKeeper score, Image alien, AudioClip alienShot,
			Image mother, AudioClip motherShot, SpaceShip ship,
			DrawingCanvas canvas) {

		// set parameters as instance variables
		this.score = score;
		this.ship = ship;
		this.canvas = canvas;
		this.alienShot = alienShot;
		this.motherShot = motherShot;
		this.mother = mother;

		// make a 2d array of aliens
		theArmy = new Alien[NUM_COLS][NUM_ROWS];

		// use a for loop to put an alien in each cell of the array
		for (int i = 0; i < NUM_COLS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {
				theArmy[i][j] = new Alien(alien, i * HORIZ_SPACE + EDGE, j
						* VERT_SPACE + EDGE, ship, canvas);
			}
		}

		// set the right border as canvas.getWidth - EDGE
		rightBorder = canvas.getWidth() - EDGE;

		// begin the movement
		start();

	}

	/**
	 * The run method that animates the right and left back and forth movement
	 * of the army
	 */
	public void run() {

		// EXTRA CREDIT: number of mother ships sent
		int motherCount = 0;

		// EXTRA CREDIT: the time of the last mother sent
		long lastMother = 0;

		// while the game is still going on
		while (!gameOver) {

			// EXTRA CREDIT: create new mother ships at timed intervals
			if (System.currentTimeMillis() > +lastMother + SAUCER_TIME
					&& !killedSaucer) {

				// create a mother ship saucer
				saucer = new MotherShip(motherCount, mother, ship, canvas);

				// update the time of last shot and number of ships sent
				motherCount++;
				lastMother = System.currentTimeMillis();
			}

			// check if movement goes in right or left direction. if right,
			if (moveRight) {

				// move all of the aliens right
				this.move(MOVE, 0);

				// check if the army of invaders has reached screen right
				if ((rightmost().getX() + 24) >= rightBorder) {

					// if so, change direction
					moveRight = false;

					// EXTRA CREDIT: Move the aliens down screen on direction
					// change
					this.move(0, VERT_MOVE);
				}
			}

			// if not moving in the right direction, move left
			else {

				// move all of the aliens left
				this.move(-MOVE, 0);

				// check if the army of invaders has reached screen right
				if (leftmost().getX() <= leftBorder) {

					// if so, change direction
					moveRight = true;

					// EXTRA CREDIT: Move the aliens down screen on direction
					// change
					this.move(0, VERT_MOVE);
				}
			}

			// once array has moved, pause and then randomly shoot
			pause(PAUSE_TIME);
			this.randomShoot();

			// EXTRA CREDIT: if the aliens are touching the spaceship, kill the
			// ship
			for (int i = 0; i < NUM_COLS; i++) {
				for (int j = 0; j < NUM_ROWS; j++) {
					if (theArmy[i][j].overlaps(ship.getShip())) {
						ship.killShip();
					}
				}
			}
		}
	}

	/**
	 * Move the army by moving each visible alien the same distance
	 * 
	 * @param xoff
	 *            the x distance to be moved
	 * @param yoff
	 *            the y distance to be moved
	 */
	public void move(int xoff, int yoff) {

		// create a nested for loop to go through each index of the array and
		// move the alien the desired amount
		for (int i = 0; i < NUM_COLS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {
				theArmy[i][j].move(xoff, yoff);
			}
		}
	}

	/**
	 * EXTRA CREDIT: Determine the right most remaining alien
	 * 
	 * @return the rightmost Alien
	 */
	public Alien rightmost() {

		// assume the alien on the left is the right most
		Alien rightmost = theArmy[0][0];

		// the x location of rightmost alien
		int rightmostX = 0;

		// check each alien's x location
		for (int i = 0; i < NUM_COLS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {

				// if an alien is further right than the previous rightmost
				if (theArmy[i][j].alive() && theArmy[i][j].getX() > rightmostX) {

					// save it as the rightmost and update location
					rightmost = theArmy[i][j];
					rightmostX = (int) theArmy[i][j].getX();
				}
			}
		}

		// return the right most alien
		return rightmost;
	}

	/**
	 * EXTRA CREDIT: Determine the left most remaining alien
	 * 
	 * @return the lefttmost Alien
	 */
	public Alien leftmost() {

		// assume the alien on the right is the left most
		Alien leftmost = theArmy[NUM_COLS - 1][NUM_ROWS - 1];

		// the x location of leftmost alien
		int leftmostX = canvas.getWidth();

		// check each alien's x location
		for (int i = NUM_COLS - 1; i >= 0; i--) {
			for (int j = NUM_ROWS - 1; j >= 0; j--) {

				// if an alien is further left than the previous leftmost
				if (theArmy[i][j].alive() && theArmy[i][j].getX() < leftmostX) {

					// save it as the leftmost and update location
					leftmost = theArmy[i][j];
					leftmostX = (int) theArmy[i][j].getX();
				}
			}
		}
		// return the left most alien
		return leftmost;
	}

	/**
	 * When called, a random alien or mother ship will shoot a bullet
	 */
	public void randomShoot() {

		// construct three random generators, for row, column, and chance of
		// shooting
		randomRow = new RandomIntGenerator(0, NUM_ROWS - 1);
		randomCol = new RandomIntGenerator(0, NUM_COLS - 1);
		randomShot = new RandomIntGenerator(1, SHOT);

		// call the shoot method on an alien: 1/4 chance of shot
		if (randomShot.nextValue() > ALIEN_SHOT) {
			// shoot from a randomized alien
			theArmy[randomCol.nextValue()][randomRow.nextValue()].shoot();
		}

		// EXTRA CREDIT: call the shoot method on the saucer: 1/16 chance of
		// shot
		else if (randomShot.nextValue() < SAUCER_SHOT) {
			// shoot from the saucer
			saucer.shoot();
		}
		// else there is a 11/16 chance that no shots will be fired
	}

	/**
	 * Check if an alien or saucer was hit by a bullet and kill it
	 * 
	 * @param bullet
	 *            the FilledRect that makes up the actual projectile
	 * @param d
	 *            the DefenseProjectile Object that created the bullet
	 */
	public void checkHit(FilledRect bullet, DefenseProjectile d) {

		// go through each alien
		for (int i = 0; i < NUM_COLS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {

				// check if the given bullet touches any alien
				if (theArmy[i][j].overlaps(bullet) && !gameOver) {

					// if so, kill that alien and pass in the noise of the
					// alienShot
					theArmy[i][j].killAlien(alienShot);

					// discontinue the DefenseProjectile that met its target
					d.discontinue();

					// update the score board
					score.updateScore(j);
				}
			}
		}

		// EXTRA CREDIT: also check if a bullet has hit the flying saucer
		if (saucer.overlaps(bullet) && !gameOver) {

			// EXTRA CREDIT: kill the saucer
			saucer.killAlien();

			// EXTRA CREDIT: play the audio of a saucer being shot
			motherShot.play();

			// EXTRA CREDIT: saucer has been killed so will not be made again
			killedSaucer = true;

			// EXTRA CREDIT: discontinue the DefenseProjectile that met its
			// target
			d.discontinue();

			// EXTRA CREDIT: update the score board
			score.updateScore(SAUCER_POINTS);
		}
	}

	/**
	 * Can set game to over or not
	 * 
	 * @param b
	 *            whether the method is calling game over or not
	 */
	public void endGame(boolean b) {

		// set the gameOver boolean to b
		gameOver = b;
	}

	/**
	 * Check to see if game is over
	 * 
	 * @return if gameOver is true or false
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * EXTRA CREDIT: Clears all of the aliens off of the screen in order to
	 * restart
	 */
	public void clear() {
		for (int i = 0; i < NUM_COLS; i++) {
			for (int j = 0; j < NUM_ROWS; j++) {
				theArmy[i][j].killAlien(alienShot);
			}
		}
	}

	/**
	 * EXTRA CREDIT: Checks if the mother ship is alive for scoring purposes
	 * 
	 * @return whether or not the saucer has been killed
	 */
	public boolean motherShipAlive() {
		return saucer.alive();
	}
}