import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.event.* ;
import javafx.scene.control.* ;
import javafx.scene.layout.* ;
import javafx.scene.Group ;
import javafx.scene.shape.*;
import javafx.scene.input.* ;
import javafx.scene.image.* ;
import javafx.scene.paint.*;
import javafx.scene.text.* ;
import javafx.animation.* ;
import javafx.util.* ;
import java.util.* ;
import java.io.* ;

public class Game extends Application
{
	private final double rootWidth = 500 ;
	private final double rootHeight = 620 ;
	protected final double mapWidth = 500 ;
	protected final double mapHeight = 600 ;

	protected boolean playerNotMoving ;
	private boolean playerOnPlatform, reel_it ;
	private double player_gravity = 5 ;
	private double playerWidth = 40 ;
	private double playerHeight = 60 ;
	private int counter, score ;
	private double maxHeight ;

	private Stage window ;
	private Scene scene ;
	private Label scoreLabel ;
	private Pane pane ;
	protected KeyFrame keyFrame ;
	protected Timeline timeline ;

	private Player player ;
	private Platform platform_on ;

	private Set<KeyCode> keysPressed = new HashSet<KeyCode>() ;		// a Set is used to store the KeyCodes of the keys pressed
	private List<Platform> platformList = new ArrayList<Platform>() ;

	enum Dir
	{
		LEFT, RIGHT ;
	}
	protected Dir playerFacing ;


	public static void main(String[] args) 
	{
		Application.launch(args) ;
	}

	@Override
	public void start(Stage primaryStage)
	{
		window = primaryStage ;
		window.setTitle("") ;
		window.setResizable(false) ;

		pane = new Pane() ;
		pane.setPrefWidth(mapWidth) ;
		pane.setPrefHeight(mapHeight) ;
		pane.setStyle("-fx-background-color : linear-gradient(indianred, red, crimson)") ;

		initialize() ;

		play() ;

		window.setScene(scene) ;
		window.show() ;
	}


	private void initialize()
	{
		// pass in the Pane, x & y coordinates, width, height and the direction
		player = new Player(this.pane, mapWidth*.25, mapHeight*.5, playerWidth, playerHeight, Dir.RIGHT) ;
		player.setVelocityX(9.0) ;
		player.setVelocityY(5.0) ;

		spawnPlatform(10) ;		

		score = 0 ;

		scoreLabel = new Label("Score : ") ;
		scoreLabel.setPrefWidth(300) ;
		scoreLabel.setPrefHeight(30) ;

		VBox root = new VBox(scoreLabel, pane) ;

		scene = new Scene(root) ;
		scene.setOnKeyPressed(event ->
		{
			if(event.getCode() == KeyCode.LEFT)
			{
				if(player.getBox().getMinX() > 0)
				{
					player.moveLeft() ;
					playerNotMoving = false ;
					playerFacing = Dir.LEFT ;
				}
			}
			else if(event.getCode() == KeyCode.RIGHT)
			{
				if(player.getBox().getMaxX() < mapWidth)
				{
					player.moveRight() ;
					playerNotMoving = false ;
					playerFacing = Dir.RIGHT ;
				}
			}
		}) ;
		scene.setOnKeyReleased(event ->
		{
			if(event.getCode() == KeyCode.LEFT)
				playerNotMoving = true ;
			else if(event.getCode() == KeyCode.RIGHT)
				playerNotMoving = true ;
		}) ;
	}


	private void play()
	{
		player.jump() ;	

		counter = 0 ;
		keyFrame = new KeyFrame(Duration.millis(100), event ->
		{
			if(player.isFalling())
			{
				if(playerCollides())
				{
					playerOnPlatform = true ;
					player.jump() ;
				}
			}

			if(playerOnPlatform)
			{
				if((platform_on.isOn() && platform_on.wasTouched() && player.getY() <= mapHeight*0.5) || player.getY() < player.getHeight())
					reel_it = true ;

				// check if the player moves off the platform
				if(player.getBox().getMaxX() < platform_on.getBox().getMinX() || player.getBox().getMinX() > platform_on.getBox().getMaxX())
				{
					player.falls(true) ;
					playerOnPlatform = false ;
				}
			}

			if(reel_it)
			{
				if(playerNotMoving)
				{
					fall() ;
					spawnPlatform(1) ;
					reel_it = false ;
				}
			}
			
			double num = getRandomNum(15) ;
			
			if(num % 5 == 0)		// then spawn a serrated platform on the screen
			{
				double playerX = player.getX() ;
				double playerY = player.getY() ;

			}
			
			counter++ ;
		}) ;

		timeline = new Timeline(keyFrame) ;
		timeline.setCycleCount(Timeline.INDEFINITE) ;
		timeline.play() ;
	}


