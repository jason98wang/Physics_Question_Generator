/**
 * [Variable.java]
 * Variable object representing a variable or constant
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Date: December 09, 2018
 */

//Imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Variable extends Symbol{

	private double value; //Value of the variable
	private boolean constant; //Flag to mark whether it is a constant or not

    /**
     * Variable
     * This constructor makes a variable object with a given id
     * @param id, a String representing the id of the variable
     */
	Variable(String id) {
		super(id); //Make a symbol with the given id
		try {
			setImage(ImageIO.read(new File("Symbols/Variables/" + id + ".png"))); //Set the associated image
		} catch (IOException e) {
			System.out.println("io exception -> " + id); //If there ie no associated image
		}
		if (id.equals("pi")) { //If the variable created is pi (id is pi)
			value = Math.PI;
			constant = true; //Flag as constant
		} else if (id.equals("g")) { //If the variable created is g (id is g)
			value = 9.81;
			constant = true; //Flag as constant
		} else if (id.equals("c")) { //If the variable created is c (id is c)
			value = 3.0 * Math.pow(10,8);
			constant = true; //Flag as constant
		} else if (id.equals("k")) { //If the variable created is k (id is k)
			value = 8.99 * Math.pow(10,9);
			constant = true; //Flag as constant
		} else {
			try {
				value = Double.parseDouble(id); //Set the value to the id if the id can be parsed as a double
			} catch (NumberFormatException e) {

			}
			if (value != 0.0) {
				constant = true; //If id was set earlier (id can be parsed as double), flag as constant
			}
		}
	} //End of constructor

    /**
     * setValue
     * Sets the value of the variable
     * @param value, the double value to be set
     */
	public void setValue(double value) {
		this.value = value;
	} //End of setValue

    /**
     * getValue
     * Returns the value of the variable
     * @return value, the double value of the variable
     */
	public double getValue() {
		return value;
	} //End of getValue

    /**
     * isConstant
     * Returns whether the variable is a constant
     * @return constant, the boolean indicating whether the variable is a constant
     */
	public boolean isConstant() {
		return constant;
	} //End of isConstant

} //End of class
