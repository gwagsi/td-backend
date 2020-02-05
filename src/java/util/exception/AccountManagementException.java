/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.exception;

/**
 *
 * @author Sorelus
 */
public class AccountManagementException extends Exception{
    
    private static final long serialVersionUID = 1L;
    private String fonction="";
    private int codeError;
    private String smallMessage="";
    private String fullMessage="";
    private String classe="";
    
    public AccountManagementException(String classe,String fonction,int code,String sMess, String message){
      super(message);
      
      this.classe=classe;
      this.fonction=fonction;
      this.codeError=code;
      this.smallMessage=sMess;
      this.fullMessage=message;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public int getCodeError() {
        return codeError;
    }

    public void setCodeError(int codeError) {
        this.codeError = codeError;
    }

    public String getSmallMessage() {
        return smallMessage;
    }

    public void setSmallMessage(String smallMessage) {
        this.smallMessage = smallMessage;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    
}
