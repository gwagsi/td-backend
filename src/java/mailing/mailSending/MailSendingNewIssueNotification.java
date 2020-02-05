/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailing.mailSending;

import java.util.HashMap;
import mailing.mailContent.FileMessageContent;
import mailing.mailTool.CustomMail;
import mailing.mailTool.MailSender;

/**
 *
 * @author erman
 */
public class MailSendingNewIssueNotification {

    private final String[] issueInfo;
    private final String[] developperEmail;
    
    public MailSendingNewIssueNotification(String[] excavatorInfo, String[] jobResponseInfos) throws NullPointerException {
        this.issueInfo = excavatorInfo;
        this.developperEmail = jobResponseInfos;
    }

    public String sendMailNewJobNotification() {

        String res = "envoyé";
//                issue.getAccountID().getEmail(),
//                issue.getAccountID().getUser().getName(),
//                issue.getAccountID().getUser().getSurname(),
//                issue.getCategory(),
//                issue.getSubcategory(),
//                DateFunction.formatDateToStringYMMMD(issue.getCreationDate()),
//                String.valueOf(issue.getStatut()),
//                issue.getDescription()
        String issueSenderEmail = issueInfo[0];
        String userTotalName = MailFunction.getTotalName(issueInfo[1], issueInfo[2]);
        String issueCategory = issueInfo[3];
        String issueSubcategory = issueInfo[4];
        String issueCreationDate = issueInfo[5];
        String issueStatus = issueInfo[6];
        String issueDescription = issueInfo[7];
        
        String gwtUrl = MailFunction.getUrlWeb();
        String subject = "Trucksanddirt: An Issue Added On TrucksAndDirt.com";
        
        CustomMail custMail = getNewIssueMailNotificationMessage(issueSenderEmail, userTotalName, issueCategory, issueSubcategory, issueCreationDate, gwtUrl, issueDescription);
        
        MailSender mailSender = new MailSender();
        String sendState = mailSender.sendMail(developperEmail, custMail, subject);
        
        return res;
    }

    /**
     *
     * @param issueSenderEmail
     * @param userTotalName
     * @param issueCategory
     * @param issueSubcategory
     * @param issueCreationDate
     * @param gwtUrl
     * @param issueDescription
     * @return
     */
    public CustomMail getNewIssueMailNotificationMessage(String issueSenderEmail, String userTotalName, String issueCategory, String issueSubcategory, String issueCreationDate, String gwtUrl, String issueDescription) {
        
         HashMap hashValues = new HashMap();
        
         hashValues.put("###ISSUESENDEREMAIL", issueSenderEmail);
         hashValues.put("###TOTALNAME", userTotalName);
         hashValues.put("###ISSUE_CATEGORY", issueCategory);
         hashValues.put("###ISSUE_CREATE_DATE", issueCreationDate);
         hashValues.put("###GWT_URL", gwtUrl);
         hashValues.put("###DESCRIPTION", issueDescription);
         hashValues.put("###ISSUE_SUBCATEGORY", issueSubcategory);
         
        CustomMail ctMail = new CustomMail(hashValues, FileMessageContent.MAIL_AddNewIssueNotificationMessageContent);
        ctMail.buildMail();
        return ctMail;

    }

}
