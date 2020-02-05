/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mailing.mailContent;

import java.util.Calendar;

/**
 *
 * @author erman
 */
public enum FileMessageContentEnum {
    DefaulMailContent("DefaulMailContent.html"),
    JobCancelTruckOwnerMessageContent("JobCancelTruckOwnerMessageContent.html"),
    AccountCreationMessageContent("AccountCreationMessageContent.html"),
    AddNewJobResponseMessageContent("AddNewJobResponseMessageContent.html"),
    JobCancelExcavatorMessageContent("JobCancelExcavatorMessageContent.html"),
    JobConfirmTruckOwnerMessageContent("JobConfirmTruckOwnerMessageContent.html"),
    JobConfirmationMessageContent("JobConfirmationMessageContent.html"),
    JobRejectionMessageContent("JobRejectionMessageContent.html"),
    NewJobOnTruckMessageContent("NewJobOnTruckMessageContent.html"),
    RetrieveUserCodeMessageContent("RetrieveUserCodeMessageContent.html"),
    SendUserCodeMessageContent("SendUserCodeMessageContent.html"),
    AddNewJobNotificationMessageContent("AddNewJobNotificationMessageContent.html"),
    EditJobMessageContent("EditJobMessageContent.html"),
    FailurePaymentTruckOwner("FailurePaymentTruckOwner.html"),
    FailurePaymentExcavator("FailurePaymentExcavator.html"),
    ;

    private final String name;
    private FileMessageContentEnum(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    /*
    public static void main(String[] args){
        
        Calendar c = Calendar.getInstance();
        System.out.println("time of day: " + c.getTime() + "[ " + c.getTimeInMillis() + " ]\n\n");
        c.add(Calendar.MONTH, -50);
        System.out.println("time next: " + c.getTime() + "[ " + c.getTimeInMillis() + " ]");
        
        System.out.println("FileMessageContentEnum = " + FileMessageContentEnum.AccountCreationMessageContent);
        System.out.println("FileMessageContentEnum = " + FileMessageContentEnum.AccountCreationMessageContent.toString());
        for (FileMessageContentEnum file : FileMessageContentEnum.values()) {
            System.out.println("FileMessageContentEnum = " + file);
        }
    }
    */
}
