import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Variable extends Symbol{

	private double value;
	private boolean constant;

	// constructor
	Variable(String id) {
		super(id);
		try {
			setImage(ImageIO.read(new File("Symbols/Variables/" + id + ".png")));
		} catch (IOException e) {
			System.out.println("io exception -> " + id);
		}
		if (id.equals("pi")) {
			value = Math.PI;
			constant = true;
		} else if (id.equals("g")) {
			value = 9.81;
			constant = true;
		} else if (id.equals("c")) {
			value = 3.0 * Math.pow(10,8);
			constant = true;
		} else if (id.equals("k")) {
			value = 8.99 * Math.pow(10,9);
			constant = true;
		} else {
			try {
				value = Double.parseDouble(id);
			} catch (NumberFormatException e) {

			}
			if (value != 0.0) {
				constant = true;
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
	
	public boolean isConstant() {
		return constant;
	}
}
