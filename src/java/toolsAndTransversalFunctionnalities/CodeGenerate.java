/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package toolsAndTransversalFunctionnalities;

import java.util.List;

/**
 *
 * @author erman
 */
public class CodeGenerate {
    
    private static final int PIN_SIZE = 4;
   
    private static int generateNumberBetweenAB(int borneInf, int borneSup){
        
        int d = borneSup - borneInf;
        
        return(int) (d*Math.random() + borneInf);
    }
    
    public static String generatePassword(){
        
        int nbreCaractere = generateNumberBetweenAB(6, 8);
        String generatePassword = "";
        String allCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        for (int i = 0; i < nbreCaractere; i++) {
            int indiceCaractere = (int) Math.abs(Math.floor(Math.random() * allCharacters.length()-1));
            //int indiceCaractere = (int) Math.floor(Math.random() * allCharacters.length());

            //System.out.println("generateCode: indiceCaractere = " + indiceCaractere);
            generatePassword += allCharacters.charAt(indiceCaractere);
        }

        return generatePassword;
    }

    public static String generateCode(int identifiant){
        return generateCode(PIN_SIZE, identifiant);
    }
    
    private static String generateCode(int nbCaractere, int identifiant){
        
        int nbreCaractere = nbCaractere;
        String code = "";
        String allCharacters = "abtuvwxyzABCDEFGNcdefghijklmnopqrsOPQRSTUVWXYZHIJKLM1234567890";
        
        for(int i = 0; i < nbreCaractere; i++)
        {
           int indiceCaractere = (int) Math.abs(Math.floor(Math.random() * allCharacters.length() -1));
           
           code += allCharacters.charAt(indiceCaractere);
        }
        
        return code;
    }
    
    
    public static String generateCode(int id, List<String> codeList) {
        String code;
        do{
            code = generateCode(PIN_SIZE, id);
            System.out.println("generateCode: code = " + code);
        }while (codeList.contains(code));
        return code;
    }
    /*
    public static void main(String args[]){
      double d = -100.675;
      double d1 = 100.675;
      float f = 90;    
      float f1 = 90;    
      String s = "alinje suis malalde et j'aime bien les ships car c'est vramimentcool. Merci mon aimiblaise" + new Date().getTime();
      //System.out.println("HashCode1:" + hashCode(s));
      System.out.println("HashCode2:" + "random".hashCode());
      System.out.println("HashCode3:" + hashCode("random"));
      /*System.out.println(generatePassword() + " Math.floor(d)   " + Math.floor(d));
      System.out.println(generatePassword() + " Math.floor(f)   " + Math.floor(f)); 
      System.out.println(generatePassword() + " Math.floor(d1)   " + Math.floor(d1));
      System.out.println(generatePassword() + " Math.floor(f1)   " + Math.floor(f1)); 
      System.out.println(generatePassword() + " Math.ceil(d)   " + Math.ceil(d));
      System.out.println(generatePassword() + " Math.ceil(f)   " + Math.ceil(f));
      System.out.println(generatePassword() + " Math.ceil(d1)   " + Math.ceil(d1));
      System.out.println(generatePassword() + " Math.ceil(f1)   " + Math.ceil(f1));
   }
    */
}
