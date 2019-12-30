import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;

/**
 * An individual alien in the army that can move, shoot, and die.
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class Alien {

	/** constants and instance variables */

	// the dimensions of the alien image
	private static final int WIDTH = 24;
	private static final int HEIGHT = 16;

	// the visible construction of the image
	private VisibleImage alien;

	// the ship target
	private SpaceShip ship;

	// the canvas
	private DrawingCanvas canvas;

	// whether or not the alien is alive
	private boolean stillAlive = true;

	/**
	 * The constructor of an alien which makes it appear on the screen
	 * 
	 * @param alienImg
	 *            the image file the depicts an alien
	 * @param x
	 *            the x location of the alien
	 * @param y
	 *            the y location of the alien
	 * @param ship
	 *            the target SpaceShip the alien must kill
	 * @param canvas
	 *            the canvas
	 */
	public Alien(Image alienImg, int x, int y, SpaceShip ship,
			DrawingCanvas canvas) {

		// set parameters as instance variables
		this.canvas = canvas;
		this.ship = ship;

		// make a new visible alien at x, y
		alien = new VisibleImage(alienImg, x, y, canvas);
	}

	/**
	 * move the alien by a specific offset
	 * 
	 * @param xoff
	 *            the x distance to move
	 * @param yoff
	 *            the y distance to move
	 */
	public void move(double xoff, double yoff) {

		// move the visible image by x offset and y offset
		alien.move(xoff, yoff);
	}

	/**
	 * When called, the alien will die.
	 * 
	 * @param alienShot
	 *            the sound when an alien is shot
	 */
	public void killAlien(AudioClip alienShot) {

		// if the alien is still on the canvas
		if (stillAlive) {

			// remove the alien from the canvas
			alien.removeFromCanvas();

			// EXTRA CREDIT: signal death with a sound
			alienShot.play();

			// no longer alive so can't be killed again
			stillAlive = false;
		}
	}

	/**
	 * The alien will launch a projectile when this method is called.
	 */
	public void shoot() {

		// the alien can only shoot if it is still on the canvas
		if (stillAlive) {

			// create a new projectile with ship as a parameter
			new Projectile(alien.getX() + WIDTH / 2, alien.getY() + HEIGHT,
					ship, canvas);
		}
	}

	/**
	 * Check if the alien overlaps a Drawable Object
	 * 
	 * @param object
	 *            any Drawable Object that may overlap
	 * @return true if the object overlaps the alien
	 */
	public boolean overlaps(Drawable2DInterface object) {

		// return whether or not the alien image overlaps object
		return alien.overlaps(object);
	}

	/**
	 * Whether or not the alien is alive (on canvas, shooting)
	 * 
	 * @return if the alien is alive
	 */
	public boolean alive() {
		return stillAlive;
	}

	/**
	 * @return x location of alien
	 */
	public double getX() {

		// return the x location of the alien image
		return alien.getX();
	}

	/**
	 * @return y location of the alien
	 */
	public double getY() {

		// return the y location of the alien image
		return alien.getY();
	}

}