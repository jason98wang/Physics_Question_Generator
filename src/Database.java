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

	// credentials used to track the gist file
	private static final String USERNAME = "plap2018";
	private static final String PASSWORD = "thinklikeaphysicist";
	private static final String ID = "e8cfd6bc0c6aff7d18170237f1b36b34";

	// github gist vars
	private static GistService service;
	private static Gist gist;
	private static GistFile gistFile;

	private static SimpleLinkedList<Subject> subjects;
	private static SimpleLinkedList<Symbol> symbols;
	private static SimpleLinkedList<SimpleLinkedList<Symbol>> formulas;

	private static File jsonFile;

	public static void main(String[] args) {
		Database db = new Database();
		db.update();
	}

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

	// private methods

	private void initGist() {
		// set user info
		service = new GistService();
		service.getClient().setCredentials(USERNAME, PASSWORD);

		// get database gist
		try {
			gist = service.getGist(ID);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// get gistfile from its respective gist
		gistFile = gist.getFiles().get("database");

		// get data from file
		String data = gistFile.getContent();

		try {
			FileWriter file = new FileWriter(jsonFile);
			file.write(data);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		interpretData();
	}

	private static void interpretData() {
		// take in array of subjects
		JSONObject all = null;
		try {
			all = (JSONObject) (new JSONParser()).parse(new FileReader(jsonFile));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		JSONArray array1 = (JSONArray) all.get("subjects");
		// for every subject
		for (Object a : array1) {
			JSONObject subject = (JSONObject) a;

			// get its name, grade and level
			String name = (String) subject.get("name");
			int grade = Integer.parseInt((String) subject.get("grade"));
			String level = (String) subject.get("level");

			// add subject to linkedlist
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

					// get its problem statement and its formula
					String problemStatement = (String) question.get("problem");
					SimpleLinkedList<Symbol> formula = stringToSymbols((String) question.get("formula"));

					Question q;

					if (formula != null) { // if question is calculable
						q = new Question(problemStatement, formula);
						formulas.add(formula);
					} else { // if question is not calculable
						BufferedImage image = stringToImage((String) question.get("image")); // SimpleLinkedList<String> 
						JSONArray specificQuestions = (JSONArray) question.get("sq");
						JSONArray specificAnswers = (JSONArray) question.get("sa");
						JSONArray possibleAnswers = (JSONArray) question.get("pa");

						SimpleLinkedList<String> sq = new SimpleLinkedList<String>();
						SimpleLinkedList<String> sa = new SimpleLinkedList<String>();
						SimpleLinkedList<String> pa = new SimpleLinkedList<String>();

						for (Object d : specificQuestions) {
							JSONObject jo = (JSONObject) d;
							String str = (String) jo.get("q");
							sq.add(str);
						}
						for (Object d : specificAnswers) {
							JSONObject jo = (JSONObject) d;
							String str = (String) jo.get("a");
							sa.add(str);
						}
						for (Object d : possibleAnswers) {
							JSONObject jo = (JSONObject) d;
							String str = (String) jo.get("p");
							pa.add(str);
						}

						// add new question
						q = new Question(problemStatement, sq, sa, pa, image);
					}
					u.addQuestion(q);
				}
			}

			// get this subject's units
			JSONArray students = (JSONArray) subject.get("students");
			for (Object b : students) {
				JSONObject student = (JSONObject) b;

				// get its name and number
				String name2 = (String) student.get("name");
				String studentNumber = (String) student.get("studentNumber");
				String password = (String) student.get("password");
				int incorrect = Integer.parseInt((String) student.get("incorrect"));
				int total = Integer.parseInt((String) student.get("total"));

				// add unit to subject
				Student st = new Student(name2, studentNumber, password, incorrect, total);
				s.getStudents().add(st);
			}
		}

		JSONArray array2 = (JSONArray) all.get("operations");
		for (Object a : array2) {
			JSONObject symbol = (JSONObject) a;

			// get its id
			String id = (String) symbol.get("id");
			Symbol s = new Operation(id);
			symbols.add(s);
		}

		JSONArray array3 = (JSONArray) all.get("variables");
		for (Object a : array3) {
			JSONObject symbol = (JSONObject) a;

			// get its id
			String id = (String) symbol.get("id");
			Symbol s = new Variable(id);
			symbols.add(s);
		}
	}

	private static SimpleLinkedList<Symbol> stringToSymbols(String str) {
		if (str.equals("null")) {
			return null;
		}
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		str = str.substring(1, str.length());

		while (str.length() > 0) {
			String sub = str.substring(0, str.indexOf(" "));			
			if ((sub.equals("+")) || (sub.equals("-")) || (sub.equals("mul")) || (sub.equals("div")) || (sub.equals("sqrt")) || (sub.equals("^")) || (sub.equals("(")) || (sub.equals(")"))) {
				formula.add(new Operation(sub));
			} else {
				formula.add(new Variable(sub));
			}
			str = str.substring(str.indexOf(" ") + 1);
		}

		return formula;
	}

	// converts string taken from database to image for the question
	private static BufferedImage stringToImage(String str) {
		if (str.equals("null")) {
			return null;
		}
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

	// public methods
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
						qobj.put("sq", new JSONArray());
						qobj.put("sa", new JSONArray());
						qobj.put("pa", new JSONArray());
					} else {
						JSONArray sq = new JSONArray();
						JSONArray sa = new JSONArray();
						JSONArray pa = new JSONArray();

						for (int l = 0; l < question.getSpecificQuestions().size(); l++) {
							JSONObject aobj = new JSONObject();
							aobj.put("q", question.getSpecificQuestions().get(l));
							sq.add(aobj);
						}
						for (int l = 0; l < question.getSpecificAnswers().size(); l++) {
							JSONObject aobj = new JSONObject();
							aobj.put("a", question.getSpecificAnswers().get(l));
							sa.add(aobj);
						}
						for (int l = 0; l < question.getPossibleAnswers().size(); l++) {
							JSONObject aobj = new JSONObject();
							aobj.put("p", question.getPossibleAnswers().get(l));
							pa.add(aobj);
						}

						qobj.put("formula", "null");
						qobj.put("image", question.imageToString());
						qobj.put("sq", sq);
						qobj.put("sa", sa);
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

	// getters
	public SimpleLinkedList<Subject> getSubjects() {
		return subjects;
	}

	public SimpleLinkedList<Symbol> getSymbols() {
		return symbols;
	}
	public SimpleLinkedList<SimpleLinkedList<Symbol>> getFormulas() {
		return formulas;
	}
}