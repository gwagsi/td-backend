/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.sendNotification;

import java.util.List;

/**
 * Cette permet d'envoyer des notifications aux utilisateurs.
 *
 * @author erman
 */
public class SendNotificationNewJob implements Runnable{
    
    private List<String> userRegistrationID;
    private NotificationMessage messageToSend;

    public SendNotificationNewJob(List<String> userRegistrationID, NotificationMessage messageToSend) {
        this.userRegistrationID = userRegistrationID;
        this.messageToSend = messageToSend;
    }
    
    @Override
    public void run() {
        new SendNotification().sendNotification(userRegistrationID, messageToSend);
    }
    
    public List<String> getUserRegistrationID() {
        return userRegistrationID;
    }

    public void setUserRegistrationID(List<String> userRegistrationID) {
        this.userRegistrationID = userRegistrationID;
    }

    public NotificationMessage getMessageToSend() {
        return messageToSend;
    }

    public void setMessageToSend(NotificationMessage messageToSend) {
        this.messageToSend = messageToSend;
    }
    
    
}
