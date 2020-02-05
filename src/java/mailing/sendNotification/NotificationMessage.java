/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailing.sendNotification;

import java.io.File;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import mailing.mailSending.MailFunction;

/**
 *
 * @author erman
 */
@XmlRootElement
public class NotificationMessage {

    private String notificationID;
    private String notificationTitle;
    private Date notificationDate;
    private String messageToSend;
    private int notificationType;// 1-Add a new job, 2-Edit job, 3-Deleted Job
    private String jobLocation;
    private String excavatorName;
    private int jobNotificationID;
    private static boolean isLoaded = false;
/*
    public NotificationMessage(String notificationID, String notificationTitle, Date notificationDate, String messageToSend, int notificationType, String jobLocation, String excavatorName, int jobNotificationID) {
        setNotificationID(notificationID);
        setNotificationTitle(notificationTitle);
        setNotificationDate(notificationDate);
        setMessageToSend(messageToSend);
        setNotificationType(notificationType);
        setJobLocation(jobLocation);
        setExcavatorName(excavatorName);
        setJobNotificationID(jobNotificationID);
    }
*/

    public NotificationMessage(String notificationID, String notificationTitle, Date notificationDate, String messageToSend, int notificationType, String jobLocation, String excavatorName, int jobNotificationID) {
        this.notificationID = notificationID;
        this.notificationTitle = notificationTitle;
        this.notificationDate = notificationDate;
        this.messageToSend = messageToSend;
        this.notificationType = notificationType;
        this.jobLocation = jobLocation;
        this.excavatorName = excavatorName;
        this.jobNotificationID = jobNotificationID;
    }

    public NotificationMessage(String fileName) {
        //if (!isLoaded) {
        System.out.println("[NotificationMessage] fileName: " + fileName);
            String buildLocation = MailFunction.buildLocation(fileName);
            setObj(builNotificationObj(buildLocation));
        //}
    }

    private void setObj(NotificationMessage nMsg) throws NullPointerException {
        if(nMsg == null) throw new NullPointerException();
        this.notificationID = nMsg.getNotificationID();
        this.notificationTitle = nMsg.getNotificationTitle();
        this.notificationDate = nMsg.getNotificationDate();
        this.messageToSend = nMsg.getMessageToSend();
        this.notificationType = nMsg.getNotificationType();
        this.jobLocation = nMsg.getJobLocation();
        this.excavatorName = nMsg.getExcavatorName();
        this.jobNotificationID = nMsg.getJobNotificationID();
    }
    
    public NotificationMessage() {
        
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getExcavatorName() {
        return excavatorName;
    }

    public void setExcavatorName(String excavatorName) {
        this.excavatorName = excavatorName;
    }

    public int getJobNotificationID() {
        return jobNotificationID;
    }

    @XmlElement
    public void setJobNotificationID(int jobNotificationID) {
        this.jobNotificationID = jobNotificationID;
    }

    public int getNotificationType() {
        return notificationType;
    }

    @XmlAttribute
    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationID() {
        return notificationID;
    }

    @XmlElement
    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    @XmlElement
    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    @XmlElement
    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getMessageToSend() {
        return messageToSend;
    }

    @XmlElement
    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" + "notificationID=" + notificationID + ", notificationTitle=" + notificationTitle + ", notificationDate=" + notificationDate + ", messageToSend=" + messageToSend + ", notificationType=" + notificationType + ", jobLocation=" + jobLocation + ", excavatorName=" + excavatorName + ", jobNotificationID=" + jobNotificationID + '}';
    }

    private NotificationMessage builNotificationObj(String fileName) {
        
        System.out.println("[builNotificationObj] fileName: " + fileName);
        NotificationMessage notificationMessage = null;
        isLoaded = false;
        try {

            File file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(NotificationMessage.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            notificationMessage = (NotificationMessage) jaxbUnmarshaller.unmarshal(file);
            System.out.println(notificationMessage);
            
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
        isLoaded = true;
        return notificationMessage;

    }
/*
    public static void main(String[] args) {

        NotificationMessage notificationMessage = new NotificationMessage("50", "New Job", new Date(), 
                "A new job has been created", 5, "Yaoundé", "ExcavatorName", 1);
        
        try {

            File file = new File("/home/camertronix/Bureau/allDepot/file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(NotificationMessage.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(notificationMessage, file);
            jaxbMarshaller.marshal(notificationMessage, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
 
        NotificationMessage customer2 = new NotificationMessage("AddNewJobNotificationMessageContent.xml");
        System.out.println("Print: " + customer2);
        
        try {

            File file = new File("/home/camertronix/Bureau/allDepot/file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(NotificationMessage.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            NotificationMessage customer = (NotificationMessage) jaxbUnmarshaller.unmarshal(file);
            System.out.println(customer);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
*/
}
