/**
 * [Database].java
 * Database is a class used to communicate between the Gist File where all the data is stored
 * and the user interfaces, namely StudentInfo, Login, QuizEditor
 * It stores all the information about subjects, their units and questions, and student accuracy 
 * in a JSON format, by utilizing a local JSON file
 * @author      Yili Liu
 * @since       Nov.20.2018
 */

// IMPORTS
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import data_structures.SimpleLinkedList;

public class Database {

	// INITIALIZE VARIABLES
	// credentials used to track the gist file
	private static final String USERNAME = "plap2018";
	private static final String PASSWORD = "thinklikeaphysicist";
	private static final String ID = "e8cfd6bc0c6aff7d18170237f1b36b34";

	// github gist vars
	private static GistService service;
	private static Gist gist;
	private static GistFile gistFile;

	// variables taken from database
	private static SimpleLinkedList<Subject> subjects; // subjects
	private static SimpleLinkedList<Symbol> symbols; // symbols
	private static SimpleLinkedList<SimpleLinkedList<Symbol>> formulas; // previously stored formulas

	// json file used to interpret data from database
	// and to store data to database
	private static File jsonFile;

	// CONSTRUCTOR
	/**
	 * Constructor
	 * Instantiates json file, subjects, symbols and formulas
	 * and initializes gist database
	 */
	Database() {
		// fine json file
		jsonFile = new File("database/database.json");

		// init subjects and symbols
		subjects = new SimpleLinkedList<Subject>();
		symbols = new SimpleLinkedList<Symbol>();
		formulas = new SimpleLinkedList<SimpleLinkedList<Symbol>>();

		// setup gist and get data
		initGist();
	}

