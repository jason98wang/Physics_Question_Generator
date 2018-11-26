import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	    try {
			setImage(ImageIO.read(new File("Symbols/Variables/" + id + ".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
