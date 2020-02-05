/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailTool;

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
public class MailLoader {
    
    private static String fileLocation;
    private static String fileHeader;
    private static String fileFooter;
    
    public MailLoader(String fileLocation){
        this(fileLocation, "header.html", "footer.html");
    }
    
    public MailLoader(String fileLocation, String headerLocation, String footerLocation){
        MailLoader.fileLocation = MailFunction.buildLocation(fileLocation);
        MailLoader.fileHeader = MailFunction.buildLocation(headerLocation);
        MailLoader.fileFooter = MailFunction.buildLocation(footerLocation);
    }

    public static String getFileLocation() {
        return fileLocation;
    }

    public static void setFileLocation(String fileLocation) {
        MailLoader.fileLocation = fileLocation;
    }
    
    
    public StringBuilder loadMailBody() {
        StringBuilder bodyMail = null;
        try {
            bodyMail = readFile(fileLocation);
        } catch (IOException ex) {
            Logger.getLogger(MailLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bodyMail;
    }
    
    public StringBuilder loadMailHeader() {
        StringBuilder headerMail = null;
        try {
            headerMail = readFile(fileHeader);
        } catch (IOException ex) {
            Logger.getLogger(MailLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return headerMail;
    }
    
    public StringBuilder loadMailFooter() {
        StringBuilder footerMail = null;
        try {
            footerMail = readFile(fileFooter);
        } catch (IOException ex) {
            Logger.getLogger(MailLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return footerMail;
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
