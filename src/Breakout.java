package breakout;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Breakout extends Game {
	
	//initializing variables
	int brickWidth = 80;
	int brickHeight = 40;
	int level;
	int lives;
	boolean wonLevel = false;
	int score = 0;
	//vectors to indicate ball direction
	int ballYVec = 3;
	int ballXVec = 1;
	//2D arrays to represent bricks
	Rectangle bricks[][] = new Rectangle[7][5];
	int[][] hitArray = new int[bricks.length][bricks[0].length];
	int bricksRemain = bricks.length * bricks[0].length;

	
	boolean intro = true;
	boolean introScreen = false;
	boolean isPlaying = false;
	boolean gameOver = false;
	
	int brickCount = bricks.length * bricks[0].length;
	
    // rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};

    //rectangle to represent player
    private Rectangle player = new Rectangle(640/2-45/2, 460, 45, 10) {{
    	setFill(Color.WHITE);
    }};
    
    //rectangle for one brick
    private Rectangle makeBricks(int x, int y) {
    	Rectangle rectangle = new Rectangle();
    	rectangle.setWidth(brickWidth);
    	rectangle.setHeight(brickHeight);
    	rectangle.setX(x*brickWidth + 40);
    	rectangle.setY(y*brickHeight + 20);
    	rectangle.setFill(Color.GREEN);
    	rectangle.setStroke(Color.BLACK);
    	rectangle.setStrokeType(StrokeType.INSIDE);
    	rectangle.setStrokeWidth(2);
    	
    	hitArray[x][y] = 0;
    	
    	return rectangle;
    }
    
    /**
     * Uses nested-for loop to iterate through
     * 2D bricks array to make bricks
     */
    private void initializeBricks() {
    	for(int i = 0; i < bricks.length; i++) {
    		for(int j = 0; j < bricks[i].length; j++) {
    			bricks[i][j] = makeBricks(i, j);
    		}
    	}
    }
    
    //text-field for scoreboard
    private Text scoreBoard = new Text() {{
    	setX(10);
    	setY(15);
    	setFill(Color.WHITE);
    }};
    
    //text-field to count lives
    private Text livesCounter = new Text() {{
    	setX(575);
    	setY(15);
    	setFill(Color.WHITE);
    }};
    
    //text displayed before start of game
    private Text introTxt = new Text() {{
    	setX(bg.getWidth()/2);
    	setY(bg.getHeight()/2);
    	setText("Press P to Play!");
    	setFill(Color.WHITE);
    }};
    
    //text displayed when user runs out of lives
    private Text gameOverTxt = new Text() {{
    	setX(bg.getWidth()/2);
    	setY(bg.getHeight()/2);
    	setText("Game Over! Press P to try again!");
    	setFill(Color.WHITE);
    }};

    //ball
    private Circle ball = new Circle(640/2-5/2, 250, 5) {{
    	setFill(Color.WHITE);
    }};
    
    /**
     * Collision detection method iterates through 2D array of bricks, and if the hit count for
     * the brick is less than 3, it will register the collision and change the direction of the ball.
     * It will then call the updateBrick() method to change the color of the brick
     */
    public void checkCollision() {
    	for(int i = 0; i < bricks.length; i++) {
    		for(int j = 0; j < bricks[i].length; j++) {
    			if(hitArray[i][j] < 3) {
	    			if(ball.getCenterX() + ball.getRadius() > bricks[i][j].getX() && ball.getCenterX() - ball.getRadius() <  bricks[i][j].getX() + bricks[i][j].getWidth()) {
	    				if(ball.getCenterY() + ball.getRadius() > bricks[i][j].getY() && ball.getCenterY()-ball.getRadius() < bricks[i][j].getY() + bricks[i][j].getHeight()) {
	    					if(ball.getCenterX() - ball.getRadius() < bricks[i][j].getX() || ball.getCenterX() + ball.getRadius() > bricks[i][j].getX() + bricks[i][j].getWidth()) {  
	    						ballXVec *= -1;
	    					}
	    					if(ball.getCenterY() - ball.getRadius() < bricks[i][j].getY() || ball.getCenterY() + ball.getRadius() > bricks[i][j].getY() + bricks[i][j].getHeight()) {
	    						ballYVec *= -1;
	    					}
	    					updateBrick(i, j);	
	    				}
	    			}
    			}
    		}
    	}
    }
     
    /**
     * increments the hit counter by one and
     * changes the color of the brick depending on the hit count
     * @param x coordinate of brick in array
     * @param y coordinate of brick in array
     */
    public void updateBrick(int x, int y) {
    	hitArray[x][y]++;
    	if(hitArray[x][y] == 1) {
    		bricks[x][y].setFill(Color.YELLOW);
    	}
    	else if(hitArray[x][y] == 2) {
    		bricks[x][y].setFill(Color.RED);
    	}
    	else if(hitArray[x][y] == 3) {
    		bricks[x][y].setFill(Color.BLACK);
    		score++;
    		bricksRemain--;
    	}
    }
    
    /**
     * Constructs a new test game.
     * @param stage the primary stage
     */
    public Breakout(Stage stage) {
        super(stage, "Breakout!", 60, 640, 480);
        // initializes Bricks, lives, level, and score at the beginning of the game
        initializeBricks();
        lives = 3;
        level = 1;
        score = 0;
        getSceneNodes().getChildren().add(bg);
        //adds each brick onto the screen
        for(int i = 0; i < bricks.length; i++) {
        	for(int j = 0; j < bricks[i].length; j++) {
        		getSceneNodes().getChildren().add(bricks[i][j]);
        	}
        }
        //adds rest of elements onto screen
        getSceneNodes().getChildren().addAll(player, ball, scoreBoard, livesCounter, gameOverTxt, introTxt);
    } // TestGame

    @Override
    public void update(Game game, GameTime gameTime) {
    	
    	//sets player, ball and bricks visible
    	player.setVisible(isPlaying);
		ball.setVisible(isPlaying);
		for(int i = 0; i < bricks.length ; i++) {
			for(int j = 0; j < bricks[i].length; j++) {
				bricks[i][j].setVisible(isPlaying);
			}
		}
    	
		//intro screen
    	if(intro) {
    		if(game.getKeyManager().isKeyPressed(KeyCode.P)) {
    			intro = false;
    			isPlaying = true;
    		}
    	}
    	//game screen
    	if(isPlaying) {
    		
    		introTxt.setVisible(intro);
    		gameOverTxt.setVisible(gameOver);
    		
	    	scoreBoard.setText("Score: " + Integer.toString(score));
	    	livesCounter.setText("Lives: " + Integer.toString(lives));
	    	
	    	//moves ball around
	    	ball.setCenterY(ball.getCenterY()+level*ballYVec);
	    	ball.setCenterX(ball.getCenterX()-level*ballXVec);
	    	
	    	checkCollision();
	    	
	    	//ball-border collision
	    	if(((ball.getCenterX()) - ball.getRadius() > (bg.getWidth())) || ((ball.getCenterX()) + ball.getRadius() < 5)) {
	    		ballXVec *= -1;
	    	}
	    	if((ball.getCenterY() < 5)) {
	    		ballYVec *= -1;
	    	}
	    	
	    	//paddle-ball collision
	    	if(((ball.getCenterX()) > (player.getX()-4)) && ((ball.getCenterX()) < (player.getX() + player.getWidth()))) {
	    		if(ball.getCenterY() > player.getY() - 5)
	    			ballYVec *= -1;
	    	}
	    	
	    	//player misses ball case
	    	if(ball.getCenterY() > (player.getY() + 10)) {
	    		lives--;
	    		ball.setCenterX(640/2-5/2);
	    		ball.setCenterY(250);
	    	}

	    	// player movement
	        if(player.getX() > 10)
	        	if (game.getKeyManager().isKeyPressed(KeyCode.LEFT)) player.setX(player.getX() - 4);
	        if(player.getX() < 630 - 45 )
	        	if (game.getKeyManager().isKeyPressed(KeyCode.RIGHT)) player.setX(player.getX() + 4);

	        //next-level transition
	        if(bricksRemain == 0) {
	        	wonLevel = true;
	        	isPlaying = false;
	        }
	        
	        // game-over transition
	        if(lives == 0) {
	        	isPlaying = false;
	        	gameOver = true;
	        }
    	}
    	
    	// if isPlaying variable changes to false, then each element will be invisible
		player.setVisible(isPlaying);
		ball.setVisible(isPlaying);
		for(int i = 0; i < bricks.length ; i++) {
			for(int j = 0; j < bricks[i].length; j++) {
				bricks[i][j].setVisible(isPlaying);
			}
		}
		
		// places gameOverTxt in the middle of the screen
		// if player elects to play again, lives, level counters are reset
		// and isPlaying is set to true – setting the elements visible again
		// resets bricks
		if(gameOver) {
			gameOverTxt.setText("GAME OVER! PRESS P TO TRY AGAIN\nScore: " + score);
			if(game.getKeyManager().isKeyPressed(KeyCode.P)) {
				gameOver = false;
				isPlaying = true;
				lives = 3;
				level = 1;
			}
			player.setVisible(isPlaying);
			ball.setVisible(isPlaying);
			for(int i = 0; i < bricks.length ; i++) {
				for(int j = 0; j < bricks[i].length; j++) {
					bricks[i][j].setFill(Color.GREEN);
					hitArray[i][j] = 0;
				}
			}
		}
		
		// if there are no more bricks, scene is reset, lives and level increments by 1
		// resets bricks
        if(wonLevel == true) {
        	gameOverTxt.setText("Congratulations! Press P to play the next level");
        	if(game.getKeyManager().isKeyPressed(KeyCode.P)) {
        		wonLevel = false;
        		isPlaying = true;
            	lives++;
            	level++;
        	}
        	for(int i = 0; i < bricks.length ; i++) {
				for(int j = 0; j < bricks[i].length; j++) {
					bricks[i][j].setFill(Color.GREEN);
					hitArray[i][j] = 0;
				}
			}
        }
		
    	
    } // update

} // TestGame