	// PRIVATE METHODS
	/**
	 * initGist
	 * This method establishes connections to the gist database,
	 * reads from it,
	 * writes to the json file,
	 * and interprets the data
	 */
	private static void initGist() {
		// set github user info
		service = new GistService();
		service.getClient().setCredentials(USERNAME, PASSWORD);

		// get database gist
		try {
			gist = service.getGist(ID);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// get gist file from its respective gist
		gistFile = gist.getFiles().get("database");

		// get data from file
		String data = gistFile.getContent();

		// write data to the json file
		try {
			FileWriter file = new FileWriter(jsonFile);
			file.write(data);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// interpret data
		interpretData();
	}

	/**
	 * interpretData
	 * This method parses through the json file to interpret the data
	 */
	private static void interpretData() {
		// take in array of operations, variables, and subjects
		JSONObject all = null;
		try {
			all = (JSONObject) (new JSONParser()).parse(new FileReader(jsonFile));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// get subjects array
		JSONArray array1 = (JSONArray) all.get("subjects");

		// for every subject
		for (Object a : array1) {
			// convert object to JSONObject
			JSONObject subject = (JSONObject) a;

			// get its name, grade and level
			String name = (String) subject.get("name");
			int grade = Integer.parseInt((String) subject.get("grade"));
			String level = (String) subject.get("level");

			// add subject to linked list
			Subject s = new Subject(name, grade, level);
			subjects.add(s);

			// get this subject's units
			JSONArray units = (JSONArray) subject.get("units");

			// for every unit
			for (Object b : units) {
				JSONObject unit = (JSONObject) b;

				// get its name and number
				String name2 = (String) unit.get("name");
				int number = Integer.parseInt((String) unit.get("number"));

				// add unit to subject
				Unit u = new Unit(name2, number);
				s.getUnits().add(u);

				// get this unit's questions
				JSONArray questions = (JSONArray) unit.get("questions");

				// for every question
				for (Object c : questions) {
					JSONObject question = (JSONObject) c;

					// get its problem statement, its formula and its image
					String problemStatement = (String) question.get("problem");
					SimpleLinkedList<Symbol> formula = stringToSymbols((String) question.get("formula"));
					BufferedImage image = stringToImage((String) question.get("image"));

					// initialize question
					Question q;

					if (formula != null) { // if question is calculable
						if (image == null) {
							// instantiate question using only the problem statement and formula
							q = new Question(problemStatement, formula);
						} else {
							// instantiate question using the problem statement, formula and image
							q = new Question(problemStatement, formula, image);
						}
						formulas.add(formula);
					} else { // if question is not calculable
						// get questions and their respective answers, and other possible answers
						JSONArray questionsAnswers = (JSONArray) question.get("qa");
						JSONArray possibleAnswers = (JSONArray) question.get("pa");

						SimpleLinkedList<String> sq = new SimpleLinkedList<String>(); // specific questions
						SimpleLinkedList<String> sa = new SimpleLinkedList<String>(); // specific answers
						SimpleLinkedList<String> pa = new SimpleLinkedList<String>(); // possible answers

						// for every question and its answer
						for (Object d : questionsAnswers) {
							JSONObject jo = (JSONObject) d;
							String possibleQuestion = (String) jo.get("q"); // get the question
							String possibleAnswer = (String) jo.get("a"); // and its answer

							// add them to lists of specific questions and answers
							sq.add(possibleQuestion);
							sa.add(possibleAnswer);
						}

						// for every other answer
						for (Object d : possibleAnswers) {
							JSONObject jo = (JSONObject) d;
							String str = (String) jo.get("p"); // get the possible answer

							// add them to list of possible answers
							pa.add(str);
						}

						// instantiate question using the problem statement,
						// specific questions and answers,
						// possible answers,
						// and its image if applicable
						if (image == null) {
							q = new Question(problemStatement, sq, sa, pa);
						} else {
							q = new Question(problemStatement, sq, sa, pa, image);
						}
					}
					// add question to unit
					u.addQuestion(q);
				}
			}

			// get this subject's students
			JSONArray students = (JSONArray) subject.get("students");
			// for every student
			for (Object b : students) {
				JSONObject student = (JSONObject) b;

				// get their name, student number, password, incorrect questions and total questions answered
				String name2 = (String) student.get("name");
				String studentNumber = (String) student.get("studentNumber");
				String password = (String) student.get("password");
				int incorrect = Integer.parseInt((String) student.get("incorrect"));
				int total = Integer.parseInt((String) student.get("total"));

				// create new student
				Student st = new Student(name2, studentNumber, password, incorrect, total);
				// add student to subject
				s.getStudents().add(st);
			}
		}

		// get operations array
		JSONArray array2 = (JSONArray) all.get("operations");
		for (Object a : array2) {
			JSONObject symbol = (JSONObject) a;

			// get its id
			String id = (String) symbol.get("id");
			
			// create new operation
			Symbol s = new Operation(id);
			
			// add it to list of symbols
			symbols.add(s);
		}

		// get variables array
		JSONArray array3 = (JSONArray) all.get("variables");
		for (Object a : array3) {
			JSONObject symbol = (JSONObject) a;

			// get its id
			String id = (String) symbol.get("id");
			
			// create new variable
			Symbol s = new Variable(id);
			
			// add it to list of symbols
			symbols.add(s);
		}
	}

	/**
	 * stringToSymbols
	 * This method takes in a string and converts it into a list of symbols
	 * @param String str, a string containing symbols
	 * @return SimpleLinkedList<Symbol> formula, formula required for the question
	 */
	private static SimpleLinkedList<Symbol> stringToSymbols(String str) {
		// if there is no formula, return null
		if (str.equals("null")) {
			return null;
		}
		// create new list of symbols
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		// remove the first space
		str = str.substring(1, str.length());

		// while string is not empty
		while (str.length() > 0) {
			// symbol to be converted is stored in sub
			String sub = str.substring(0, str.indexOf(" "));
			
			// check if it is an operation or variable and add to formula
			if ((sub.equals("+")) || (sub.equals("-")) || (sub.equals("mul")) || (sub.equals("div")) || (sub.equals("sqrt")) || (sub.equals("^")) || (sub.equals("(")) || (sub.equals(")"))) {
				formula.add(new Operation(sub));
			} else {
				formula.add(new Variable(sub));
			}
			
			// shorten string
			str = str.substring(str.indexOf(" ") + 1);
		}

		// return the list of symbols
		return formula;
	}

	/**
	 * stringToImage
	 * converts string taken from database to a byte array
	 * then from the byte array to an image
	 * @param String str, a base64 string representing the image
	 * @return BufferedImage image, the image required for the question
	 */
	private static BufferedImage stringToImage(String str) {
		// if there is no image, return null
		if (str.equals("null")) {
			return null;
		}
		
		// make decode string to byte array
		BufferedImage image = null;
		byte[] bytes = Base64.getDecoder().decode(str);
		
		// input the byte array
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		
		try {
			// read from the input stream and convert into image
			image = ImageIO.read(bais);
			bais.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// return the image
		return image;
	}

	// PUBLIC METHODS
	/**
	 * update
	 * This method writes data to the json file, reads from the json file line by line
	 * and pushes it to the gist database
	 */
	public void update() {
		JSONObject all = new JSONObject();

		JSONArray array1 = new JSONArray();
		for (int i = 0; i < subjects.size(); i++) {
			Subject subject = subjects.get(i);
			JSONObject sobj = new JSONObject();
			sobj.put("name", subject.getName());
			sobj.put("grade", Integer.toString(subject.getGrade()));
			sobj.put("level", subject.getLevel());

			JSONArray b = new JSONArray();
			for (int j = 0; j < subject.getUnits().size(); j++) {
				Unit unit = subject.getUnits().get(j);
				JSONObject uobj = new JSONObject();
				uobj.put("name", unit.getName());
				uobj.put("number", Integer.toString(unit.getNum()));

				JSONArray c = new JSONArray();
				for (int k = 0; k < unit.getQuestions().size(); k++) {
					Question question = unit.getQuestions().get(k);
					JSONObject qobj = new JSONObject();

					qobj.put("problem", question.getProblemStatement());

					if (question.isNumerical()) {
						qobj.put("formula", question.toString());
						qobj.put("image", "null");
						qobj.put("qa", new JSONArray());
						qobj.put("pa", new JSONArray());
					} else {
						JSONArray qa = new JSONArray();
						JSONArray pa = new JSONArray();

						for (int l = 0; l < question.getSpecificQuestions().size(); l++) {
							JSONObject aobj = new JSONObject();
							aobj.put("q", question.getSpecificQuestions().get(l));
							aobj.put("a", question.getSpecificAnswers().get(l));
							qa.add(aobj);
						}
						for (int l = 0; l < question.getPossibleAnswers().size(); l++) {
							JSONObject aobj = new JSONObject();
							aobj.put("p", question.getPossibleAnswers().get(l));
							pa.add(aobj);
						}

						qobj.put("formula", "null");
						qobj.put("image", question.imageToString());
						qobj.put("qa", qa);
						qobj.put("pa", pa);
					}

					c.add(qobj);
				}
				uobj.put("questions", c);
				b.add(uobj);
			}

			JSONArray d = new JSONArray();
			for (int j = 0; j < subject.getStudents().size(); j++) {
				Student student = subject.getStudents().get(j);
				JSONObject tobj = new JSONObject();
				tobj.put("name", student.getName());
				tobj.put("studentNumber", student.getStudentNumber());
				tobj.put("password", student.getPassword());
				tobj.put("incorrect", Integer.toString(student.getIncorrectQuestions()));
				tobj.put("total", Integer.toString(student.getTotalQuestions()));
				d.add(tobj);
			}

			sobj.put("units", b);
			sobj.put("students", d);
			array1.add(sobj);
		}

		JSONArray array2 = new JSONArray();
		JSONArray array3 = new JSONArray();

		for (int i = 0; i < symbols.size(); i++) {
			Symbol s = symbols.get(i);
			JSONObject sobj = new JSONObject();
			sobj.put("id", s.getId());
			if (s instanceof Operation) {
				array2.add(sobj);
			} else {
				array3.add(sobj);
			}
		}

		all.put("subjects", array1);
		all.put("operations", array2);
		all.put("variables", array3);

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(all.toJSONString());
			String prettyJsonString = gson.toJson(je);

			// write to file
			FileWriter file = new FileWriter(jsonFile);
			file.write(prettyJsonString);
			file.close();

			// update gist
			gistFile.setContent(prettyJsonString);
			service.updateGist(gist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getSubjects
	 * This method returns a linked list of subjects from the database
	 * @return subjects, a linked list of subjects
	 */
	public SimpleLinkedList<Subject> getSubjects() {
		return subjects;
	}

	/**
	 * getSymbols
	 * This method returns a linked list of symbols
	 * @return symbols, a linked list of symbols
	 */
	public SimpleLinkedList<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * getFormulas
	 * This method returns a linked list of linked list of symbols
	 * @return formulas, a linked list of linked list of symbols
	 */
	public SimpleLinkedList<SimpleLinkedList<Symbol>> getFormulas() {
		return formulas;
	}
}