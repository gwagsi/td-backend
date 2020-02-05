/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.sendNotification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mailing.mailSending.MailFunction;

/**
 * Cette classe permet de charger le contenu brute des mail dans fichiers
 *
 * @author erman
 */
public class NotificationMessageLoader {
    
    private static String fileLocation;
    
    public NotificationMessageLoader(String fileLocation){
        NotificationMessageLoader.fileLocation = MailFunction.buildLocation(fileLocation);
    }
    
    public static String getFileLocation() {
        return fileLocation;
    }

    public static void setFileLocation(String fileLocation) {
        NotificationMessageLoader.fileLocation = fileLocation;
    }
    
    
    public StringBuilder loadMailBody() {
        StringBuilder bodyMail = null;
        try {
            bodyMail = readFile(fileLocation);
        } catch (IOException ex) {
            Logger.getLogger(NotificationMessageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bodyMail;
    }
    
    public StringBuilder readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb;
        } finally {
            br.close();
        }
    }

}
