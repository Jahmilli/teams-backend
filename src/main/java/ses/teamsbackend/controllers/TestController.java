package ses.teamsbackend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/databaseTesting")
public class TestController {

  @GetMapping("/test")
  public String getString() {
    return "String got lol";
  }

  //This OVERWRITES data within the given reference.
  //If you want to update 
  @GetMapping("/saveString")
  public String saveString(){
    //Need to get an instance of the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    
    //You can reference an already existing reference
    DatabaseReference usersRef = database.getReference("users/This is an example of saving a string in a database");

    //Generate a random number
    Random r = new Random();
    
    //Either send it to the database once you're done to the reference using Async
    //usersRef.setValueAsync("This is an example of saving a string in a database", r.nextInt((100 - 10) + 1) + 10);

    //Or do a setValue and add a callback function 
    usersRef.setValue("This is an example of saving a string in a database",
     r.nextInt((100 - 10) + 1) + 10, new DatabaseReference.CompletionListener(){
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference){
          if (databaseError != null) {
            System.out.println("Data could not be saved " + databaseError.getMessage());
          } else {
            System.out.println("Data saved successfully.");
          }
        }
      });


    return "saveString called";
  }

  @GetMapping("/updateString")
  public String updateString(){
    //Need to get an instance of the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //You can reference an already existing reference
    DatabaseReference usersRef = database.getReference("users/This is an example of saving a string in a database");

    //Generate a random number
    Random r = new Random();

    //Put some data in a hashmap(This is also a valid way of updating data.)
    Map<String, Object> users = new HashMap<>();
    users.put("This is an example of saving a string in a database", r.nextInt((100 - 10) + 1) + 10);

    usersRef.updateChildrenAsync(users);

    return "updateString called";
  }

  @GetMapping("/retrieveString")
  public String getDatabaseString(){
    //Need to get an instance of the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    
    //Get reference
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = ref.child("users");

    return "retrieveString called";
  }
}