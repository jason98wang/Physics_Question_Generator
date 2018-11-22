//import com.besaba.revonline.pastebinapi.response.*;
//import com.besaba.revonline.pastebinapi.Pastebin;
//import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
//
//public class Database {
//
//	//	private FileInputStream serviceAccount;
//	//	private FirebaseOptions options;
//	//	private DatabaseReference database;
//
//	public static void main(String[] args) {
//		Database db = new Database();
//
//		db.addQuestion(new Subject(), new Unit("Physics U", 12), new Question("smth", new SimpleLinkedList<Symbol>()));
//	}
//
//	// constructor
//	Database() {
//
//		final PastebinFactory factory = new PastebinFactory();
//		final Pastebin pastebin = factory.createPastebin("c83961406d6e246b28ecec08bc8b0c85");
//		final String pasteKey = "physics"; //"LAZD9ZCs";
//		final Response<String> pasteResponse = pastebin.getRawPaste(pasteKey);
//
//		// api key - b6f87a4bce9f7a84af46e614638a815a
//		if (pasteResponse.hasError()) {
//			System.out.println("Unable to read paste content!");
//			return;
//		}
//		System.out.println(pasteResponse.get());
//
//		//		try {
//		//			database = FirebaseDatabase.getInstance().getReference();
//		//			serviceAccount = new FileInputStream("./physics-question-generator-firebase-adminsdk-avcwb-1581389e4d.json");
//		//
//		//			options = new FirebaseOptions.Builder()
//		//					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
//		//					.setDatabaseUrl("https://physics-question-generator.firebaseio.com/")
//		//					.build();
//		//
//		//			FirebaseApp.initializeApp(options);
//		//
//		//
//		//		} catch (IOException e) {
//		//			e.printStackTrace();
//		//		}
//	}
//
//	public void addQuestion(Subject subject, Unit unit, Question question) {
//		
//
//		//		CompletionListener cl = new CompletionListener() {
//		//			public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
//		//			}
//		//		};
//		//
//		//		database.child("physics").child("subjects").child(subject.getName()).child(unit.getName()).child("question").setValue(question, cl);
//	}
//
//	public void getData() {
//
//	}
//
//
//}

//package com.besaba.revonline.pastebinexamples;

import java.io.IOException;
import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;

public class Database {

	private static final String USERNAME = "plap2018";
	private static final String PASSWORD = "thinklikeaphysicist";
	private static GistService service;
	private static Gist gist;
	private static GistFile file;
	private static String data;
	private static SimpleLinkedList<Subject> subjects;

	public static void main(String[] args) {
		Database db = new Database();
		db.interpretData();
	}
	
	Database() {
		// set user info
		service = new GistService();
		service.getClient().setCredentials(USERNAME, PASSWORD);
		
		// get database gist
		try {
			gist = service.getGist("e1d0f6a4738e8a0dea2dbed067a2f05a");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// get gistfile from its respective gist
		Map<String, GistFile> m = gist.getFiles();
		file = m.get("Database");
		
		// get data from file
		data = file.getContent();
		
		System.out.println(data.indexOf("\n"));
		System.out.println(data.lastIndexOf("\n"));
	}
	
	public void addSubject(Subject s) {
		subjects.add(s);
	}
	
	public void removeSubject(Subject s) {
		subjects.add(s);
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
	
	public void setData() {
		file.setContent("a\ndfadf\n");
		try {
			service.updateGist(gist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void interpretData() {
		
	}
}
