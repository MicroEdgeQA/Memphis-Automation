package common;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Properties;

public class Notification
{	
    public static void main(String[] args)
    {
    	sendEmail(args);
    }

    public static void sendEmail(String args[])
    {        		   
    	// Command line arguments
    	String resultsDir = args[0]; 
    	String from       = args[1];
    	String to         = args[2];
    	String host       = args[3];
    	String user       = args[4];
    	String password   = args[5];
    	    	
    	String subject = "Automated Test Results";
        String bodyText = "The automated test execution has completed.  The results are attached to this e-mail.";
        
        String attachmentName = resultsDir + "\\results.zip";
        
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props);

        try
        {
            InternetAddress fromAddress = new InternetAddress(from);
            InternetAddress toAddress = new InternetAddress(to);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);

            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(bodyText);

            FileDataSource fileDataSource = new FileDataSource(attachmentName);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(fileDataSource.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
        } catch (MessagingException e)
          {
        		System.out.println("Problem sending e-mail!");
          }
    }
}