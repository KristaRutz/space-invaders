import java.awt.Color;
import javax.swing.JLabel;
import objectdraw.*;

/**
 * Keeps track of the score of the game and whether the user has won
 * 
 * @author Krista Rutz
 * @section CS051 Lab 4
 */

public class ScoreKeeper extends ActiveObject {

	/** constants and instance variables */

	// maximum score of the game (all alien point values summed)
	private static final int MAX = 700;

	// EXTRA CREDIT: Different possible scores for different things
	private static final int ALIEN_POINTS = 10;
	private static final int SAUCER_POINTS = 300;
	private static final int ALIEN_LEVELS = 4;

	// the dimensions of the you lose text
	private static final int TEXT_HEIGHT = 48;
	private static final int TEXT_WIDTH = 200;

	// EXTRA CREDIT: the speed of the text
	private static final int MOVE = 5;

	// EXTRA CREDIT: the time paused between step
	private static final int PAUSE_TIME = 50;

	// the text display of the score and win message
	private Text youWin;

	// the string displayed on the scoreboard
	private String scoreMessage;

	// the variable to keep track of game score
	private int score = 0;

	// the canvas
	private DrawingCanvas canvas;

	// EXTRA CREDIT: the display of the score in a GUI
	private JLabel scoreBoard;

	// the Controller that contains the game
	private SpaceInvadersController game;

	/**
	 * Creates a new score board display when called.
	 * 
	 * @param game
	 *            the controller that builds the score keeper and displays it
	 * @param canvas
	 *            the canvas
	 */
	public ScoreKeeper(SpaceInvadersController game, DrawingCanvas canvas) {

		// assign the parameters to instance variables
		this.canvas = canvas;
		this.game = game;

		// creates the score board and (EXTRA CREDIT) places in a GUI
		scoreMessage = "Score: " + score;
		scoreBoard = new JLabel(scoreMessage);

		// begin animation
		start();
	}

	/**
	 * (Mandatory) Doesn't move anything when first created
	 */
	public void run() {

	}

	/**
	 * Updates the score when called, determines if user has won.
	 * 
	 * @param row
	 *            the row of the alien to determine its point value
	 */
	public void updateScore(int row) {

		// increase the user's score
		// EXTRA CREDIT: different aliens/ships worth different points
		score = score + ((ALIEN_LEVELS - row) * ALIEN_POINTS);

		// set the text to display the new score
		scoreMessage = "Score: " + score;

		// EXTRA CREDIT: place the updated score in the GUI
		scoreBoard.setText(scoreMessage);

		// check if the score means user has won (by killing all aliens)
		if ((score >= MAX && game.motherShipAlive())
				|| (!game.motherShipAlive() && score >= MAX + SAUCER_POINTS)) {

			// end the game so more mother ships can not be created
			game.endGame();

			// if so, create the win message and format
			youWin = new Text("YOU WIN", (canvas.getWidth() - TEXT_WIDTH) / 2,
					500, canvas);
			youWin.setColor(Color.ORANGE);
			youWin.setFontSize(TEXT_HEIGHT);
			youWin.setFont("Courier");

			// EXTRA CREDIT: move the text up the screen
			while (youWin.getY() > -TEXT_HEIGHT) {
				youWin.move(0, -MOVE);
				pause(PAUSE_TIME);
			}

			// when it reaches the end of the screen, remove from canvas
			youWin.removeFromCanvas();
		}
	}

	/**
	 * EXTRA CREDIT: Create a GUI to display scoring
	 * 
	 * @return the JLabel with the score
	 */
	public JLabel getScore() {
		return scoreBoard;
	}

	/**
	 * EXTRA CREDIT: Reset the score if the user loses and wants to restart
	 */
	public void resetScore() {

		// set the score to 0 and update the display
		score = 0;
		scoreMessage = "Score: " + score;
		scoreBoard.setText(scoreMessage);
	}
}