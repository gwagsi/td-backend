/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.metier.function;

import entities.LenghtOfBed;
import entities.Truck;
import entities.TruckAxle;
import entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erman
 */
public class FormatDataOfSearchTruckFilter {
    
    
    
    public static String getHTMLDataOfAvailableTruck(List<Object[]> informationList, String attribut_session, String res) {
        
        TruckFunction tf = new TruckFunction();
        String informations;
        String picture;
        String dot;
        int i = 0;
        for (Object[] info : informationList) {
            
            Truck truck = (Truck) info[0];
            float distance_within = NumberProcessing.round(String.valueOf(info[1]), 2);
            
            String userName = truck.getUserID().getSurname();
            TruckAxle truckaxleID = truck.getTruckaxleID();
            LenghtOfBed lenghtofbedID = truck.getLenghtofbedID();
            /*
            System.out.println("getHTMLDataOfAvailableTruck: truck : " + truck + "\n"
                    + "TruckOwner: " + truck.getUserID().getName() + "  " + (userName == null ? "" : userName).replace("#", "  ")+ "\n"
                    + "Latitude: " + truck.getLatitude() + "\n"
                    + "Longitude: " + truck.getLongitude() + "\n"
                    + "distance_within : " + distance_within + " Miles " + "\n"
            );
            */
            int pictureTrucksID = (truck.getPicture() == null ? -1 : truck.getPicture().getDocumentID());
            String pictureTrucksPATH = (truck.getPicture() == null ? "" : truck.getPicture().getPathName());
            
            int pictureInsurancesID = (truck.getPictureInsurance() == null ? -1 : truck.getPictureInsurance().getDocumentID());
            String pictureInsurancesPATH = (truck.getPictureInsurance() == null ? "" : truck.getPictureInsurance().getPathName());
            
            String description = "";
            
            if (truck.getTruckDescription().equals("") || truck.getTruckDescription() == null) {
                description = "";
            } else {
                description = truck.getTruckDescription();
            }
            
            informations = "Year : " + truck.getYear() + ", "
                    + "Matricule : " + truck.getTruckNumber() + ", "
                    + "Hiring price : $" + truck.getLocationPrice() + ", "
                    + "Axle : " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName());
            
            if (pictureTrucksPATH.equals("")) {
                picture = "<img  src='./images/noImage.jpg' width='200' hight='200' title='" + informations + "' alt='#' > ";
            } else {
                picture = "<img  src='./../" + pictureTrucksPATH + "' width='200' hight='200' title='" + informations + "' alt='#' > ";
            }
            
            if (truck.getDOTNumber() == null || truck.getDOTNumber().equals("")) {
                dot = "DOT Number: N/A";
            } else {
                dot = "DOT Number: Yes";
            }
            
            String donnees = "\"<td><table id='ligneTruck" + truck.getTruckID() + "'><tr><td width='20%' rowspan='2'><a href='./controller?"
                    + "action=truckManagement&amp;method=detail&amp;attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "&amp;'>"
                    + picture
                    + " </a></td><td valign='top'><a href='./controller?action=truckManagement&amp;method=detail&amp;"
                    + "attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "' title='Truck Number | Truck Description'>"
                    + "<h3>" + truck.getTruckNumber() + " | " + tf.replaceDotCommaCharReverse(description) + "</a></h3>" + dot + " &nbsp;<img src='images/point.png'"
                    + " width='1.2%'> &nbsp; General liability: $" + truck.getGeneralLiability() + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp; Insurance Liability: $" + truck.getInsuranceLiability() + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;"
                    + "Length of bed: " + (lenghtofbedID == null ? "" : lenghtofbedID.getName()) + " <br>Axle: " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp;Direct Deposit: No &nbsp;<img src='images/point.png' width='1.2%'> &nbsp;Online Time Sheet: No "
                    + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;<br>Automated Booking: No &nbsp;"
                    + "<img src='images/point.png' width='1.2%'> &nbsp;Distance: " + distance_within + " Miles</td></tr>"
                    + "<tr><td><table>"
                    + "<tr><td><div id='rating" + i + "' data-average='0'></div></td>"
                    + "<td align='center' style='width: 30px;' title='Number of rate for"
                    + " this truck'> 0 </td>"
                    + "<td> <input type='button' id='operator' name='book' value='Employee Operated'>"
                    + " </td><td align='right'> <input type='button' id='yellow' name='truck " + truck.getTruckID() + "' value='Book truck' "
                    + "title='Here you can book the current truck'></td>"
                    + "<td align='right'> <input type='button' "
                    + "id='question' name='question " + truck.getTruckID() + "' value='Ask Question' title='You can ask a question to the truck owner'>"
                    + "</td> </tr></table></td></tr><tr><td colspan='2' id='gris'><div id='datesAvailables'>"
                    + "</div>  </td></tr></table></td>\"";
            
            if (i < informationList.size() - 1) {
                res += "[" + donnees + "], \n";
            } else {
                res += "[" + donnees + "]";
                
            }

            i++;
        }
        return res;
    }
    
    public static String getHTMLDataOfAvailableTruckWithoutDistance(List<Object[]> informationList, String attribut_session, String res) {
        
        TruckFunction tf = new TruckFunction();
        String informations;
        String picture;
        String dot;
        int i = 0;
        for (Object[] info : informationList) {
            
            Truck truck = (Truck) info[0];
            float distance_within = NumberProcessing.round(String.valueOf(info[1]), 2);
            String surname = truck.getUserID().getSurname();
            TruckAxle truckaxleID = truck.getTruckaxleID();
            LenghtOfBed lenghtofbedID = truck.getLenghtofbedID();
            /*
            System.out.println("_\ngetHTMLDataOfAvailableTruck: truck : " + truck + "\n"
                    + "TruckOwner: " + truck.getUserID().getName() + "  " + (surname == null ? "" : surname).replace("#", "  ")+ "\n"
                    + "Latitude: " + truck.getLatitude() + "\n"
                    + "Longitude: " + truck.getLongitude() + "\n"
                    + "distance_within : " + distance_within + " Miles " + "\n"
            );
            */
            int pictureTrucksID = (truck.getPicture() == null ? -1 : truck.getPicture().getDocumentID());
            String pictureTrucksPATH = (truck.getPicture() == null ? "" : truck.getPicture().getPathName());
            
            int pictureInsurancesID = (truck.getPictureInsurance() == null ? -1 : truck.getPictureInsurance().getDocumentID());
            String pictureInsurancesPATH = (truck.getPictureInsurance() == null ? "" : truck.getPictureInsurance().getPathName());
            
            String description = "";
            
            if (truck.getTruckDescription().equals("") || truck.getTruckDescription() == null) {
                description = "";
            } else {
                description = truck.getTruckDescription();
            }
            
            informations = "Year : " + truck.getYear() + ", "
                    + "Matricule : " + truck.getTruckNumber() + ", "
                    + "Hiring price : $" + truck.getLocationPrice() + ", "
                    + "Axle : " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName());
            
            if (pictureTrucksPATH.equals("")) {
                picture = "<img  src='./images/noImage.jpg' width='200' hight='200' title='" + informations + "' alt='#' > ";
            } else {
                picture = "<img  src='./../" + pictureTrucksPATH + "' width='200' hight='200' title='" + informations + "' alt='#' > ";
            }
            
            if (truck.getDOTNumber() == null || truck.getDOTNumber().equals("")) {
                dot = "DOT Number: N/A";
            } else {
                dot = "DOT Number: Yes";
            }
            
            String donnees = "\"<td><table id='ligneTruck" + truck.getTruckID() + "'><tr><td width='20%' rowspan='2'><a href='./controller?"
                    + "action=truckManagement&amp;method=detail&amp;attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "&amp;'>"
                    + picture
                    + " </a></td><td valign='top'><a href='./controller?action=truckManagement&amp;method=detail&amp;"
                    + "attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "' title='Truck Number | Truck Description'>"
                    + "<h3>" + truck.getTruckNumber() + " | " + tf.replaceDotCommaCharReverse(description) + "</a></h3>" + dot + " &nbsp;<img src='images/point.png'"
                    + " width='1.2%'> &nbsp; General liability: $" + truck.getGeneralLiability() + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp; Insurance Liability: $" + truck.getInsuranceLiability() + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;"
                    + "Length of bed: " + (lenghtofbedID == null ? "" : lenghtofbedID.getName()) + " <br>Axle: " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp;Direct Deposit: No &nbsp;<img src='images/point.png' width='1.2%'> &nbsp;Online Time Sheet: No "
                    + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;<br>Automated Booking: No &nbsp;"
                    + " </td></tr>"
                    + "<tr><td><table>"
                    + "<tr><td><div id='rating" + i + "' data-average='0'></div></td>"
                    + "<td align='center' style='width: 30px;' title='Number of rate for"
                    + " this truck'> 0 </td>"
                    + "<td> <input type='button' id='operator' name='book' value='Employee Operated'>"
                    + " </td><td align='right'> <input type='button' id='yellow' name='truck " + truck.getTruckID() + "' value='Book truck' "
                    + "title='Here you can book the current truck'></td>"
                    + "<td align='right'> <input type='button' "
                    + "id='question' name='question " + truck.getTruckID() + "' value='Ask Question' title='You can ask a question to the truck owner'>"
                    + "</td> </tr></table></td></tr><tr><td colspan='2' id='gris'><div id='datesAvailables'>"
                    + "</div>  </td></tr></table></td>\"";
            
            if (i < informationList.size() - 1) {
                res += "[" + donnees + "], \n";
            } else {
                res += "[" + donnees + "]";
                
            }

            i++;
        }
        return res;
    }

    public static String getHTMLDataOfAvailableTruckFiltered(List<Object[]> informationList, String attribut_session, String res) {
        
        TruckFunction tf = new TruckFunction();
        String informations;
        String picture;
        String dot;
        int i = 0;
        
        for (Object[] info : informationList) {

            Truck truck = (Truck) info[0];
            float distance_within = NumberProcessing.round(String.valueOf(info[1]), 2);
            String surname = truck.getUserID().getSurname();
            TruckAxle truckaxleID = truck.getTruckaxleID();
            LenghtOfBed lenghtofbedID = truck.getLenghtofbedID();
            /*
            System.out.println("_\ngetHTMLDataOfAvailableTruckFiltered: truck : " + truck + "\n"
                    + "TruckOwner: " + truck.getUserID().getName() + "  " + (surname == null ? "" : surname).replace("#", "  ")+ "\n"
                    + "Latitude: " + truck.getLatitude() + "\n"
                    + "Longitude: " + truck.getLongitude() + "\n"
                    + "distance_within : " + distance_within + " Miles\n"
                    + "truckAxle : " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "\n"
                    + "generalLiability : $" + truck.getGeneralLiability() + "\n"
                    + "insuranceLiability : $" + truck.getInsuranceLiability() + "\n"
            );
            */
            int pictureTrucksID = (truck.getPicture() == null ? -1 : truck.getPicture().getDocumentID());
            String pictureTrucksPATH = (truck.getPicture() == null ? "" : truck.getPicture().getPathName());

            int pictureInsurancesID = (truck.getPictureInsurance() == null ? -1 : truck.getPictureInsurance().getDocumentID());
            String pictureInsurancesPATH = (truck.getPictureInsurance() == null ? "" : truck.getPictureInsurance().getPathName());

            String description = "";

            if (truck.getTruckDescription().equals("") || truck.getTruckDescription() == null) {
                description = "";
            } else {
                description = truck.getTruckDescription();
            }

            informations = "Year : " + truck.getYear() + ", "
                    + "Matricule : " + truck.getTruckNumber() + ", "
                    + "Hiring price : $" + truck.getLocationPrice() + ", "
                    + "Axle : " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName());

            //System.out.println("Informations du truck: "+informations);
            if (pictureTrucksPATH.equals("")) {
                picture = "<img  src='./images/noImage.jpg' width='200' hight='200' title='" + informations + "' alt='#' > ";
                //picture="<img  src='./assets/img/noImage.jpg' title='"+informations+"' alt='#' > ";
            } else {
                //picture="<img  src='./assets/img/content/"+list.get(i).getPicture()+"' title='"+informations+"' alt='#' > ";
                picture = "<img  src='./../" + pictureTrucksPATH + "' width='200' hight='200' title='" + informations + "' alt='#' > ";
            }

            if (truck.getDOTNumber() == null || truck.getDOTNumber().equals("")) {
                dot = "DOT Number: N/A";
            } else {
                dot = "DOT Number: Yes";
            }

            String donnees = "\"<td><table id='ligneTruck" + truck.getTruckID() + "'><tr><td width='20%' rowspan='2'><a href='./controller?"
                    + "action=truckManagement&amp;method=detail&amp;attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "&amp;'>"
                    + picture
                    + " </a></td><td valign='top'><a href='./controller?action=truckManagement&amp;method=detail&amp;"
                    + "attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "' title='Truck Number | Truck Description'>"
                    + "<h3>" + truck.getTruckNumber() + " | " + tf.replaceDotCommaCharReverse(description) + "</a></h3>" + dot + " &nbsp;<img src='images/point.png'"
                    + " width='1.2%'> &nbsp; General liability: $" + truck.getGeneralLiability() + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp; Insurance Liability: $" + truck.getInsuranceLiability() + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;"
                    + "Length of bed: " + (lenghtofbedID == null ? "" : lenghtofbedID.getName()) + " <br>Axle: " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp;Direct Deposit: No &nbsp;<img src='images/point.png' width='1.2%'> &nbsp;Online Time Sheet: No "
                    + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;<br>Automated Booking: No &nbsp;"
                    + "<img src='images/point.png' width='1.2%'> &nbsp;Distance: " + distance_within + " Miles</td></tr>"
                    + "<tr><td><table>"
                    + "<tr><td><div id='rating" + i + "' data-average='0'></div></td>"
                    + "<td align='center' style='width: 30px;' title='Number of rate for"
                    + " this truck'> 0 </td>"
                    + "<td> <input type='button' id='operator' name='book' value='Employee Operated'>"
                    + " </td><td align='right'> <input type='button' id='yellow' name='truck " + truck.getTruckID() + "' value='Book truck' "
                    + "title='Here you can book the current truck'></td>"
                    + "<td align='right'> <input type='button' "
                    + "id='question' name='question " + truck.getTruckID() + "' value='Ask Question' title='You can ask a question to the truck owner'>"
                    + "</td> </tr></table></td></tr><tr><td colspan='2' id='gris'><div id='datesAvailables'>"
                    + "</div>  </td></tr></table></td>\"";

            if (i < informationList.size() - 1) {
                res += "[" + donnees + "], \n";
            } else {
                res += "[" + donnees + "]";

            }

            i++;
        }
        return res;
    }


    public static String getHTMLDataOfAvailableTruckFilteredWithoutDistance(List<Object[]> informationList, String attribut_session, String res) {
        
        TruckFunction tf = new TruckFunction();
        String informations;
        String picture;
        String dot;
        int i = 0;
        
        for (Object[] info : informationList) {

            Truck truck = (Truck) info[0];
            float distance_within = NumberProcessing.round(String.valueOf(info[1]), 2);
            String surname = truck.getUserID().getSurname();
            TruckAxle truckaxleID = truck.getTruckaxleID();
            LenghtOfBed lenghtofbedID = truck.getLenghtofbedID();
            /*
            System.out.println("_\ngetHTMLDataOfAvailableTruckFiltered: truck : " + truck + "\n"
                    + "TruckOwner: " + truck.getUserID().getName() + "  " + (surname == null ? "" : surname).replace("#", "  ")+ "\n"
                    + "Latitude: " + truck.getLatitude() + "\n"
                    + "Longitude: " + truck.getLongitude() + "\n"
                    + "distance_within : " + distance_within + " Miles\n"
                    + "truckAxle : " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "\n"
                    + "generalLiability : $" + truck.getGeneralLiability() + "\n"
                    + "insuranceLiability : $" + truck.getInsuranceLiability() + "\n"
            );
            */
            int pictureTrucksID = (truck.getPicture() == null ? -1 : truck.getPicture().getDocumentID());
            String pictureTrucksPATH = (truck.getPicture() == null ? "" : truck.getPicture().getPathName());

            int pictureInsurancesID = (truck.getPictureInsurance() == null ? -1 : truck.getPictureInsurance().getDocumentID());
            String pictureInsurancesPATH = (truck.getPictureInsurance() == null ? "" : truck.getPictureInsurance().getPathName());

            String description = "";

            if (truck.getTruckDescription().equals("") || truck.getTruckDescription() == null) {
                description = "";
            } else {
                description = truck.getTruckDescription();
            }

            informations = "Year : " + truck.getYear() + ", "
                    + "Matricule : " + truck.getTruckNumber() + ", "
                    + "Hiring price : $" + truck.getLocationPrice() + ", "
                    + "Axle : " +  (truckaxleID == null ? "" : truckaxleID.getTruckAxleName());

            //System.out.println("Informations du truck: "+informations);
            if (pictureTrucksPATH.equals("")) {
                picture = "<img  src='./images/noImage.jpg' width='200' hight='200' title='" + informations + "' alt='#' > ";
                //picture="<img  src='./assets/img/noImage.jpg' title='"+informations+"' alt='#' > ";
            } else {
                //picture="<img  src='./assets/img/content/"+list.get(i).getPicture()+"' title='"+informations+"' alt='#' > ";
                picture = "<img  src='./../" + pictureTrucksPATH + "' width='200' hight='200' title='" + informations + "' alt='#' > ";
            }

            if (truck.getDOTNumber() == null || truck.getDOTNumber().equals("")) {
                dot = "DOT Number: N/A";
            } else {
                dot = "DOT Number: Yes";
            }

            String donnees = "\"<td><table id='ligneTruck" + truck.getTruckID() + "'><tr><td width='20%' rowspan='2'><a href='./controller?"
                    + "action=truckManagement&amp;method=detail&amp;attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "&amp;'>"
                    + picture
                    + " </a></td><td valign='top'><a href='./controller?action=truckManagement&amp;method=detail&amp;"
                    + "attribute=" + attribut_session + "&amp;d=" + truck.getTruckID() + "' title='Truck Number | Truck Description'>"
                    + "<h3>" + truck.getTruckNumber() + " | " + tf.replaceDotCommaCharReverse(description) + "</a></h3>" + dot + " &nbsp;<img src='images/point.png'"
                    + " width='1.2%'> &nbsp; General liability: $" + truck.getGeneralLiability() + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp; Insurance Liability: $" + truck.getInsuranceLiability() + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;"
                    + "Length of bed: " +  (lenghtofbedID == null ? "" : lenghtofbedID.getName()) + " <br>Axle: " +  (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "&nbsp;<img src='images/point.png' width='1.2%'>"
                    + " &nbsp;Direct Deposit: No &nbsp;<img src='images/point.png' width='1.2%'> &nbsp;Online Time Sheet: No "
                    + "&nbsp;<img src='images/point.png' width='1.2%'> &nbsp;<br>Automated Booking: No &nbsp;"
                    + "</td></tr>"
                    + "<tr><td><table>"
                    + "<tr><td><div id='rating" + i + "' data-average='0'></div></td>"
                    + "<td align='center' style='width: 30px;' title='Number of rate for"
                    + " this truck'> 0 </td>"
                    + "<td> <input type='button' id='operator' name='book' value='Employee Operated'>"
                    + " </td><td align='right'> <input type='button' id='yellow' name='truck " + truck.getTruckID() + "' value='Book truck' "
                    + "title='Here you can book the current truck'></td>"
                    + "<td align='right'> <input type='button' "
                    + "id='question' name='question " + truck.getTruckID() + "' value='Ask Question' title='You can ask a question to the truck owner'>"
                    + "</td> </tr></table></td></tr><tr><td colspan='2' id='gris'><div id='datesAvailables'>"
                    + "</div>  </td></tr></table></td>\"";

            if (i < informationList.size() - 1) {
                res += "[" + donnees + "], \n";
            } else {
                res += "[" + donnees + "]";

            }

            i++;
        }
        return res;
    }

    public static List<String> getOfAvailableTruckDataSList(List<Object[]> informationList) {

        List<String> listResult = new ArrayList<>();

        for (Object[] info : informationList) {

            Truck truck = (Truck) info[0];
            float distance_within = NumberProcessing.round(String.valueOf(info[1]), 2);
            User userOwner = truck.getUserID();
            String surname = (userOwner == null ? "" : userOwner.getSurname());
            TruckAxle truckaxleID = truck.getTruckaxleID();
            LenghtOfBed lenghtofbedID = truck.getLenghtofbedID();
            String telephone = (userOwner == null ? "" : userOwner.getTelephone());
            /*
            System.out.println("getOfAvailableTruckDataSList: truck : " + truck + "\n"
                    + "TruckOwner: " + (userOwner == null ? "" : userOwner.getName()) + "  " + (surname == null ? "" : surname).replace("#", "  ") + "\n"
                    + "Latitude: " + truck.getLatitude() + "\n"
                    + "Longitude: " + truck.getLongitude() + "\n"
                    + "distance_within : " + distance_within + " Miles\n"
                    + "truckAxle : " + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + "\n"
                    + "generalLiability : $" + truck.getGeneralLiability() + "\n"
                    + "insuranceLiability : $" + truck.getInsuranceLiability() + "\n"
            );
            */
            int pictureTrucksID = (truck.getPicture() == null ? -1 : truck.getPicture().getDocumentID());
            String pictureTrucksPATH = (truck.getPicture() == null ? "" : truck.getPicture().getPathName());

            int pictureInsurancesID = (truck.getPictureInsurance() == null ? -1 : truck.getPictureInsurance().getDocumentID());
            String pictureInsurancesPATH = (truck.getPictureInsurance() == null ? "" : truck.getPictureInsurance().getPathName());
            
            String res = truck.getTruckID() + ";" //00
                    + truck.getDistance() + ";" //01
                    + truck.getTruckNumber() + ";" //02
                    + truck.getPhoneNumber() + ";" //03
                    + pictureTrucksPATH + ";" //04
                    + truck.getLocationPrice() + ";" //05
                    + truck.getGeneralLiability() + ";" //06
                    + truck.getDOTNumber() + ";"
                    + truck.getYear() + ";"
                    + (lenghtofbedID == null ? "" : lenghtofbedID.getLenghtofbedID()) + ";"
                    + (truckaxleID == null ? "" : truckaxleID.getTruckaxleID()) + ";" //10
                    + (userOwner == null ? "" : userOwner.getName()) + ";"
                    + (surname == null ? "" : surname) + ";"
                    + (userOwner == null ? "" : userOwner.getAccountID().getEmail()) + ";"
                    + (telephone == null ? "" : telephone) + ";" //14
                    + truck.getDistance() + ";" //15
                    + truck.getRate() + ";"
                    + truck.getTruckZipCode() + ";"
                    + truck.getTruckNumber() + ";"
                    + (userOwner == null ? "" : userOwner.getAddress()) + ";"
                    + truck.getInsuranceLiability() + ";" //20
                    + (truckaxleID == null ? "" : truckaxleID.getSortField()) + ";"
                    + pictureTrucksID + ";"
                    + pictureInsurancesPATH + ";"
                    + pictureInsurancesID + ";"
                    + truck.getTruckDescription() + ";" //25
                    + truck.getNumberOfRate() + ";"
                    + (truckaxleID == null ? "" : truckaxleID.getTruckAxleName()) + ";"
                    + (lenghtofbedID == null ? "" : lenghtofbedID.getName()) + ";"
                    + distance_within + ";" //29
                    + "null";
            
            listResult.add(res);
        }
        return listResult;
    }

}
