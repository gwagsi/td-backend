/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.sendNotification;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cette permet d'envoyer des notifications aux utilisateurs.
 *
 * @author erman
 */
public class SendNotification {

    private static final String SERVER_KEY = "AIzaSyB9n_Gpcu4YCiuDHhfzcyx4y2yAyqHd4Lo";
    //private static final String SERVER_KEY = "AIzaSyDgMuX35Nx0UfYL_ZyImw-6R9h-BP7JEqQ";
    private final int numberAttemp = 2;
    
    public void sendNotification(List<String> userRegistrationID, NotificationMessage messageToSend) {
        
        //System.out.println("message date = "+messageToSend.getNotificationDate());
        
        Message androidMessage = new Message.Builder()
                .timeToLive(5)
                .addData("id", messageToSend.getNotificationID())
                .addData("title", messageToSend.getNotificationTitle())
                .addData("creationDate", Long.toString(messageToSend.getNotificationDate().getTime()))
                .addData("message", messageToSend.getMessageToSend())
                .addData("excavatorName", messageToSend.getExcavatorName())
                .addData("jobLocation", messageToSend.getJobLocation())                
                .build();

        Sender sender = new Sender(SERVER_KEY); // l'apikey qui a été générée précédemment 

        if (userRegistrationID.isEmpty()) {
           
            Logger.getLogger(SendNotification.class.getName()).log(Level.INFO, null, "ERROR !!! Someone is trying to send notification without registration ID. ");
       
        } else if (userRegistrationID.size() == 1) {
            
            try {
                Result result = sender.send(androidMessage, userRegistrationID.get(0), numberAttemp);
                //Result result = sender.send(androidMessage, userRegistrationID2, numberAttemp);

                System.out.println("Notification sent. [ Result error code name is : " + result.getErrorCodeName() + " ] "
                        + " [ Result message ID is : " + result.getMessageId() + " ] [ Result tostring is : " + result.toString() + " ]");

            } catch (IOException ex) {
                Logger.getLogger("An error occur when sending notification... "
                        + ""+SendNotification.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else if (userRegistrationID.size() < 1000) {

            try {
                MulticastResult results = sender.send(androidMessage, userRegistrationID, numberAttemp);
                
                System.out.println("Notification sent : Result : "+results);

            } catch (IOException ex) {
                Logger.getLogger("An error occur when sending notification... "+
                        SendNotification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
