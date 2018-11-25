import java.awt.Rectangle;
class Button{
  
  private boolean buttonClicked = false; 
   Rectangle boundingBox;
  private int x;
  private int y;
  
  Button(int x, int y,int width,int height){
    this.x = x;
    this.y=y;
    setBoundingBox(new Rectangle(x,y,width, height));
  }
  
  public void setButtonClicked(boolean buttonClicked){
    this.buttonClicked = buttonClicked;
  }
  
  public boolean getButtonClicked(){
    return buttonClicked;
  }

public Rectangle getBoundingBox() {
	return boundingBox;
}

public void setBoundingBox(Rectangle boundingBox) {
	this.boundingBox = boundingBox;
}
  
}