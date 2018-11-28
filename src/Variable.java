import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Variable extends Symbol{

	private double value;

	// constructor
	Variable(String id) {
	    super(id);
	    try {
			setImage(ImageIO.read(new File("Symbols/Variables/" + id + ".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (id.equals("pi")) {
            value = Math.PI;
        } else if (id.equals("g")) {
		value = 9.81;
	} else if (id.equals("c")) {
		value = 3.0 * Math.pow(10,8);
	} else {
	    	try {
	    	    value = Double.parseDouble(id);
            } catch (NumberFormatException e) {

            }
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
