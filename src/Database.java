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
	private static GistFile file;
	private static String data;

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
		jsonFile = new File("database.json");

		// init subjects and symbols
		subjects = new SimpleLinkedList<Subject>();
		symbols = new SimpleLinkedList<Symbol>();

		// setup gist and get data
		initGist();

		// output data to json file
		fileOutput();
		// interpret data from json file
		fileInput();
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
		file = gist.getFiles().get("database");

		// get data from file
		data = file.getContent();
	}

	private static void fileInput() {
		// take in array of subjects
		JSONArray array = null;
		try {
			array = (JSONArray) (new JSONParser()).parse(new FileReader(jsonFile));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// for every subject
		for (Object a : array) {
			JSONObject subject = (JSONObject) a;

			// get its name, grade and level
			String name = (String) subject.get("name");
			int grade = Integer.parseInt((String) subject.get("grade"));
			String level = (String) subject.get("level");

			// add subject to linkedlist
			Subject s = new Subject(name, grade, level);
			subjects.add(s);
			System.out.println("made new subject " + name + " " + grade + " " + level);

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
				s.addUnit(u);
				System.out.println("made new unit " + name + " " + number);

				// get this unit's questions
				JSONArray questions = (JSONArray) unit.get("questions");

				// for every question
				for (Object c : questions) {
					JSONObject question = (JSONObject) c;

					// get its problem statement and its formula
					String problemStatement = (String) question.get("problem");
					SimpleLinkedList<Symbol> formula = toSymbol((String) question.get("formula")); // (SimpleLinkedList<Symbol>) question.get("formula");

					for (int i = 0; i < formula.size(); i++) {
						System.out.print(formula.get(i).getId() + " ");
					}
					System.out.println();

					Question q = new Question(problemStatement, formula); // temp
					u.addQuestion(q);
					System.out.println("made new question " + problemStatement + " " + formula);					
				}
			}
		}
	}

	private static SimpleLinkedList<Symbol> toSymbol(String stringFormula) {
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		stringFormula = stringFormula.substring(1, stringFormula.length());
		while (stringFormula.length() > 0) {
			String sub = stringFormula.substring(0, stringFormula.indexOf(" "));
			if ((sub.equals("+")) || (sub.equals("-")) || (sub.equals("*")) || (sub.equals("/")) || (sub.equals("sqrt")) || (sub.equals("^")) || (sub.equals("(")) || (sub.equals(")"))) {
				formula.add(new Operation(sub));
			} else {
				formula.add(new Variable(sub));
			}
			stringFormula = stringFormula.substring(stringFormula.indexOf(" ") + 1);
			// toSymbol(stringFormula.substring(stringFormula.indexOf(" ",1)));
		}

		return formula;
	}

	private static void fileOutput() {
		// write to json file
		try {
			FileWriter file = new FileWriter(jsonFile);
			file.write(data);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		file.setContent(data);
		try {
			service.updateGist(gist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// add and remove subjects, units, and questions
	public void addSubject(Subject s) {
		subjects.add(s);
	}

	public void removeSubject(Subject s) {
		subjects.remove(s);
	}

	//	public void addUnit(Subject s, Unit u) {
	//		s.addUnit(u);
	//	}
	//
	//	public void removeUnit(Subject s, Unit u) {
	//		s.removeUnit(u);
	//	}
	//
	//	public void addQuestion(Unit u, Question q) {
	//		u.addQuestion(q);
	//	}
	//
	//	public void removeQuestion(Unit u, Question q) {
	//		u.removeQuestion(q);
	//	}

	public void addSymbol(Symbol s) {
		symbols.add(s);
	}

	public void removeSymbol(Symbol s) {
		symbols.remove(s);
	}

	// getters
	public SimpleLinkedList<Subject> getSubjects() {
		return subjects;
	}

	public SimpleLinkedList<Symbol> getSymbols() {
		return symbols;
	}
}
