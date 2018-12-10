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

import data_structures.SimpleLinkedList;

public class test {

	private static BufferedImage image;

	public static void main(String[] args) throws IOException {
		
		SimpleLinkedList<Integer> test = new SimpleLinkedList<Integer>();
		
		test.add(2);
		test.add(3);
		test.add(4);
		test.add(5);
		
		System.out.println(test.contain(6));

	}

}
