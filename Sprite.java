import javafx.scene.image.* ;
import javafx.geometry.* ;

public class Sprite
{
	private double x, y ;
	private double width, height ;
	private double velocityX, velocityY ;
	private Image image ;
	private ImageView imageView ;
	private String filename ;

	public Sprite()
	{
		this.x = 0 ;
		this.y = 0 ;
		this.width = 0 ;
		this.height = 0 ;
	}

	public Sprite(String filename, double x, double y, double width, double height)
	{
		this.x = x ;
		this.y = y ;
		this.width = width ;
		this.height = height ;
		this.filename = filename ;

		setImage(this.filename) ;
	}

	private void setImage(String filename)
	{
		this.filename = filename ;
		this.image = new Image(this.filename, this.width, this.height, false, true) ;
		
		// create the ImageView
		this.imageView = new ImageView(this.image) ;
		this.imageView.setCache(true) ;
		this.imageView.setX(this.x) ;
		this.imageView.setY(this.y) ;
	}

	public Bounds getBounds()
	{
		return this.imageView.getBoundsInParent() ;
	}

	// returns a new BoundingBox depending on the Sprite's current position
	public BoundingBox getBox()
	{
		return new BoundingBox(this.x, this.y, this.width, this.height) ;
	}

	public void setX(double x)
	{
		this.x = x ;
		this.imageView.setX(this.x) ;
	}

	public void setY(double y)
	{
		this.y = y ;
		this.imageView.setY(this.y) ;
	}

	public void setVelocity(double vx, double vy)
	{
		this.velocityX = vx ;
		this.velocityY = vy ;
	}

	public void setWidth(double w)
	{
		this.width = w ;
		imageView.setFitWidth(this.width) ;
	}

	public void setHeight(double h)
	{
		this.height = h ;
		imageView.setFitHeight(this.height) ;
	}

	public Double getX()
	{
		return this.x ;
	}

	public Double getY()
	{
		return this.y ;
	}

	public Double getVelocityX()
	{
		return this.velocityX ;
	}

	public Double getVelocityY()
	{
		return this.velocityY ;
	}

	public Double getWidth()
	{
		return this.width ;
	}

	public Double getHeight()
	{
		return this.height ;
	}

	public ImageView getImage()
	{
		return this.imageView ;
	}

	public String getFilename()
	{
		return this.filename ;
	}
}