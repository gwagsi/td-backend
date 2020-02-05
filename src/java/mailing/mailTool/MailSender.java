/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailTool;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mailing.IMessageBuilder;
import mailing.mailSending.MailFunction;

/**
 * Cette permet d'envoyer des mails.
 *
 * @author erman
 */
public class MailSender {
    
    private final String host;
    private final String port;

    String randomNumber = UUID.randomUUID().toString();

    private final String sender;
    private final String senderPwd;

    Properties loadedProps = null;

    public MailSender() {
        loadEmailForSending();
        this.host = (loadedProps.getProperty("host") == null ? "smtp.gmail.com" : loadedProps.getProperty("host"));
        this.port = (loadedProps.getProperty("port") == null ? "465" : loadedProps.getProperty("port"));
        this.sender = loadedProps.getProperty("sender");
        this.senderPwd = loadedProps.getProperty("senderPwd");
    }

    public MailSender(String host, String port, String sender, String senderPwd) {
        this.host = host;
        this.port = port;
        this.sender = sender;
        this.senderPwd = senderPwd;
    }

    public final void loadEmailForSending() {

        try {
            loadedProps = MailFunction.loadProperties(MailFunction.getEmailSettingLocation2());
        } catch (IOException ex) {
            System.out.println("loadEmailForSending: " + ex.getMessage());
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String sendMail(String receiver, IMessageBuilder custMail, String subjet) {

        if (!checkValidEmail(receiver)) {
            System.out.println("[sendMail] Invalid Email  receiver: " + receiver);
            return "InvalidEmail";
        }
        
        System.out.println("Parametre d'envoi de message: receiver = " + receiver + "\n "
                + "------\n  subjet =  " + subjet);
        String res = "envoyé";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.debug", "true"); 
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, senderPwd);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

            message.setSubject(subjet + " (Message Number = " + randomNumber + ")");

            message.setContent(custMail.getMessageContent(), "text/html; charset=ISO-8859-1");
            Transport.send(message);

            System.out.println("sendMail : message envoyé à  " + receiver);
        } catch (MessagingException e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            System.err.println(res + "\n\nsendMail: Message n'a pas pu être Envoyé: " + receiver);
            return res;
        }
        return res;

    }

    public String sendMail(String receiver, IMessageBuilder custMail, String subjet, String ccEmail) {

        if (!checkValidEmail(ccEmail) || !checkValidEmail(receiver)) {
            System.out.println("[sendMail] Invalid Email receiver: " + receiver + " - ccEmail: " + ccEmail);
            return "InvalidEmail";
        }
        
        System.out.println("Parametre d'envoi de message: receiver = " + receiver + "\n "
                + "------\n  subjet =  " + subjet);
        String res = "envoyé";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); 
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, senderPwd);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail));

            message.setSubject(subjet + " (Message Number = " + randomNumber + ")");

            message.setContent(custMail.getMessageContent(), "text/html; charset=ISO-8859-1");
            Transport.send(message);

            System.out.println("sendMail : message envoyé à  " + receiver);
        } catch (MessagingException e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            System.err.println(res + "\n\nsendMail: Message n'a pas pu être Envoyé: " + receiver);
            return res;
        }
        return res;

    }

    public String sendMail(Object[] receivers, IMessageBuilder custMail, String subjet) {

        final ArrayList<String> arrayList = new ArrayList(Arrays.asList(receivers));
        checkValidEmail(arrayList);
        receivers = arrayList.toArray(new String[0]);
        
        String res = " envoyé";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); 
        props.put("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, senderPwd);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));

            InternetAddress[] destinataires = new InternetAddress[receivers.length];
            int i = 0;
            for (Object receiver : receivers) {
                destinataires[i++] = new InternetAddress(String.valueOf(receiver));
            }

            message.setRecipients(Message.RecipientType.TO, destinataires);

            message.setSubject(subjet + " (Message Number = " + randomNumber + ")");

            message.setContent(custMail.getMessageContent(), "text/html; charset=ISO-8859-1");
            Transport.send(message);
            System.out.println("sendMail : message envoyé à  " + Arrays.asList(receivers));

        } catch (MessagingException e) {
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            e.printStackTrace(str);
            res = string.toString();
            System.err.println(res + "\n\nsendMail: Message n'a pas pu être Envoyé: " + Arrays.asList(receivers));
            return res;
        }
        return res;
    }
    
    public String sendMail(List<String> receivers, List<IMessageBuilder> custMailList, List<String> subjets) {
        String res = "";

        int i = 0;
        for (String reciever : receivers) {

            if (!checkValidEmail(reciever)) {
                System.out.println("[sendMail] Invalid Email  receiver: " + reciever);
                continue;
            }

            IMessageBuilder custMail = custMailList.get(i);
            String subject = subjets.get(i++);
            res += sendMail(reciever, custMail, subject);
        }
        return res;

    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX
            //= Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            //= Pattern.compile("^[a-zA-Z0-9_\\-\\.]{3,}@[a-zA-Z0-9\\-_]{2,}\\.[a-zA-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
            = Pattern.compile("([^.@]{3,})(\\.[^.@]+)*@([^.@]+\\.)+([^.@]{2,6}$)", Pattern.CASE_INSENSITIVE);

    private static boolean checkValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    
    private static boolean checkValidEmail(ArrayList emailList) {
        
        //String[] validEmailList = new String[emailList.length];
        boolean isExistEmail = true;
        ArrayList emailList2 = new ArrayList();
        emailList2.addAll(emailList);
        for (Object email : emailList2) {
            
            if (!checkValidEmail((String) email)) {
                System.out.println("Invalid Email: " + email);
                emailList.remove(email);
                isExistEmail &= false;
            }
        }
        
        return isExistEmail;
    }
/*
    public static void main(String args[]) {
        System.out.println("email is valid: " + checkValidEmail("webmaster@müller.de"));
        String[] validEmailList = new String[]{
            "ermannghonda@gmail.com",
            "trucj.here.@g..com",
            "t@g.com",
            "tchindaerman@gmail.com",
        };
        final ArrayList<String> arrayList = new ArrayList(Arrays.asList(validEmailList));
        checkValidEmail(arrayList);
        validEmailList = arrayList.toArray(new String[0]);
        for (String validMail : validEmailList) {
            System.out.println("Valid in Main Email: " + validMail);
        }
    }
*/
}
