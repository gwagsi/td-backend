/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailing.mailSending;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import loadConfiguration.URLLoader;

/**
 *
 * @author erman
 */
public class MailFunction {

    private static String url = null;
    private static String urlWeb = null;

    //private static final String urlActivate = "/activate?welcome=";
    //private static final String urlWebActivate = "/controller?action=accountManagement&method=Register&welcome=";
    public final int maxLengh = 50;

    public static String getUrl() {
        URLLoader.setUrls();
        if (url == null) {
            MailFunction.url = URLLoader.gwtURL;
        }
        return MailFunction.url;
    }

    public static String getUrlWeb() {
        URLLoader.setUrls();
        if (urlWeb == null) {
            urlWeb = URLLoader.frontEndURL;
        }
        return urlWeb;
    }
    
    public static String getUrlAdmin() {
        return URLLoader.administratorURL;
    }
    
    public static String getUrlLogo() {
       return URLLoader.siteLogo;
      
    }

    public static String getUrlActivate() {
        //return MailFunction.getUrl()+urlActivate;
        return URLLoader.gwtActivationURL;
    }

    public static String getUrlWebActivate() {
        //return MailFunction.getUrlWeb()+urlWebActivate;
        return URLLoader.frontEndActivationURL;
    }

    public static String getMailMessageLocation2() {
        return buildLocation("accountCreationMessage.properties");
    }

    public static String getEmailSettingLocation2() {
        return buildLocation("emailAccountSetting.properties");
    }

    public static String buildLocation(String fileName) {
        String file = "dumTruckMessage" + System.getProperty("file.separator") + fileName;
        file = getAConfigFileDir() + file;
        return file;
    }

    public static String getAConfigFileDir() {
        String userPath = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");
        String configFileDir = userPath + fileSeparator + ".." + fileSeparator
                + ".." + fileSeparator + ".." + fileSeparator + ".."
                + fileSeparator + ".." + fileSeparator + "config-files"
                + fileSeparator;
        return configFileDir;
    }

    public static String getTotalName(String name, String firstName) {
        String[] surname = firstName.split("#");
        String totalName;
        if (surname.length <= 1) {
            totalName = name + "  " + surname[0];
        } else {
            totalName = name + "  " + surname[0] + " " + surname[1];
        }
        return totalName;
    }

    public static String getHtmlTotalName(String name, String firstName) {
        String[] surname = firstName.split("#");
        String totalName;
        if (surname.length <= 1) {
            totalName = "<b>" + name + "</b>  <b>" + surname[0] + "</b>";
        } else {
            totalName = "<b>" + name + "</b> <b>" + surname[0] + "</b> <b>" + surname[1] + "</b>";
        }
        return totalName;
    }

    /**
     * Cette méthode stocke le fichier Properties à l'emplacement spécifié
     *
     * @param props Le fichier à stocker
     * @param fileLocation L'emplacement où le fichier doit être stocké
     * @param comments Commentaires à insérer en tête du fichier
     * @throws FileNotFoundException
     * @throws IOException si une erreur est survenue lors de l'écriture du
     * fichier
     */
    public void saveProperties(Properties props, String fileLocation, String comments) throws FileNotFoundException, IOException {
        OutputStream out = new FileOutputStream(fileLocation);
        props.store(out, comments);
        out.flush();
        out.close();
    }

    /**
     * Cette méthode stocke le fichier Properties à l'emplacement spécifié
     *
     * @param texte Le fichier à stocker
     * @param fileLocation L'emplacement où le fichier doit être stocké
     * @param comments Commentaires à insérer en tête du ception
     */
    public void saveProperties(String texte, String fileLocation, String comments) {
        try {

            System.out.println("saveProperties: fileLocation : " + fileLocation);
            //BufferedWriter a besoin d un FileWriter, 
            //les 2 vont ensemble, on donne comme argument le nom du fichier
            //false signifie qu'on n'ajoute pas dans le fichier (append), on ne marque pas par dessus 
            FileWriter fw = new FileWriter(fileLocation, false);

            // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
            BufferedWriter output = new BufferedWriter(fw);

            //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
            output.write("<!-- " + comments + " 1  -->\n" + texte);
            //on peut utiliser plusieurs fois methode write

            output.flush();
            //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter

            output.close();
            //et on le ferme

        } catch (IOException ioe) {
            System.out.println("saveProperties: Erreur : " + ioe.getMessage());
        }
        //on "catch" l exception ici si il y en a une, et on l affiche sur la console 
    }

