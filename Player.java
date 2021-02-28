import java.util.* ;
import javafx.event.* ;
import javafx.scene.control.* ;
import javafx.scene.Group ;
import javafx.scene.layout.* ;
import javafx.animation.* ;
import javafx.scene.input.* ;
import javafx.util.* ;
import javafx.geometry.* ;
import javafx.scene.image.* ;
import javafx.scene.paint.*;
import javafx.scene.text.* ;

public class Player extends Game
{
	private final double width ;
	private final double height ;
	private final double gravity = 5 ;
	private final double terminalVelocity = 100 ;

	private double x, y, velocityX, velocityY, vertical_speed ;
	private boolean falling, jumping, endTheGame ;
	private int counter ;

	private Timeline timeline ;
	private KeyFrame keyFrame ;
	private Pane root ;

	private Sprite currentSprite ;	

	private ArrayList<Sprite> rightList = new ArrayList<Sprite>() ;
	private ArrayList<Sprite> leftList = new ArrayList<Sprite>() ;


	public Player(Pane p, double x, double y, double w, double h, Dir d)
	{
		this.root = p ;
		this.x = x ;
		this.y = y ;
		this.width = w ;
		this.height = h ;
		this.falling = false ;

		initializeSprites(x, y) ;

		if(d == Dir.LEFT)
			animation(leftList, 100) ;
		else if(d == Dir.RIGHT)
			animation(rightList, 100) ;

		playerNotMoving = true ;
		playerFacing = d ;

		endTheGame = false ;
	}


	public void moveLeft()
	{
		this.x-=this.velocityX ;

		if(playerFacing == Dir.RIGHT)
		{
			timeline.stop() ;
			root.getChildren().remove(currentSprite.getImage()) ;
			animation(leftList, 100) ;
			playerFacing = Dir.LEFT ;
		}
	}


	public void moveRight()
	{
		this.x+=this.velocityX ;

		if(playerFacing == Dir.LEFT)
		{
			timeline.stop() ;
			root.getChildren().remove(currentSprite.getImage()) ;
			animation(rightList, 100) ;
			playerFacing = Dir.RIGHT ;
		}
	}


	public void jump()
	{
		vertical_speed = 0 ;
		falling = false ;
		jumping = true ;
	}


	// accepts an ArrayList of Sprites and the delay(in milliseconds) between the frames
	private void animation(ArrayList<Sprite> list, int delay)
	{
		counter = 1 ;
		root.getChildren().add(list.get(counter-1).getImage()) ;	// put the first sprite of the ArrayList in the root
		currentSprite = list.get(counter-1) ;

		keyFrame = new KeyFrame(Duration.millis(delay), event ->
		{
			root.getChildren().remove(list.get(counter-1).getImage()) ;

			if(counter == list.size())	// once the last sprite in the list is reached...
				counter = 0 ;			// ...start again from the very first sprite of the list

			root.getChildren().add(list.get(counter).getImage()) ;

			currentSprite = list.get(counter) ;

			if(endTheGame)	endGame() ;

			if(jumping)
			{
				vertical_speed+=10 ;
				this.y-=vertical_speed ;

				if(vertical_speed > 30)
				{
					vertical_speed = 0 ;
					jumping = false ;
					falling = true ;
				}
			}

			if(falling)
			{
				if(this.vertical_speed > terminalVelocity)	
					this.vertical_speed = terminalVelocity ;
				else
					this.vertical_speed+=gravity ;

				this.y+=this.vertical_speed ;
				
				if(this.y > mapHeight)
				{
					Text txt = new Text("You Just Got Caked!") ;
					txt.setFill(Color.WHITE) ;
					txt.setStrokeWidth(1.5) ;
					txt.setStroke(Color.BLACK) ;
					txt.setFont(Font.font("mv boli", FontWeight.BOLD, FontPosture.REGULAR, 40)) ;
					txt.setX(60) ;
					txt.setY(mapHeight/2) ;

					root.getChildren().add(txt) ;
					endTheGame = true ;
				}
			}

			updateAllSprites() ;

			counter++ ;
		}) ;

		timeline = new Timeline(keyFrame) ;
		timeline.setCycleCount(Timeline.INDEFINITE) ;
		timeline.play() ;
	}


	// updates the coordinates of all the Sprites used
	private void updateAllSprites()
	{
		for(int index = 0 ; index < 2 ; index++)
		{
			rightList.get(index).setX(this.x) ;
			rightList.get(index).setY(this.y) ;
		
			leftList.get(index).setX(this.x) ;
			leftList.get(index).setY(this.y) ;
		}
	}


	private void initializeSprites(double x, double y)
	{
		String filename ;

		// sprites for being idle while facing the right
		for(int counter = 1 ; counter < 3 ; counter++)
		{
			// sprites for movement in the right direction
			filename = "black_rightidle_" + Integer.toString(counter) + ".png" ;
			Sprite tem3 = new Sprite(filename, x, y, this.width, this.height) ;
			tem3.setVelocity(this.velocityX, this.velocityY) ;
			rightList.add(tem3) ;

			// sprites for movement in the left direction
			filename = "black_leftidle_" + Integer.toString(counter) + ".png" ;
			Sprite temp4 = new Sprite(filename, x, y, this.width, this.height) ;
			temp4.setVelocity(this.velocityX, this.velocityY) ;
			leftList.add(temp4) ;
		}
	}


	public BoundingBox getBox()
	{
		return currentSprite.getBox() ;
	}


	public Bounds getBounds()
	{
		return currentSprite.getBounds() ;
	}


	public Double getX()
	{
		return this.x ;
	}

	public void setX(double x)
	{
		this.x = x ;
		updateAllSprites() ;
	}

	public Double getY()
	{
		return this.y ;
	}

	public void setY(double y)
	{
		this.y = y ;
		updateAllSprites() ;
	}

	public void stopTimeline()
	{
		this.timeline.stop() ;
	}

	public Double getGravity()
	{
		return this.gravity ;
	}

	public void falls(boolean f)
	{
		if(f == false) vertical_speed = 0 ;
		this.falling = f ;

		this.jumping = !this.falling ;
	}

	public Boolean isFalling()
	{
		return this.falling ;
	}

	public Boolean isJumping()
	{
		return this.jumping ;
	}

	public double getWidth()
	{
		return this.width ;
	}

	public double getHeight()
	{
		return this.height ;
	}

	public void setVelocityX(double vx)
	{
		this.velocityX = vx ;
	}

	public Double getVelocityX()
	{
		return this.velocityX ;
	}

	public void setVelocityY(double vy)
	{
		this.velocityY = vy ;
	}

	public Double getVelocityY()
	{
		return this.velocityY ;
	}
}
