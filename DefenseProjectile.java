import objectdraw.*;

import java.awt.*;

/**
 * The projectile that a ship uses to defend itself
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class DefenseProjectile extends ActiveObject {

	/** constants and instance variables */

	// the dimensions of a bullet
	private static final int WIDTH = 2;
	private static final int HEIGHT = 15;

	// pause time between movements
	private static final int PAUSE_TIME = 40;

	// the set y location of the projectile
	private static final int YLOC = 390;

	// the distance for the bullet to move each step
	private static final int MOVE = 5;

	// the projectile
	private FilledRect bullet;

	// whether the projectile is actively able to kill
	private boolean live = true;

	// the invaders that our ship is trying to hit
	private Invaders theArmy;

	/**
	 * The constructor for a new projectile
	 * 
	 * @param x
	 *            the location from where a bullet will be launched
	 * @param army
	 *            the invaders that are the target of a projectile
	 * @param canvas
	 *            the canvas
	 */
	public DefenseProjectile(int x, Invaders army, DrawingCanvas canvas) {

		// make our target the invading army
		theArmy = army;

		// create a new bullet and set to red
		bullet = new FilledRect(x, YLOC, WIDTH, HEIGHT, canvas);
		bullet.setColor(Color.RED);

		// begin animation
		start();
	}

	/**
	 * Animated so a bullet will go up from where it is shot and check to see if
	 * it has hit any invaders. If so, it will kill them and update the score.
	 */
	public void run() {

		// while the bullet is still in the playing area
		while (bullet.getY() >= -HEIGHT) {

			// move the bullet upwards
			bullet.move(0, -MOVE);

			// if the bullet is live, check if it hits a target alien or saucer
			if (live) {

				// check if the bullet hits anything
				theArmy.checkHit(bullet, this);
			}

			// pause between movements
			pause(PAUSE_TIME);
		}

		// remove bullet when it goes off screen
		bullet.removeFromCanvas();
	}

	/**
	 * Called if a bullet successfully hits an alien, discontinuing this bullet
	 */
	public void discontinue() {

		// do not let it kill any more targets
		live = false;

		// hide the bullet
		bullet.hide();
	}
}