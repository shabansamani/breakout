package breakout;

import javafx.stage.Stage;
import javafx.application.*;
import com.michaelcotterell.game.*;

public class Driver extends Application {
	 @Override
	    public void start(Stage primaryStage) throws Exception { 
	        Game game = new Breakout(primaryStage);
	        primaryStage.setTitle(game.getTitle());
	        primaryStage.setScene(game.getScene());
	        primaryStage.show();
	        game.run();
	    } // start
	    
	    public static void main(String[] args) {
	        launch(args);
	    } // main
}
