/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailTool;

import java.util.HashMap;
import java.util.Iterator;
import mailing.IMessageBuilder;
import mailing.mailSending.MailFunction;

/**
 *
 * @author erman
 */
public class CustomMail implements IMessageBuilder {
    
    // Pour l'entête du mail
    private static StringBuilder header;
    
    //Pour Le footer du mail
    private static StringBuilder footer;
    
    // Pour le corps du mail(Body)
    private StringBuilder body;
    
    // Corps personnalisé
    private StringBuilder customBody;
    
    // Ensemble des valeurs qu'il faut remplacer dans le header
    private static HashMap headerVar;
    
    // Ensemble des variables qu'il faut remplacer dans le footer
    private static HashMap footerVar;
    
    // Ensemble des variables qu'il faut remplacer dans le body
    private HashMap bodyVar;
    
    // FileLocation
    private String bodyfileLocation;
    
    // Pour la lecture des mails dans les fichiers
    private final MailLoader mloader;
    
    public CustomMail(HashMap bodyVar, String bodyfileLocation) {
        this.bodyfileLocation = bodyfileLocation;
        this.bodyVar = bodyVar;
        this.mloader = new MailLoader(bodyfileLocation);
        this.body = mloader.loadMailBody();
        this.customBody = mloader.loadMailBody();
        
        //On recharge le footer et le header que si on avait pas encore chargé.
        //Car les opérations suivantes accèdent aux fichiers par consequent sont couteuses
        //if (header == null || footer == null) {
            
            CustomMail.header = mloader.loadMailHeader();
            CustomMail.footer = mloader.loadMailFooter();

            //A remplir
            
            headerVar = new HashMap();
            headerVar.put("###GWT_URL", MailFunction.getUrl());
            headerVar.put("###LOGO", MailFunction.getUrlLogo());
            headerVar.put("###WEB_VERSION1",  MailFunction.getUrlWeb());
            headerVar.put("###WEB_VERSION2", MailFunction.getUrlWeb());
            headerVar.put("###WEB_VERSION3", MailFunction.getUrlWeb());

            //A remplir
            footerVar = new HashMap();
            footerVar.put("###WEB_VERSION", MailFunction.getUrlWeb());
            
        //}
    }
    
    
    
    public String buildContentMail(HashMap variable, StringBuilder contentMail){
        
        
        Iterator it = variable.keySet().iterator();

        while (it.hasNext()) {

            String propertyName = (String) it.next();
            String propertyValue = String.valueOf(variable.get(propertyName));

            try {

                contentMail.replace(contentMail.indexOf(propertyName), contentMail.indexOf(propertyName) + propertyName.length(), propertyValue );
                
            } catch (Exception e) {
                //System.out.println("Warning: propertyName = " + propertyName +" --- propertyValue =  " + propertyValue + " \t" + e.getMessage());
            }

        }
        
        return contentMail.toString();
    }
    
    
    public void upDateBodyVarContent(HashMap bodyVar){
        this.bodyVar = bodyVar;
        this.customBody = new StringBuilder(body);
        buildContentMail(bodyVar, this.customBody);
    }
    
    
    public String buildMail(){
        String sHeader = buildContentMail(headerVar, CustomMail.header);
        String sFooter = buildContentMail(footerVar, CustomMail.footer);
        String sBody = buildContentMail(bodyVar, this.customBody);
        
        return sHeader + sBody + sFooter;
    }
    
    @Override
    public String getMessageContent(){
        return (header.toString() + customBody.toString() + footer.toString());
    }
    
    @Override
    public String getMessageTitle() {
        return "";
    }

    public String getBodyfileLocation() {
        return bodyfileLocation;
    }

    public void setBodyfileLocation(String bodyfileLocation) {
        this.bodyfileLocation = bodyfileLocation;
    }
    
    @Override
    public String toString(){
        return getMessageContent();
    }
            
}
