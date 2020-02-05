/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package toolsAndTransversalFunctionnalities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author erman
 */
public class CredentialInformation {
    
    private String propertiesFileLocation ;

    public String getPropertiesFileLocation() {
        return propertiesFileLocation;
    }

    public void setPropertiesFileLocation(String propertiesFileLocation) {
        this.propertiesFileLocation = propertiesFileLocation;
    }
     
    public String getAConfigFile(String fileName) {
        String userPath = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");
        String fileLocation = userPath + fileSeparator + ".." + fileSeparator
                + ".." + fileSeparator + ".." + fileSeparator + ".."
                + fileSeparator + ".." + fileSeparator + "config-files"
                + fileSeparator + fileName;
        
        System.out.println("CredentialInformation: fileLocation for credential Information: " + fileLocation);
        return fileLocation;
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
     * Cette méthode lit un fichier Properties à l'emplacement spécifié
     *
     * @return Le fichier Properties chargé
     * @throws FileNotFoundException si le fichier n'a pas été trouvé
     * @throws IOException si une erreur est survenue durant la lecture
     */
    public Properties loadProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(getAConfigFile("credentialInformation.properties")));
        return props;
    }



}