    /**
     * Cette méthode stocke le fichier Properties à l'emplacement spécifié
     *
     * @param h Le HashMap à stocker
     * @param fileLocation L'emplacement où le fichier doit être stocké
     * @param comments Commentaires à insérer en tête du fichier
     * @throws FileNotFoundException
     * @throws IOException si une erreur est survenue lors de l'écriture du
     * fichier
     */
    public void saveProperties(HashMap h, String fileLocation, String comments) throws FileNotFoundException, IOException {

        Properties props = new Properties();

        Iterator it = h.keySet().iterator();

        while (it.hasNext()) {
            String propertyName = (String) it.next();

            String propertyValue = (String) h.get(propertyName);

            props.setProperty(propertyName, propertyValue);
        }

        OutputStream out = new FileOutputStream(fileLocation);
        props.store(out, comments);
        out.flush();
        out.close();
    }

    /**
     * Cette méthode lit un fichier Properties à l'emplacement spécifié
     *
     * @param propertiesFileLocation L'emplacemnt du fichier
     * @return Le fichier Properties chargé
     * @throws FileNotFoundException si le fichier n'a pas été trouvé
     * @throws IOException si une erreur est survenue durant la lecture
     */
    public static Properties loadProperties(String propertiesFileLocation) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propertiesFileLocation));
        return props;
    }

    /**
     *
     * Cette méthode affiche cahque paire [clé,valuer] d'un fichier Properties
     *
     * @param props Le fichier à afficher
     */
    public void displayProperties(Properties props) {
        Iterator it = props.keySet().iterator();
        while (it.hasNext()) {
            String propertyName = (String) it.next();
            String propertyValue = props.getProperty(propertyName);
            //System.out.println(propertyName + " = " + propertyValue);
        }
    }

    /**
     * *****************************************************************************************************
     * this function takes in parameter a string, and return the same string,
     * where we replaced $#$#$ by ';' and special character by their values
     *
     * @param string the string where we will replace the string "$#$#$" by the
     * character ';'
     * @return String the string were we replaced the character "$#$#$" by the
     * character ';'
     * ****************************************************************************************************
     */
    /*
    public String replaceDotCommaCharReverse(String string) {

        String content = string.replace("$#$#$", ";");
        content = content.replace("&lt;", "<");
        content = content.replace("&gt;", ">");
        content = content.replace("&amp;", "&");
        content = content.replace("&quot;", "\"");
        content = content.replace("&apos;", "\'");

        return content;

    }
*/
    public static String replaceDotCommaCharReverse(String string) {
        String content = string.replace("$#$#$", ";");
        content = content.replace("&lt;", "<");
        content = content.replace("&gt;", ">");
        content = content.replace("&quot;", "\"");
        content = content.replace("&apos;", "\'");
        content = content.replace("&amp;", "&");

        return content;
    }

    public static String replaceDotCommaChar(String string) {
        String content = string.replace(";", "$#$#$");
        content = content.replace("&", "&amp;");
        content = content.replace("<", "&lt;");
        content = content.replace(">", "&gt;");
        content = content.replace("\"", "&quot;");
        content = content.replace("\'", "&apos;");
        return content;
    }

    public String subString(String string, int firstPosition, int lastPostion) {

        if (lastPostion < firstPosition || string.length() < lastPostion) {
            return (string);
        }
        return (string.substring(firstPosition, lastPostion) + "...");

    }
    /*
    public static void main(String[] args) throws Exception {
        String content = "&&&Jesu+ismalade;et,5<;;>6/\"##;=toto";
        System.out.println(content);
        String s = replaceDotCommaChar(content);
        System.out.println(s);
        s = replaceDotCommaCharReverse(s);
        System.out.println(s);
        String urlEncodeddata=URLEncoder.encode(content,"UTF-8");
        System.out.println(urlEncodeddata);
        String urlDecodeddata=URLDecoder.decode(urlEncodeddata,"UTF-8");
        System.out.println(urlDecodeddata);
    }
    */
}
