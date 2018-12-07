import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class test {

	private static BufferedImage image;

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(new File("testing.txt"));
		String s = "";
		while (sc.hasNextLine()) {
			s += sc.nextLine();
		}
		image = stringToImage(s);
		sc.close();
		
		JFrame f = new JFrame();
		Panel p = new Panel();

		f.add(p);

		p.setVisible(true);
		f.setVisible(true);
		f.setSize(600, 400);
	}

	// converts string taken from database to image for the question
	private static BufferedImage stringToImage(String str) {
		BufferedImage image = null;
		byte[] bytes = Base64.getDecoder().decode(str);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			image = ImageIO.read(bais);
			bais.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static String imageToString(BufferedImage img) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", baos);
			byte[] bytes = baos.toByteArray();
			baos.close();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("returned null");
		return null;
	}

	private static class Panel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);

			g.drawImage(image, 0, 0, this);

			repaint();
		}
	}

}
