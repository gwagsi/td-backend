/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.security;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.DecoderException;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;

/**
 *
 * @author erman
 */
public class CodeDecodeAES {

    static Cipher cipher;
    public static final String method = "AES";
    public static final String DEFAULT_SECRET_KEY = "2aec8d66dc6f6a0f2100903c6da0102c";//128
    public static final String DEFAULT_SECRET_KEY_256 = "34e4665a7eb93fa57e95f39edb7a8ee47be2fed630afede1fc2209faa59b7ef2";//256
    public static final String ACCOUNT_HIST_KEY = "36efe0e5d45af9fc2b117bc3fe37a267";//128
    public static final String ACCOUNT_HIST_KEY_256 = "c408cbcb8f4a9a93bcde3afa833091f50d7b951c8606962efeb1ab83f574303d";//256

/* 
    public static void main(String[] args) throws Exception {
        // génération d'une clé AES de 128bits
        SecretKey secretKey = generateKey();

        String randomNumber = UUID.randomUUID().toString();
        // Print secret key
        char[] hex = encodeHex(secretKey.getEncoded());
        String secretKeyStr = convertSecretKeyToString(secretKey);
        System.out.println("Secret Key: " + secretKeyStr + " [" + secretKeyStr.length() + "] [" + randomNumber + "]");

		// le clair
        //String plainText = "Chaine de caractère à crippter";
        //String plainText = "4";
        int plainText = 1520;
        System.out.println("Plain Text Before Encryption: " + plainText);

        // le chiffré
        String encryptedText = encryptAccountHistory(plainText);
        System.out.println("Encrypted Text After Encryption: " + encryptedText + " [" + encryptedText.length() + "]");

        // de nouveau le clair
        String decryptedText = decryptAccountHistory(encryptedText);
        System.out.println("Decrypted Text After Decryption: " + decryptedText);
        
    }
       
*/
    public static String decrypt(String encryptedText) {
        String decryptedText = decrypt(encryptedText, DEFAULT_SECRET_KEY);
        return decryptedText;
    }
    
    public static String decryptAccountHistory(String encryptedText) {
        String decryptedText = decrypt(encryptedText, ACCOUNT_HIST_KEY);
        return decryptedText.split(";")[0];
        //return decryptedText;
    }

    private static void instantiateCipher() throws Exception {
        // instanciation d'un objet chiffreur implémentant le chiffrement AES
        cipher = Cipher.getInstance(method);
    }
    
    public static String decrypt(String encryptedText, String secretKey) {
        
        //MailFunction.replaceDotCommaCharReverse(encryptedText);
        String decryptedText = null;
        //System.out.println("encryptedText: " + encryptedText);
        try {
            
            encryptedText = URLDecoder.decode(encryptedText,"UTF-8");
            //System.out.println("encryptedTextDecoded: " + encryptedText);
            
            instantiateCipher();
            // chargement du chiffré dans un tableau d'octets
            byte[] encryptedTextByte = DatatypeConverter.parseBase64Binary(encryptedText);
            
            // initialisation du chiffreur en mode déchiffrement avec clé secrete
            SecretKey sKey = convertSecretKeyToString(secretKey);
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            
            // déchiffrement du tableau d'octets
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            
            // chaine de caractères représentant le clair
            decryptedText = new String(decryptedByte);
            //System.out.println("decryptedText: " + decryptedText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CodeDecodeAES.class.getName()).log(Level.SEVERE, null, ex);
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            ex.printStackTrace(str);
            String res = string.toString();
            System.out.println("res: " + res);
        } catch (Exception ex) {
            Logger.getLogger(CodeDecodeAES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decryptedText;
    }
    
    public static String encrypt(String plainText) {
        
        // chaine de caractères représentant le chiffré 
        String encryptedText = encrypt(plainText, DEFAULT_SECRET_KEY);

        return encryptedText;
    }
    
    public static String encryptAccountHistory(int plainText) {
        String encryptAccHist = String.valueOf(plainText);
        encryptAccHist += ";" + UUID.randomUUID().toString();
        // chaine de caractères représentant le chiffré 
        String encryptedText = encrypt(encryptAccHist, ACCOUNT_HIST_KEY);

        return encryptedText;
    }

    public static String encrypt(String plainText, String secretKey) {
        
        String encryptedText;
        String urlEncodeddata = null;
        //System.out.println("plainText: " + plainText);
        try {
            instantiateCipher();
            // chargement du clair dans un tableau d'octets
            byte[] plainTextByte = plainText.getBytes("UTF-8");
            
            // initialisation du chiffreur en mode chiffrement avec clé secrete
            SecretKey sKey = convertSecretKeyToString(secretKey);
            cipher.init(Cipher.ENCRYPT_MODE, sKey);
            
            // chiffrement du tableau d'octets
            byte[] encryptedByte;
            encryptedByte = cipher.doFinal(plainTextByte);
            
            // chaine de caractères représentant le chiffré
            encryptedText = DatatypeConverter.printBase64Binary(encryptedByte);
            
            urlEncodeddata = URLEncoder.encode(encryptedText,"UTF-8");
            
            //System.out.println("encryptedText: " + encryptedText);
            //System.out.println("urlEncodeddata: " + urlEncodeddata);
            
        } catch (UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CodeDecodeAES.class.getName()).log(Level.SEVERE, null, ex);
            StringWriter string = new StringWriter();
            PrintWriter str = new PrintWriter(string);
            ex.printStackTrace(str);
            String res = string.toString();
            System.out.println("res: " + res);
        } catch (Exception ex) {
            Logger.getLogger(CodeDecodeAES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return urlEncodeddata;
        //MailFunction.replaceDotCommaChar(encryptedText);
        //return encryptedText;
    }
    
    public static String convertSecretKeyToString(SecretKey key) {
        char[] hex = encodeHex(key.getEncoded());
        return String.valueOf(hex);
    }
    
    public static SecretKey convertSecretKeyToString(String secretKeyStr) {
        
        byte[] encoded;
        try {
            encoded = decodeHex(secretKeyStr.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
        return new SecretKeySpec(encoded, method);
    }
    
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(method);
        keyGenerator.init(128); // 128 default; 192 and 256 also possible
        return keyGenerator.generateKey();
    }


}
