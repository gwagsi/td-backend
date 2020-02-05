/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package toolsAndTransversalFunctionnalities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author erman
 */

@Stateless
@LocalBean
public class PropertiesFile {
    
    private Properties myProps = new Properties();
    
    public String setProperties(String valeur, String value){
        myProps.setProperty(value, value);
        return "good";
    }


    /**
     * Cette méthode stocke le fichier Properties à l'emplacement spécifié
     * 
     * @param props Le fichier à stocker
     * @param fileLocation L'emplacement où le fichier doit être stocké
     * @param comments Commentaires à insérer en tête du fichier
     * @throws FileNotFoundException
     * @throws IOException si une erreur est survenue lors de l'écriture du fichier
     */
    public void saveProperties(Properties props, String fileLocation, String comments) throws FileNotFoundException, IOException {
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
    public Properties loadProperties(String propertiesFileLocation) throws FileNotFoundException, IOException {
            Properties props = new Properties();
            props.load(new FileInputStream(propertiesFileLocation));
            return props;
    }


    /**
     * 
     * Cette méthode affiche cahque paire [clé,valuer] d'un fichier Properties
     * 
     * @param props Le fichier à afficher
     * @return le contenu du fichier
     */
    public String displayProperties(Properties props) {
        
        String res = "";
        Iterator it = props.keySet().iterator();
        while (it.hasNext()) {
            String propertyName = (String) it.next();
            String propertyValue = props.getProperty(propertyName);
            res += propertyName + "=" + propertyValue+"####";
        }
        return res;
    }

        
}
