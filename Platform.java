import javafx.scene.layout.* ;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.* ;

public class Platform
{
	private Pane pane ;
	private Sprite sprite ;
	private double x, y, width, height ;
	private boolean is_on, was_touched, serrated ;
	private Color color ;
	private Rectangle r1 ;


	public Platform(Pane p, double x, double y, double w, double h, boolean s)
	{
		this.pane = p ;
		this.x = x ;
		this.y = y ;
		this.width = w ;
		this.height = h ;
		this.is_on = false ;
		this.was_touched = false ;
		this.serrated = s ;

		if(serrated)
		{
			this.sprite = new Sprite("SerratedPlatform.png", this.x, this.y, this.width, this.height) ;
			this.pane.getChildren().add(this.sprite.getImage()) ;
		}
		else
		{
			r1 = new Rectangle(x, y, width, height) ;
			r1.setFill(this.color) ;
			pane.getChildren().add(r1) ;
		}
	}

	public BoundingBox getBox()
	{
		return new BoundingBox(this.x, this.y, this.width, this.height) ;
	}

	public Bounds getBounds()
	{
		if(serrated)
			return this.sprite.getImage().getBoundsInParent() ;
		else
			return this.r1.getBoundsInParent() ;
	}

	public void remove()
	{
		if(!serrated)
			this.pane.getChildren().remove(this.r1) ;
		else
			this.pane.getChildren().remove(this.sprite.getImage()) ;
	}

	public void setFill(Color c)
	{
		if(!serrated)
			this.r1.setFill(c) ;
	}

	public void onPlatform(boolean b)
	{
		this.is_on = b ;

		if(!was_touched) was_touched = true ;
	}

	public Boolean isOn()
	{
		return this.is_on ;
	}

	public Boolean wasTouched()
	{
		return this.was_touched ;
	}

	public Double getX()
	{
		return this.x ;
	}

	public void setX(double x)
	{
		this.x = x ;
		
		if(!serrated)
			r1.setX(this.x) ;
		else
			sprite.setX(this.x) ;
	}

	public Double getY()
	{
		return this.y ;
	}

	public void setY(double y)
	{
		this.y = y ;

		if(!serrated)
			r1.setY(this.y) ;
		else
			sprite.setY(this.y) ;
	}

	public Double getWidth()
	{
		return this.width ;
	}

	public Double getHeight()
	{
		return this.height ;
	}

	public  Boolean isSerrated()
	{
		return this.serrated ;
	}
}