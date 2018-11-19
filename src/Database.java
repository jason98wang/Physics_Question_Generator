import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
	
	private FileInputStream serviceAccount;
	private FirebaseOptions options;
	private DatabaseReference database;
	
	// constructor
	Database() {
		try {
			database = FirebaseDatabase.getInstance().getReference();
			serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

			options = new FirebaseOptions.Builder()
			    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			    .setDatabaseUrl("https://physics-question-generator.firebaseio.com/")
			    .build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addQuestion(String unit, String problemStatement, SimpleLinkedList<Symbol> formula) {
	    Question q = new Question(problemStatement, formula);

	    // database.child("subjects").child(unit).child("question").setValue(q);
	}
	
	public void getData() {
		
	}
	
	
}
