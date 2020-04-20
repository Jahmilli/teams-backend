package ses.teamsbackend;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Email {
    
    public static void sendPasswordResetEmail(String email, String name, String link){
      // Sender's email ID needs to be mentioned
      final String from = "ses2bnoreply@gmail.com";
      final String password = "password1234!@#$";

      // Get system properties
      Properties properties = new Properties();
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.port", "587");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");

      // Get the default Session object.
      Session session = Session.getInstance(properties,
        new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from, password);
            }
        }  
      );

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

         // Set Subject: header field
         message.setSubject("Password Change Request: SES2B Chat");

         // Now set the actual message
         message.setText("Hello " + name + 
         ",\n\n We have recently been informed that you have requested a password change.\n" 
         + "If you have not initiated this request, please ignore this message.\n" 
         + "Otherwise, please click on the link below to reset your password.\n\n"
         + link
         + "\n\nSES2B chat team.");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
    }
}