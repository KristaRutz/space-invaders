import java.awt.*;

import objectdraw.*;

/**
 * EXTRA CREDIT:
 * 
 * The mother ship is the flying saucer of the alien army. One can win without
 * killing the mother ship, but when the mother ship is killed you will end up
 * with more points (but must still kill all of the aliens).
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class MotherShip extends ActiveObject {

	/** constants and instance variables */

	// the width and height of the saucer
	private static final int WIDTH = 45;
	private static final int HEIGHT = 22;

	// the y of the moving saucer
	private static final int INITIAL_Y = -50;
	private static final int SAUCER_Y = 10;

	// the speed of the saucer
	private static final int MOVE = 2;

	// the time between saucer movements
	private static final int PAUSE_TIME = 60;

	// the image of the mother ship flying saucer
	private VisibleImage saucer;

	// the ship target
	private SpaceShip ship;

	// the canvas
	private DrawingCanvas canvas;

	// the number of the ship
	private int count;

	// whether or not the saucer is alive
	private boolean stillAlive = true;

	/**
	 * Constructs a new Mother Ship active object that looks like a flying
	 * saucer
	 * 
	 * @param count
	 *            the number of ships created
	 * @param motherShip
	 *            the image of the saucer
	 * @param ship
	 *            the space ship target
	 * @param canvas
	 *            the canvas
	 */
	public MotherShip(int count, Image motherShip, SpaceShip ship,
			DrawingCanvas canvas) {

		// assign the parameters as instance variables
		this.ship = ship;
		this.count = count;
		this.canvas = canvas;

		// make the very first saucer invisible on screen and the rest on the
		// screen
		if (count == 0) {
			saucer = new VisibleImage(motherShip, -WIDTH, INITIAL_Y, canvas);
		} else {
			saucer = new VisibleImage(motherShip, -WIDTH, SAUCER_Y, canvas);
		}

		// start the movement of the ship
		start();
	}

	/**
	 * Animates the saucer to move across the top of the screen
	 */
	public void run() {

		// while the saucer hasn't reached the edge or been shot
		while (saucer.getX() < canvas.getWidth() && stillAlive) {

			// move the VisibleImage towards the right
			saucer.move(MOVE, 0);
			pause(PAUSE_TIME);
		}

		// remove it from the canvas if it reaches the end
		if (stillAlive) {
			saucer.removeFromCanvas();
		}
	}

	/**
	 * Called when a bullet hits the mother ship to remove it
	 */
	public void killAlien() {

		// if the ship has not already been killed by another bullet
		if (stillAlive) {

			// remove the image from the canvas
			saucer.removeFromCanvas();

			// set the saucer to not be alive
			stillAlive = false;
		}
	}

	/**
	 * When called, the mother ship shoots a projectile
	 */
	public void shoot() {

		// the saucer can only shoot if it is alive
		if (stillAlive && count != 0) {

			// create a new projectile with ship as a parameter
			new Projectile(saucer.getX() + WIDTH / 2, saucer.getY() + HEIGHT,
					ship, canvas);
		}

	}

	/**
	 * Check if the alien ship overlaps a Drawable Object
	 * 
	 * @param object
	 *            any Drawable Object that may overlap
	 * @return true if the object overlaps the alien saucer
	 */
	public boolean overlaps(Drawable2DInterface object) {
		return saucer.overlaps(object);
	}

	/**
	 * Whether or not the alien ship is alive (on canvas, shooting)
	 * 
	 * @return if the saucer is alive
	 */
	public boolean alive() {
		return stillAlive;
	}

}
