import java.io.IOException;
import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;

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

	public static void main(String[] args) {
		Database db = new Database();
		
		Subject s = new Subject("Physics", 12, "U"); 
		db.addSubject(s);
		Unit u = new Unit("TLAP", 1);
		db.addUnit(s, u);
		Question q = new Question("What is TLAP?", null);
		db.addQuestion(u, q);
		
		db.update();
	}

	Database() {
		subjects = new SimpleLinkedList<Subject>();
		symbols = new SimpleLinkedList<Symbol>();
		
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
		Map<String, GistFile> m = gist.getFiles();
		file = m.get("Database");

		// get data from file
		data = file.getContent();
	}

	private void updateGist() {
		try {
			service.updateGist(gist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void interpretGist() {
		
	}

	public void update() {
		for (int i = 0; i < subjects.size(); i++) {
			Subject s = subjects.get(i);
			data += "<<<" + s.getName() + "\n";

			for (int j = 0; j < s.getUnits().size(); j++) {
				Unit u = s.getUnits().get(j);
				data += "<<" + u.getName() + "\n";

				for (int k = 0; k < u.getQuestions().size(); k++) {
					Question q = u.getQuestions().get(k);
					data += "<" + q.getProblemStatement() + "\n";
					data += q.getFormula() + "\n";
					data += ">\n";
				}

				data += ">>\n";
			}

			data += ">>>\n";
		}
		
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

	public void addUnit(Subject s, Unit u) {
		s.addUnit(u);
	}

	public void removeUnit(Subject s, Unit u) {
		s.removeUnit(u);
	}

	public void addQuestion(Unit u, Question q) {
		u.addQuestion(q);
	}

	public void removeQuestion(Unit u, Question q) {
		u.removeQuestion(q);
	}

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
