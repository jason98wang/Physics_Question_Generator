import java.awt.image.BufferedImage;

public class Symbol {
	
	// init vars
	private String id;
	private BufferedImage image;
	
	// constructor
	Symbol(String id, BufferedImage image) {
		this.id = id;
		this.image = image;
	}

	Symbol(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public BufferedImage getImage() {
		return image;
	}
}
