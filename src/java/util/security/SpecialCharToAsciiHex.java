package easencrypdecryp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bsiani
 */
public class SpecialCharToAsciiHex {
  private static final int sizeOfIntInHalfBytes = 2;
  private static final int numberOfBitsInAHalfByte = 4;
  private static final int halfByte = 0x0F;
  private static final char[] hexDigits = { 
    '0', '1', '2', '3', '4', '5', '6', '7', 
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

  public static String decToHex(int dec) {
    StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
    hexBuilder.setLength(sizeOfIntInHalfBytes);
    for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
    {
      int j = dec & halfByte;
      hexBuilder.setCharAt(i, hexDigits[j]);
      dec >>= numberOfBitsInAHalfByte;
    }
    return hexBuilder.toString(); 
  }
  
  public static  String convertSpetialChar(char char1){
       String temp = char1+"" ;
      if(char1 != '%'){
           int codeAscii = (int)char1 ; 
        temp= "%"+decToHex(codeAscii);
        
      }
      return temp;
       
}
  
}
