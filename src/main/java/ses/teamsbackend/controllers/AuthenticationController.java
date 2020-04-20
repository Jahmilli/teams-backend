package ses.teamsbackend.controllers;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord.*;
import com.google.firebase.database.*;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ses.teamsbackend.Email;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.json.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class AuthenticationController {
    
  //Request sign in function. Requires JSON fields
  /*
    {
      "email": [email],
      "password": [password]
    }
  */
  //Do not use for irl data. Unsecure as all hell.
  @RequestMapping(value = "/requestSignIn", method = RequestMethod.POST)
  public String signIn(@RequestBody String payload){
    JSONObject jsonPayload = new JSONObject(payload);
    HttpClient client = HttpClient.newHttpClient();

    JSONObject signinRequest = new JSONObject();

    signinRequest.put("email", jsonPayload.getString("email"));
    signinRequest.put("password", jsonPayload.getString("password"));
    signinRequest.put("returnSecureToken", true);

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyADjSvD59x-xPFvLQOlfBJQ0HyPrh9O_Hc"))
      .POST(HttpRequest.BodyPublishers.ofString(signinRequest.toString()))
      .build();
    
    try{  
      HttpResponse<String> response = client.send(
        request, HttpResponse.BodyHandlers.ofString());

      return FirebaseAuth.getInstance().getUserByEmail(signinRequest.getString("email")).getUid();
    }
    catch(Exception e){
      e.printStackTrace();
      return "FAILURE";
    }
  }

  //Registering a new user creates a new entry in the realtime database.
  @RequestMapping(value = "/requestRegister", method = RequestMethod.POST)
  public String register(@RequestBody String payload){
    JSONObject jsonPayload = new JSONObject(payload);
    String userID = null;
    String email = jsonPayload.getString("email"); 
    String password = jsonPayload.getString("password");
    String name = jsonPayload.getString("name");
    
    //Create a new request with all relevant data.
    CreateRequest request = new CreateRequest()
      .setEmail(email)
      .setPassword(password)
      .setDisplayName(name);
    
    //Try to create the user. Abort on failure.
    try{
      userID = FirebaseAuth.getInstance().createUser(request).getUid();
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference emailRef = database.getReference("users/" + userID + "/email");      
      DatabaseReference nameRef = database.getReference("users/" + userID + "/name");

      //Add entries to database for user.
      emailRef.setValueAsync(jsonPayload.getString("email"));
      nameRef.setValueAsync(jsonPayload.getString("name"));
    }
    catch(FirebaseAuthException e){
      e.printStackTrace();
      return "FAILURE";
    }

    return userID;
  }

  //Update password. Requires JSON file with following fields
  /*
    {
      "email": [email]
    }
  */
  @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
  public String forgotPassword(@RequestBody String payload){
    JSONObject jsonPayload = new JSONObject(payload);
    //reset password in auth and in database.

  String email = jsonPayload.getString("email");
  String displayName = "";
  try{
    displayName = FirebaseAuth.getInstance()
    .getUserByEmail(jsonPayload.getString("email")).getDisplayName(); 
  }
  catch(FirebaseAuthException e){
    e.printStackTrace();
    return "FAILURE: Can't get Username";
  } 
  try {
    String link = FirebaseAuth.getInstance().generatePasswordResetLink(email);
    // Construct email verification template, embed the link and send
    // using custom SMTP server.
    Email.sendPasswordResetEmail(email, displayName, link);
  } catch (FirebaseAuthException e) {
    System.out.println("Error generating email link: " + e.getMessage());
  }
    return "FAILURE: Error generating email link";
  }
}