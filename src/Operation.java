/**
 * [Operation.java]
 * Operation object to be held as part of a formula
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Date: December 09, 2018
 */

//Java imports
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Operation extends Symbol{

    private int precedence; //Precedence (mathematical) - for order of operations - Greater int means higher precedence

    /**
     * Operation
     * Constructor that makes an Operation object with a given id
     * @param id, the String id of the operation
     */
    Operation(String id) {
        super(id); //Symbol constructor with id passed in
	    try {
			setImage(ImageIO.read(new File("Symbols/Operations/" + id + ".png"))); //Set appropriate image
		} catch (IOException e) {
			e.printStackTrace(); //If there is no appropriate image
		}
        if ((super.getId().equals("+")) || (super.getId().equals("-"))) {
            precedence = 1; //+ and - have lower precedence
        } else if ((super.getId().equals("mul")) || (super.getId().equals("div"))) {
            precedence = 2; //* and / have higher precedence
        } else if ((super.getId().equals("(")) || (super.getId().equals(")")))  {
        	precedence = 0; //Precedence of 0 for brackets needed for calculation aspect
        } else {
            precedence = 3; //Exponents have greatest precedence
        }
    } //End of constructor

    /**
     * getOperation
     * Returns the id of the operation
     * @return id, the id of the operation
     */
    public String getOperation() {
        return super.getId();
    } //End of getOperation

    /**
     * getPrecedence
     * Returns the numerical precedence of the operation
     * @return precedence, the integer precedence
     */
    public int getPrecedence() {
        return precedence;
    } //End of getPrecedence

} //End of class
