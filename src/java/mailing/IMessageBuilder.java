/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing;

/**
 *
 * @author erman
 */
public interface IMessageBuilder {
    public String getMessageContent();// Content or the body of message
    
    public String getMessageTitle();// Subject of time of message
}
