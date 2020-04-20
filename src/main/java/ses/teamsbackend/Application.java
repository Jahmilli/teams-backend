package ses.teamsbackend;

import java.io.FileInputStream;  
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  //You need to point the credentials to the security JSON file.
  final static String googleCredentialsPath = "/mnt/c/ses2b-group2-firebase-adminsdk-kpcwh-3e4df0559c.json";

  public static void main(String[] args) {
    FirebaseOptions options;
    FileInputStream serviceAccount;
    
    try{
      serviceAccount = new FileInputStream(googleCredentialsPath);
    }
    catch(Exception e){
      serviceAccount = null;
      System.out.println("Error Reading filestream");
    }

    try {
      options = new FirebaseOptions.Builder()
      .setCredentials(GoogleCredentials.fromStream(serviceAccount))
      .setDatabaseUrl("https://ses2b-group2.firebaseio.com/")
      .build();
      FirebaseApp.initializeApp(options);
    }
    catch(Exception e){
      options = null;
      //Will crash.
    }
    SpringApplication.run(Application.class, args);
    
  }
}