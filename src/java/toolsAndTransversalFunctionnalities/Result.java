/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package toolsAndTransversalFunctionnalities;


import java.util.List;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author erman
 */
public class Result {
    protected List<String> objectList;
    protected String object;
    protected String msg;
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger("dump1");
    
    
    
    public List<String> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<String> objectList) {
        this.objectList = objectList;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
   
    protected void afficher(List<String> list, String callingClassName) {
        for (String obj : list) {
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+callingClassName + "[ Nombre element: " + obj.split(";").length+"\f\n\t Obj: " + obj + " ]");
        }
    }
    
    
    public void afficherResult(String callingClassName) {
        
        
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+callingClassName + "{ Result_mesg:  " + this.msg );

        if (this.object != null) {
            logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+callingClassName + "[ Result_Object:  " + this.object + " ]");
        }
        if ( this.objectList != null && !this.objectList.isEmpty()) {
            this.afficher(this.objectList, callingClassName);
        }
        logger.info("\n"+"Class : "+getClass()+"\n" +"Fct       : "+"---"+"\n" +"info      : "+"}\n");
    }
    


}
