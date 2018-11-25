import java.awt.image.BufferedImage;

public class Variable extends Symbol{

	private double value;

	// constructor
	Variable(String id, BufferedImage image) {
		super(id, image);
		value = Double.parseDouble(id);
	}

	Variable(String id) {
	    super(id);
        value = Double.parseDouble(id);
    }

	// setter
	public void setValue(double value) {
		this.value = value;
	}
	
	// getter
	public double getValue() {
		return value;
	}
}
