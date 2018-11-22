import java.awt.image.BufferedImage;

public class Variable extends Symbol{
	
	// constructor
	Variable(String id, BufferedImage image) {
		super(id, image);
	}

	private double value;
	
	// setter
	public void setValue(double value) {
		this.value = value;
	}
	
	// getter
	public double getValue() {
		return value;
	}
}