	// moves all the platforms and the player down 50px
	private void fall()
	{
		for(int index = 0 ; index < platformList.size() ; index++)
		{
			Platform p = platformList.get(index) ;
			p.setY(p.getY() + 50) ;
		}

		if(player.getBox().getMaxY() < mapHeight)
			player.setY(player.getY() + 50) ;

		for(int index = 0 ; index < platformList.size() ; index++)
		{
			if(platformList.get(index).getY() > mapHeight)
				platformList.remove(platformList.get(index)) ;			
		}
	}


	private void spawnPlatform(int num)
	{
		double width, x, y, r ;
		double prevMinX, prevMaxX ;
		int min_range, max_range ;
		double height = 20 ;

		for(int counter = 0 ; counter < num ; counter++)
		{
			do
			{
				width = getRandomNum(110) ;
			}
			while(width < 60) ;

			if(platformList.isEmpty())
			{
				y = mapHeight - player.getHeight() ;
				x = player.getX() - 5 ;
				width = 100 ;
			}
			else
			{
				y = platformList.get(platformList.size()-1).getY() - 80 ;

				prevMinX = platformList.get(platformList.size()-1).getBox().getMinX() ;
				prevMaxX = platformList.get(platformList.size()-1).getBox().getMaxX() ;
				max_range = (int)(prevMaxX+(prevMaxX-prevMinX)+10) ;
				min_range = (int)(prevMinX-(prevMaxX-prevMinX)-10) ;

				if(max_range > mapWidth) max_range = (int)mapWidth ;

				if(min_range < 0) min_range = 0 ;

				while(1==1)
				{
					x = getRandomNum(min_range, max_range) ;

					if((x > (prevMinX-(prevMaxX-prevMinX)-5) && x+width < (prevMaxX-(.5*(prevMaxX-prevMinX)))))
					{
						break ;
					}
					else if(x < (prevMaxX+(prevMaxX-prevMinX)+5) && x > (prevMinX+(.5*(prevMaxX-prevMinX))))
					{
						if(x+width < mapWidth)	break ;

						x = getRandomNum(min_range, (int)(prevMaxX-(.5*(prevMaxX-prevMinX))));						
						break ;
					}
				}
			}

			Platform p1 = new Platform(pane, x, y, width, height, false) ;
			p1.setFill(Color.BLACK) ;
			platformList.add(p1) ;
		}
	}


	private Boolean playerCollides()
	{
		for(int index = 0 ; index < platformList.size() ; index++)
		{
			Platform platform = platformList.get(index) ;

			if(player.isFalling())
			{
				if(platform.getBounds().intersects(player.getBounds()))
				{

					if((player.getBox().getMinX() >= platform.getBox().getMinX() && player.getBox().getMinX() <= platform.getBox().getMaxX()) || (player.getBox().getMaxX() >= platform.getBox().getMinX() && player.getBox().getMaxX() <= platform.getBox().getMaxX()))
					{
						// check if the player is on top of the platform
						if((player.getBox().getMaxY() >= platform.getBox().getMinY() && player.getBox().getMaxY() <= platform.getBox().getMaxY()) || (player.getBox().getMaxY() > platform.getBox().getMaxY() && player.getBox().getMinY() < platform.getBox().getMinY()))
						{
							player.setY(platform.getBox().getMinY() - player.getHeight()) ;		// put the player on top of the platform

							if((player.getBox().getMaxX() - player.getWidth()/3) < platform.getBox().getMinX())
								player.setX(player.getX()+(player.getWidth()*0.5)) ;
							else if(player.getBox().getMinX()+(player.getWidth()*(1/3)) > platform.getBox().getMaxX())
								player.setX(player.getX()-(player.getWidth()*0.5)) ;

							if(platform.wasTouched() == false)
							{
								score+=10 ;
								scoreLabel.setText("Score : " + Integer.toString(score)) ;
//								System.out.println("Score = " + score) ;
							}

							platform_on = platform ;
							platform.onPlatform(true) ;

							return true ;
						}
						else
						{
							platform.onPlatform(false) ;
						}
					}
				}
			}
		}

		return false ;
	}


	private Double getRandomNum(int range)
	{
		return (double)((int)(Math.random()*range)) ;
	}

	private Double getRandomNum(int min, int max)
	{
		return (double)((int)(Math.random() * (max - min + 1) + min)) ;
	}


	protected void endGame()
	{
		System.out.println("ArrayList size = " + platformList.size()) ;
		sleep(2) ;
		Runtime.getRuntime().exit(0) ;			// terminate the program
	}

	private void sleep(int num)		// puts the System to sleep for num seconds
	{
		try		
		{
			Thread.sleep(1000 * num) ;			// putting the system to sleep for 'num' seconds
		} 
		catch(InterruptedException e)
		{
			e.printStackTrace() ;
		}
	}
}