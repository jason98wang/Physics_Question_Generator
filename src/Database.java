import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;;

public class Database {

	// credentials used to track the gist file
	private static final String USERNAME = "plap2018";
	private static final String PASSWORD = "thinklikeaphysicist";
	private static final String ID = "e1d0f6a4738e8a0dea2dbed067a2f05a";

	// github gist vars
	private static GistService service;
	private static Gist gist;
	private static GistFile gistFile;

	private static SimpleLinkedList<Subject> subjects;
	private static SimpleLinkedList<Symbol> symbols;

	private static File jsonFile;

	public static void main(String[] args) {
		Database db = new Database();

		//		Subject s = new Subject("Physics", 12, "U"); 
		//		subjects.add(s);
		//		Unit u = new Unit("TLAP", 1);
		//		s.addUnit(u);
		//		Question q = new Question("What is TLAP?", null);
		//		u.addQuestion(q);

		db.update();
	}

	Database() {
		// fine json file
		jsonFile = new File("database/database.json");

		// init subjects and symbols
		subjects = new SimpleLinkedList<Subject>();
		symbols = new SimpleLinkedList<Symbol>();

		// setup gist and get data
		initGist();
	}

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
					SimpleLinkedList<Symbol> formula = toSymbol((String) question.get("formula")); // (SimpleLinkedList<Symbol>) question.get("formula");

					Question q = new Question(problemStatement, formula); // temp
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

	private static SimpleLinkedList<Symbol> toSymbol(String stringFormula) {
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		stringFormula = stringFormula.substring(1, stringFormula.length());

		while (stringFormula.length() > 0) {
			String sub = stringFormula.substring(0, stringFormula.indexOf(" "));			
			if ((sub.equals("+")) || (sub.equals("-")) || (sub.equals("mul")) || (sub.equals("div")) || (sub.equals("sqrt")) || (sub.equals("^")) || (sub.equals("(")) || (sub.equals(")"))) {
				formula.add(new Operation(sub));
			} else {
				formula.add(new Variable(sub));
			}
			stringFormula = stringFormula.substring(stringFormula.indexOf(" ") + 1);
		}

		return formula;
	}

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
					qobj.put("formula", question.toString());
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
			// write to file
			FileWriter file = new FileWriter(jsonFile);
			file.write(all.toJSONString());
			file.close();

			// read from file line by line
			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			String output = "";
			String s = "";
			while (s != null) {
				output += s;
				s = br.readLine();
			}
			br.close();

			// update gist
			gistFile.setContent(output);
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
}