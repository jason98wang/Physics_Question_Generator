import java.awt.image.BufferedImage;

public class Variable extends Symbol{

	private double value;

	// constructor
	Variable(String id, BufferedImage image) {
		super(id, image);
		if (id.equals("pi")) {
		    value = Math.PI;
        }
	}

	Variable(String id) {
	    super(id);
        if (id.equals("pi")) {
            value = Math.PI;
        }
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
