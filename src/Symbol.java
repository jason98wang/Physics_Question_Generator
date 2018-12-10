/**
 * [Symbol.java]
 * Symbol object to represent a symbol in a formula
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Date: December 09, 2018
 */

//Generic???????????

//Imports
import java.awt.image.BufferedImage;

public class Symbol {

	//Init variables
	private String id;
	private BufferedImage image;

	/**
	 * Symbol
	 * Constructor that makes a symbol object with a given id and image
	 * @param id, a String id
	 * @param image, a BufferedImage that is the image of the symbol
	 */
	Symbol(String id, BufferedImage image) {
		this.id = id;
		this.image = image;
	} //End of constructor

	/**
	 * Symbol
	 * Constructor that makes a symbol object with a given id
	 * @param id, a String id
	 */
	Symbol(String id) {
		this.id = id;
		this.image = null;
	} //End of constructor

	/**
	 * setImage
	 * Sets the image representing the symbol
	 * @param image, the BufferedImage to be set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	} //End of setImage

	/**
	 * getId
	 * Returns the id of the symbol
	 * @return id, the String id
	 */
	public String getId() {
		return id;
	} //End of getId

	/**
	 * getImage
	 * Returns the image of the symbol
	 * @return image, the BufferedImage of the symbol
	 */
	public BufferedImage getImage() {
		return image;
	} //End of getImage

} //End of class
