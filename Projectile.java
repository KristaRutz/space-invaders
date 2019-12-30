import java.awt.Color;

import objectdraw.*;

/**
 * An attacking projectile
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class Projectile extends ActiveObject {

	/** constants and instance variables */

	// the dimensions of a bullet
	private static final int WIDTH = 2;
	private static final int HEIGHT = 15;

	// the speed of the shot
	private static final int PAUSE_TIME = 50;

	// the distance for the bullet to move each step
	private static final int MOVE = 5;

	// the projectile
	private FilledRect bullet;

	// the ship that an alien is trying to hit
	private SpaceShip target;

	// the drawing canvas
	private DrawingCanvas canvas;

	/**
	 * The constructor of the projectile which creates a bullet
	 * 
	 * @param x
	 *            x location of the parent alien
	 * @param y
	 *            y location of the parent alien
	 * @param target
	 *            the target ship
	 * @param canvas
	 *            the canvas
	 */
	public Projectile(double x, double y, SpaceShip target, DrawingCanvas canvas) {

		// assign the parameters to instance variables
		this.canvas = canvas;
		this.target = target;

		// create a new line and set to white
		bullet = new FilledRect(x, y, WIDTH, HEIGHT, canvas);
		bullet.setColor(Color.WHITE);

		// begin the animation
		start();
	}

	/**
	 * Shoot the projectile down and see if it hits the ship
	 */
	public void run() {

		// bullet starts off as a live projectile
		boolean live = true;

		// while the bullet is above the bottom of the screen
		while (bullet.getY() < canvas.getHeight()) {

			// move the bullet downwards
			bullet.move(0, MOVE);

			// if the bullet hits the ship
			if (bullet.overlaps(target.getShip()) && live) {

				// deactivate the bullet
				bullet.hide();
				live = false;

				// kill the ship
				target.killShip();
			}

			// pause
			pause(PAUSE_TIME);
		}

		// when it goes past the screen remove it from canvas
		bullet.removeFromCanvas();
	}
}