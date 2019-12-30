import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * A game where an army of aliens invades and a space ship attempts to kill all
 * the invaders
 * 
 * Recommended window: 400 x 500 px
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 * 
 *          ****EXTRA CREDIT****
 * 
 *          Implements continuous, smooth motion for the space ship. <br>
 * 
 *          To make the game more interesting, doesn't let the user fire for a
 *          certain period after the previous shot. <br>
 * 
 *          Each time the aliens changes direction, moves them farther down the
 *          screen. Aliens move all the way to edge when edge columns are gone. <br>
 * 
 *          Modified scoring: bottom row = 10 points, second row = 20 points,
 *          etc. <br>
 * 
 *          Provide more than one life for the ship, but ships automatically die
 *          when they touch an alien. <br>
 * 
 *          Mother ship for aliens. Mother ship is worth 300 points. New class
 *          implementing ActiveObject. <br>
 * 
 *          Allow game restart when the user loses. <br>
 * 
 *          Sound effects when ships or aliens are shot, when game restarts. <br>
 * 
 *          General visual effects: extra images for saucer and background,
 *          moving messages to display win and lose. <br>
 * 
 *          The stats bar and restart button are made with a GUI. <br>
 * 
 */

public class SpaceInvadersController extends WindowController implements
		KeyListener, ActionListener {

	/** constants and instance variables */

	// the directions that the ship can move as integer values
	private static final int RIGHT = 6;
	private static final int LEFT = 4;
	private static final int NOT_MOVING = 0;

	// the direction of the space ship's travel
	private int direction;

	// the elements of the GUI panel - contains a stats bar and restart button
	private JPanel panel;
	private JPanel stats;
	private JButton restart;

	// the images of invader, rocket, and mother ship to pass on
	private Image alien, rocket, mother;

	// the sounds that accompany the game
	private AudioClip shipShot, alienShot, motherShot, opener;

	// the spaceship object
	private SpaceShip ship;

	// the army of invaders
	private Invaders invaders;

	// the score keeper
	private ScoreKeeper score;

	// whether or not a key is being held down
	private boolean keyDown;

	/**
	 * The begin method, runs when the applet is initialized. Constructs the
	 * background, an army of aliens, and a ship, as well as a statistics board.
	 */
	public void begin() {

		// EXTRA CREDIT: load all of the sound files used in the game
		alienShot = getAudio("Pulse-gun-03.wav");
		shipShot = getAudio("Flash-laser-03.wav");
		motherShot = getAudio("Flutter-gun-01.wav");
		opener = getAudio("Space-hole-01.wav");

		// EXTRA CREDIT: create the sky background first, behind everything else
		Image sky = getImage("background.gif");
		new VisibleImage(sky, 0, 0, canvas);

		// create a new score keeper to keep track of the score
		score = new ScoreKeeper(this, canvas);

		// create a new spaceship with the rocket image
		rocket = getImage("rocket.gif");
		ship = new SpaceShip(shipShot, rocket, this, canvas);

		// create a new army of invaders with alien and (EXTRA CREDIT) saucer
		// images
		alien = getImage("invader1.gif");
		mother = getImage("mothership.jpg");
		invaders = new Invaders(score, alien, alienShot, mother, motherShot,
				ship, canvas);

		// call the setTarget method of the ship and pass in the invaders
		ship.setTarget(invaders);

		// EXTRA CREDIT: play the sound to signify a new game starting
		opener.play();

		// EXTRA CREDIT: create a new stats panel with the score and ship's
		// lives
		stats = new JPanel();
		stats.add(score.getScore());
		stats.add(ship.getLives());

		// EXTRA CREDIT: create a restart button
		restart = new JButton("Restart");
		restart.addActionListener(this);

		// EXTRA CREDIT: create a GUI panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		// EXTRA CREDIT: add the button and stats bar to the panel but hide the
		// restart button
		panel.add(stats);
		panel.add(restart);
		restart.hide();

		// EXTRA CREDIT: the GUI
		Container contentPane = getContentPane();
		contentPane.add(panel, BorderLayout.SOUTH);
		contentPane.validate();

		// get ready to listen to user's input on the keyboard
		canvas.addKeyListener(this);
		addKeyListener(this);
		canvas.requestFocus();
	}

	/**
	 * Called when user interacts with the GUI to restart game
	 */
	public void actionPerformed(ActionEvent e) {

		canvas.requestFocus();

		// EXTRA CREDIT: if restart button is clicked..
		if (e.getSource() == restart) {

			// do not allow them to restart again
			restart.hide();

			// clear the invaders from the screen
			invaders.clear();

			// reset the stats bar and the space ship
			score.resetScore();
			ship.resetLives();

			// make a new army of invaders and set them as the target
			invaders = new Invaders(score, alien, alienShot, mother,
					motherShot, ship, canvas);
			ship.setTarget(invaders);

			// play the opener noise
			opener.play();
		}
	}

	/**
	 * (Mandatory) KeyListener event handler for a key having been typed.
	 * 
	 * @param e
	 *            event (key that was typed)
	 */
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * (Mandatory) KeyListener event handler for a key having been released.
	 * Moves the spaceship or fire projectiles if specific keys are pressed.
	 * 
	 * @param e
	 *            event (key that was pressed)
	 */
	public void keyPressed(KeyEvent e) {

		// if the key is not already down
		if (!keyDown) {

			// when the space bar is pressed, execute this code
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {

				// call the shoot method of the spaceship
				ship.shoot();
			}
			// when the left arrow key is pressed, execute this code
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

				// set direction of the ship to LEFT
				direction = LEFT;
			}
			// when the right arrow key is pressed, execute this code
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

				// set direction of the ship to RIGHT
				direction = RIGHT;
			}
		}
		// key is being pressed down
		keyDown = true;

	}

	/**
	 * (Mandatory) KeyListener event handler for a key having been released.
	 * Stops the ship from moving.
	 * 
	 * @param e
	 *            event (key that was released)
	 */
	public void keyReleased(KeyEvent e) {

		// Remember that the key is no longer down.
		keyDown = false;

		// set ship's direction moving to not moving
		direction = NOT_MOVING;
	}

	/**
	 * Get the direction the ship should move
	 * 
	 * @return the direction (left or right) as an integer (4 or 6)
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * EXTRA CREDIT: Called by the ship when the user dies to allow a game
	 * restart
	 */
	public void allowRestart() {

		// show the restart J Button
		restart.show();

	}

	/**
	 * EXTRA CREDIT: Called by the score keeper to check when the high score is
	 * reached
	 * 
	 * @return true if the mother ship has not been shot
	 */
	public boolean motherShipAlive() {
		return invaders.motherShipAlive();
	}

	/**
	 * Called by the score keeper when the game is over
	 */
	public void endGame() {
		invaders.endGame(true);
	}
}
