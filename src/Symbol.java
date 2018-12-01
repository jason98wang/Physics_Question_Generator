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
		this.image = null;
		//		try {
		//			double v = Integer.parseInt(id);
		//			
		//		} catch (NumberFormatException e) {
		//		try {
		//			this.image = ImageIO.read(new File(id + ".png"));
		//		} catch (IOException e1) {
		//			e1.printStackTrace();
		//		}
		//		}
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public BufferedImage getImage() {
		return image;
	}
}
