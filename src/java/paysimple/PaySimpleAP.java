package paysimple;

import com.usaepay.api.jaxws.AddCustomerRequest;
import java.util.Date;
import com.usaepay.api.jaxws.AddCustomerResponse;
import com.usaepay.api.jaxws.Address;
import com.usaepay.api.jaxws.CheckData;
import com.usaepay.api.jaxws.CreditCardData;
import com.usaepay.api.jaxws.CustomerObject;
import com.usaepay.api.jaxws.GeneralFault_Exception;
import com.usaepay.api.jaxws.PaymentMethod;
import com.usaepay.api.jaxws.PaymentMethodArray;
import com.usaepay.api.jaxws.Receipt;
import com.usaepay.api.jaxws.ReceiptArray;
import com.usaepay.api.jaxws.TransactionDetail;
import com.usaepay.api.jaxws.TransactionRequestObject;
import com.usaepay.api.jaxws.TransactionResponse;
import com.usaepay.api.jaxws.UeSecurityToken;
import com.usaepay.api.jaxws.UeSoapServerPortType;
import com.usaepay.api.jaxws.usaepay;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.date.DateFunction;

public class PaySimpleAP {

    /*String endPoint = "sandbox.usaepay.com";
    String sourceKey = "Bmy7457mqTfoOZAH9s51204z53Aa2v1Y";
    String sourcePin = "1234";*/
    String endPoint = "sandbox.usaepay.com";
    String sourceKey = "_aCd8Q1261fRhkDJcjG9o8l771H8m97y";
    String sourcePin = "1234";
    String clientIpAddress = "127.0.0.1";
    String period = "Monthly";
    boolean enablePeriodicPayment = true;
    boolean sendReceipt =true;

    private PaySimpleAP() {
        ;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isSendReceipt() {
        return sendReceipt;
    }

    public void setSendReceipt(boolean sendReceipt) {
        this.sendReceipt = sendReceipt;
    }

    public PaySimpleAP(String endPoint, String sourceKey, String sourcePin,
            String clientIpAddress, String period, boolean enablePeriodicPayment, boolean sendReceipt) {
        super();
        this.endPoint = endPoint;
        this.sourceKey = sourceKey;
        this.sourcePin = sourcePin;
        this.clientIpAddress = clientIpAddress;
        this.period = period;
        this.enablePeriodicPayment = enablePeriodicPayment;
        this.sendReceipt = sendReceipt;
    }

	//public PaySimpleAP() {
    //super();
		/*endPoint="sandbox.usaepay.com";
     sourceKey="Bmy7457mqTfoOZAH9s51204z53Aa2v1Y";
     sourcePin="1234";
     clientIpAddress="127.0.0.1";
     period="Monthly";
     enablePeriodicPayment=true;*/
		// TODO Auto-generated constructor stub
    //}
    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getSourcePin() {
        return sourcePin;
    }

    public void setSourcePin(String sourcePin) {
        this.sourcePin = sourcePin;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isEnablePeriodicPayment() {
        return enablePeriodicPayment;
    }

    public void setEnablePeriodicPayment(boolean enablePeriodicPayment) {
        this.enablePeriodicPayment = enablePeriodicPayment;
    }
	

    /**
     * This methode take as parameter, the address of the customer, and amount
     * to get in a period. The amount can be 0
     *
     * @param firstName the name of customer
     * @param lastName the last name of customer
     * @param company
     * @param street
     * @param city
     * @param state
     * @param zipCode
     * @param country
     * @param email
     * @param fax
     * @param phone
     * @param amount that can be 0 if there is not montly paid.
     * @param drescription
     * @return
     */
    public MypaySimpleObj addNewCustomerToOurPaySimpleAccount(String firstName, String lastName, String company, String street, String city, String state, String zipCode, String country, String email, String fax, String phone, double amount, String startDate,String drescription) {
        // Setup address information
        Address address = new Address();
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setCompany(company);
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZip(zipCode);
        address.setCountry(country);
        address.setEmail(email);
        address.setFax(fax);
        address.setPhone(phone);
        CustomerObject customer = new CustomerObject();
        // Setup address information
        customer.setBillingAddress(address);
        // Set recurring billing options
        customer.setEnabled(this.enablePeriodicPayment);

        customer.setAmount(amount);
        customer.setNext(startDate);
        customer.setNumLeft(new BigInteger("-1"));
        customer.setSchedule(this.period);
        customer.setDescription(drescription);
        MypaySimpleObj usaepayresponse = new MypaySimpleObj();
        usaepayresponse.custnum = "";
        usaepayresponse.paymentmethodid = "";
        usaepayresponse.message = "good";
        try {
	        	// instantiate client connection object,  select www.usaepay.com
            // as the endPoint.  Use sandbox.usaepay.com if connecting to
            // the sandbox server.    
            UeSoapServerPortType client = usaepay.getClient(endPoint);
            // Instantiate security token object (need by all soap methods)
            UeSecurityToken token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );

            // Create request object
            AddCustomerRequest request = new AddCustomerRequest();
            request.setToken(token);

            request.setCustomerData(customer);
            // Create response object
            BigInteger response;
            // Add Customer
            response = client.addCustomer(token,customer);
            //BigInteger custnum = response.getAddCustomerReturn();
            usaepayresponse.custnum = "" + response;

	          //String city=client.getCustomer(token, custnum).getBillingAddress().getCity();       
        } catch (Exception e) {
            System.out.println("addNewCustomerToOurPaySimpleAccount: " + e.getMessage());

            String[] retval1 = e.getMessage().split(":");
            String msg = "";
            if (retval1.length > 1) {
                msg = retval1[1];
            } else {
                msg = retval1[0];
            }
            usaepayresponse.message = "Error: " + msg;
        }

        return usaepayresponse;
    }

    ;
                
                
                /**
		 * This methode take as parameter, the address of the customer, and amount to get in a period.
		 * The amount can be 0
                 * @param custNum 
		 * @param firstName the name of customer
		 * @param lastName the last name of customer
		 * @param company
		 * @param street
		 * @param city
		 * @param state 
		 * @param zipCode
		 * @param country
		 * @param email
		 * @param fax
		 * @param phone
		 * @param amount that can be 0 if there is not montly paid.
		 * @return
		 */
public MypaySimpleObj modifyCustomerToOurPaySimpleAccount(String custNum, String firstName, String lastName, String company, String street, String city, String state, String zipCode, String country, String email, String fax, String phone, double amount, String startDate,String drescription) {
        // Setup address information
        BigInteger custID = new BigInteger(custNum);
        Address address = new Address();
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setCompany(company);
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZip(zipCode);
        address.setCountry(country);
        address.setEmail(email);
        address.setFax(fax);
        address.setPhone(phone);
        CustomerObject customer = new CustomerObject();
	         // Setup address information
	         /*customer.setBillingAddress(address);         
         // Set recurring billing options
         customer.setEnabled(this.enablePeriodicPayment);
	         
         customer.setAmount(amount);
         customer.setNext(startDate);
         customer.setNumLeft(new BigInteger("-1"));
         customer.setSchedule(this.period);
         customer.setDescription("customer for payment transaction");   */
        MypaySimpleObj usaepayresponse = new MypaySimpleObj();
        usaepayresponse.custnum = "";
        usaepayresponse.paymentmethodid = "";
        usaepayresponse.message = "good";
        try {
	        	// instantiate client connection object,  select www.usaepay.com
            // as the endPoint.  Use sandbox.usaepay.com if connecting to
            // the sandbox server.    
            UeSoapServerPortType client = usaepay.getClient(endPoint);
            // Instantiate security token object (need by all soap methods)
            UeSecurityToken token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            customer = new CustomerObject();
            customer.setBillingAddress(address);
            customer.setEnabled(this.enablePeriodicPayment);
            customer.setAmount(amount);
            customer.setNext(startDate);
            customer.setNumLeft(new BigInteger("-1"));
            customer.setSchedule(this.period);
            customer.setDescription(drescription);

            // Create response object
            boolean response;
            // Add Customer
            response = client.updateCustomer(token, custID, customer);
            usaepayresponse.custnum = "" + custNum;

	          //String city=client.getCustomer(token, custnum).getBillingAddress().getCity();       
        } catch (Exception e) {
            System.out.println("modifyCustomerToOurPaySimpleAccount: " + e.getMessage());

            String[] retval1 = e.getMessage().split(":");
            String msg = "";
            if (retval1.length > 1) {
                msg = retval1[1];
            } else {
                msg = retval1[0];
            }
            usaepayresponse.message = "Error: " + msg;
        }

        return usaepayresponse;
    }

		
public MypaySimpleObj addAPaymentMethodeInCreateMode(String custnum, String cardType, String cardNumber, String cardCode, String cardExpiration, String avdStreet, String avsZip, String typePayment, String accountNumber, String routingNumber, boolean makeItAsDefault, boolean activateVerificationOfCreditCard) {

        MypaySimpleObj usaepayresponse = new MypaySimpleObj();
        usaepayresponse.custnum = "";
        usaepayresponse.paymentmethodid = "";
        usaepayresponse.message = "good";

        PaymentMethodArray paymethods = new PaymentMethodArray();
        PaymentMethod paymethod = new PaymentMethod();

        if (typePayment.equals("CreditCard")) {
            // Setup Credit card data information
            /*CreditCardData ccdata = new CreditCardData();
            ccdata.setCardType(cardType);
            ccdata.setCardNumber(cardNumber);
            ccdata.setCardCode(cardCode);
            ccdata.setCardExpiration(cardExpiration);
            ccdata.setAvsStreet(avdStreet);
            ccdata.setAvsZip(avsZip);
            paymethod.setCreditCardData(ccdata);*/
            paymethod.setCardType(cardType);
            paymethod.setCardNumber(cardNumber);
            paymethod.setCardCode(cardCode);
            paymethod.setCardExpiration(cardExpiration);
            paymethod.setAvsStreet(avdStreet);
            paymethod.setAvsZip(avsZip);
        }
        if (typePayment.equals("ACH")) {
            // Setup check data information
            /*CheckData chdata = new CheckData();
            chdata.setAccount(accountNumber);
            chdata.setRouting(routingNumber);*/
            //paymethod.setCheckData(chdata);
            paymethod.setRouting(routingNumber);
            paymethod.setAccount(accountNumber);

        }
        paymethod.setMethodName(typePayment);
        
        UeSoapServerPortType client = null;
        UeSecurityToken token = null;
        BigInteger custNum = new BigInteger(custnum);
        try {
	        	// as the endPoint.  Use sandbox.usaepay.com if connecting to
            // the sandbox server.
            client = usaepay.getClient(endPoint);
            // Instantiate security token object (need by all soap methods)
            token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );

            usaepayresponse.custnum = custnum;
            //Adding Payment Method
            BigInteger PaymentMethodID = client.addCustomerPaymentMethod(token, custNum, paymethod, makeItAsDefault, activateVerificationOfCreditCard);
            usaepayresponse.paymentmethodid = "" + PaymentMethodID;
        } catch (Exception e) {
            System.out.println("addAPaymentMethodeInCreateMode: " + e.getMessage());
            try {
                client.deleteCustomer(token, custNum);
                String[] retval1 = e.getMessage().split(":");
                String msg = "";
                if (retval1.length > 1) {
                    msg = retval1[1];
                } else {
                    msg = retval1[0];
                }
                usaepayresponse.message = "Error: " + e.getMessage();
            } catch (GeneralFault_Exception ex) {
                System.out.println("addAPaymentMethodeInCreateMode: " + ex.getMessage());
                //Logger.getLogger(PaySimpleAPI.class.getName()).log(Level.SEVERE, null, ex);
                usaepayresponse.message = "Error: " + ex.getMessage();
            }
        }
        return usaepayresponse;
    }

    public MypaySimpleObj addAPaymentMethodeInModificationMode(String custnum, String cardType, String cardNumber, String cardCode, String cardExpiration, String avdStreet, String avsZip, String typePayment, String accountNumber, String routingNumber, boolean makeItAsDefault, boolean activateVerificationOfCreditCard) {

        MypaySimpleObj usaepayresponse = new MypaySimpleObj();
        usaepayresponse.custnum = "";
        usaepayresponse.paymentmethodid = "";
        usaepayresponse.message = "good";

        PaymentMethodArray paymethods = new PaymentMethodArray();
        PaymentMethod paymethod = new PaymentMethod();

        if (typePayment.equals("CreditCard")) {
            // Setup Credit card data information
            /*CreditCardData ccdata = new CreditCardData();
            ccdata.setCardType(cardType);
            ccdata.setCardNumber(cardNumber);
            ccdata.setCardCode(cardCode);
            ccdata.setCardExpiration(cardExpiration);
            ccdata.setAvsStreet(avdStreet);
            ccdata.setAvsZip(avsZip);
            paymethod.setCreditCardData(ccdata);*/
            paymethod.setCardType(cardType);
            paymethod.setCardNumber(cardNumber);
            paymethod.setCardCode(cardCode);
            paymethod.setCardExpiration(cardExpiration);
            paymethod.setAvsStreet(avdStreet);
            paymethod.setAvsZip(avsZip);
        }
        if (typePayment.equals("ACH")) {
            // Setup check data information
            /*CheckData chdata = new CheckData();
            chdata.setAccount(accountNumber);
            chdata.setRouting(routingNumber);
            paymethod.setCheckData(chdata);*/
            paymethod.setRouting(routingNumber);
            paymethod.setAccount(accountNumber);

        }
        paymethod.setMethodName(typePayment);
        UeSoapServerPortType client = null;
        UeSecurityToken token = null;
        BigInteger custNum = new BigInteger(custnum);
        try {
	        	// as the endPoint.  Use sandbox.usaepay.com if connecting to
            // the sandbox server.
            client = usaepay.getClient(endPoint);
            // Instantiate security token object (need by all soap methods)
            token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );

            usaepayresponse.custnum = custnum;
            //Adding Payment Method
            BigInteger PaymentMethodID = client.addCustomerPaymentMethod(token, custNum, paymethod, true, activateVerificationOfCreditCard);
            usaepayresponse.paymentmethodid = "" + PaymentMethodID;
	         	//PaymentMethod currentMethod=client.getCustomerPaymentMethod(token, custNum,PaymentMethodID);

        } catch (Exception e) {
            System.out.println("addAPaymentMethodeInModificationMode: " + e.getMessage());
            try {
                //client.deleteCustomer(token, custNum);
                String[] retval1 = e.getMessage().split(":");
                String msg = "";
                if (retval1.length > 1) {
                    msg = retval1[1];
                } else {
                    msg = retval1[0];
                }
                usaepayresponse.message = "Error: " + msg;
            } catch (Exception ex) {
                System.out.println("addAPaymentMethodeInModificationMode: " + ex.getMessage());
                //Logger.getLogger(PaySimpleAPI.class.getName()).log(Level.SEVERE, null, ex);
                usaepayresponse.message = "Error: " + ex.getMessage();
            }
        }
        return usaepayresponse;
    }

    /**
     * Cette methode permet de faire des payments
     * de carte de credit à compte
     * @param customerID type=String
     * @param custCreditCardNum type=String
     * @param custCardCode type=String
     * @param custCardExpDate
     * @param amount type=double
     * @param sellerID type=String
     * @param sellerAccountNumber type=String
     * @param sellerRoutingNumber
     * @param paymentdescription type=String
     * @return return a object of type MypaySimpleObj
     */
    public MypaySimpleObj runPayment(String customerID, String custCreditCardNum, String custCardCode, String custCardExpDate, double amount, String sellerID, String sellerAccountNumber, String sellerRoutingNumber, String paymentdescription) {
        BigInteger custID = new BigInteger(customerID);
        CreditCardData custCard = new CreditCardData();
        custCard.setCardNumber(custCreditCardNum);
        custCard.setCardCode(custCardCode);
        custCard.setCardExpiration(custCardExpDate);
        BigInteger sellID = new BigInteger(sellerID);
        CheckData sellerCheck = new CheckData();
        sellerCheck.setAccount(sellerAccountNumber);
        sellerCheck.setRouting(sellerRoutingNumber);

        UeSoapServerPortType client = null;
        UeSecurityToken token = null;
	        	// as the endPoint.  Use sandbox.usaepay.com if connecting to
        // the sandbox server.
        // Instantiate security token object (need by all soap methods)
        try {
            client = usaepay.getClient(endPoint);
            token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            //read the customer
            CustomerObject cust = client.getCustomer(token, custID);
            CustomerObject sell = client.getCustomer(token, sellID);

            String payCustType = "CreditCard";
            String paySellType = "ACH";

            Date date = DateFunction.getGMTDate();
            MypaySimpleObj res = runtransactions(cust, amount, payCustType, custCard, null, "Cust_to_Camer", paymentdescription);
            if (!res.message.contains("Approved")) {
                System.out.println(date + "-------->++++" + res.message);
                res.message = "bad";
                return res;
            } else {
                res = runtransactions(sell, amount, paySellType, null, sellerCheck, "Camer_to_Cust", paymentdescription);
                if (!res.message.contains("Approved")) {
                    System.out.println(date + "-------->****" + res.message);
                    res.message = "bad";
                    return res;
                } else {
                    res.message = "good";
                    return res;
                }
            }
						//cust.

        } catch (Exception e) {
            System.out.println("runPayment: " + e.getMessage());
            // TODO Auto-generated catch block
            MypaySimpleObj res = new MypaySimpleObj();
            res.custnum = "";
            res.message = "";
            res.paymentmethodid = e.getMessage();
            e.printStackTrace();
            return res;
        }

    }

    /**
     * Cette methode permet de faire des payments
     * De compte à compte
     * @param customerID type=String
     * @param custAccountNumber
     * @param custRoutingNumber
     * @param custToCamerAmount type=double
     * @param camerToCustAmount
     * @param sellerID type=String
     * @param sellerAccountNumber type=String
     * @param sellerRoutingNumber
     * @param custPaymentdescription type=String
     * @param sellPaymentdescription
     * @return return a object of type MypaySimpleObj
     */
    public MypaySimpleObj runPayment(String customerID, String custAccountNumber, String custRoutingNumber, double custToCamerAmount, double camerToCustAmount, String sellerID,
            String sellerAccountNumber, String sellerRoutingNumber, String custPaymentdescription, String sellPaymentdescription) {

        BigInteger custID = new BigInteger(customerID);

        /*
         CreditCardData custCard = new CreditCardData();
         custCard.setCardNumber(custCreditCardNum);
         custCard.setCardCode(custCardCode);
         custCard.setCardExpiration(custCardExpDate);
         */
        CheckData custCheck = new CheckData();
        custCheck.setAccount(custAccountNumber);
        custCheck.setRouting(custRoutingNumber);

        BigInteger sellID = new BigInteger(sellerID);
        CheckData sellerCheck = new CheckData();
        sellerCheck.setAccount(sellerAccountNumber);
        sellerCheck.setRouting(sellerRoutingNumber);

        UeSoapServerPortType client = null;
        UeSecurityToken token = null;
        String notFoundRoutingNumMsgPref="Routing_Number_";
        String notFoundRoutingNumMsgSuffix="_was_not_found";
        // as the endPoint.  Use sandbox.usaepay.com if connecting to
        // the sandbox server.
        // Instantiate security token object (need by all soap methods)
        try {
            client = usaepay.getClient(endPoint);
            token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            //read the customer
            CustomerObject cust = client.getCustomer(token, custID);
            CustomerObject sell = client.getCustomer(token, sellID);

            //String payCustType = "CreditCard";
            String payCustType = "ACH";
            String paySellType = "ACH";

            Date date = DateFunction.getGMTDate();
            MypaySimpleObj sendRes;
            MypaySimpleObj payRes;
            //MypaySimpleObj res = runtransactions(cust, amount, payCustType, custCard, null, "Cust_to_Camer", paymentdescription);
             sendRes = runtransactions(cust, custToCamerAmount, payCustType, null, custCheck, "Cust_to_Camer", custPaymentdescription);
            if (!sendRes.message.contains("Approved") || !sendRes.resultCode.equalsIgnoreCase("A")) {
                System.out.println(
                                        "\n\n\nCustumer ID of error : "+sendRes.custnum+"\n"
                                        +"Transaction ID : "+sendRes.transactionID+"\n"
                                        +"Result code for receiving : "+sendRes.resultCode+"\n"
                                        +"ErrorCode for receiving : "+sendRes.errorCode+"\n"
                                        +date + "----Error Message when the excavator try to pay---->**** [ " + sendRes.errorCode + " ]" + sendRes.message
                                      );
                
                String notFoundRoutingNumMsg=notFoundRoutingNumMsgPref+custCheck.getRouting()+notFoundRoutingNumMsgSuffix;
                if(sendRes.message.contains(notFoundRoutingNumMsg))
                   sendRes.message = "NotExistRoutingNumber_sender";
                else if(sendRes.errorCode==0 && sendRes.resultCode.equalsIgnoreCase("D"))//Declined
                    sendRes.message = "TransactionDeclined_sender"; //the excavator have to contact his bank
                else if(sendRes.errorCode==38 || sendRes.errorCode==39)
                   sendRes.message = "IncorrectBillingInformation_sender";
                else if(sendRes.errorCode!=0)//Error in the e-payment system
                    sendRes.message = "TransactionSystemError_sender"; //The excavator have to do a physical pay or come later
                else 
                    sendRes.message= "notIdentifiedProblem_sender";
                return sendRes;
                
            } else {
                System.out.println(
                                   "Custumer ID sender : "+sendRes.custnum+"\n"
                                  +"RefNum for Sending : "+sendRes.transactionID+"\n"
                                  +"Result code for sending : "+sendRes.resultCode+"\n"
                                  +"ErrorCode for sending : "+sendRes.errorCode+"\n"
                                  );
                payRes = runtransactions(sell, camerToCustAmount, paySellType, null, sellerCheck, "Camer_to_Cust", sellPaymentdescription);
                if (!payRes.message.contains("Approved") || !payRes.resultCode.equalsIgnoreCase("A")) {
                    MypaySimpleObj refRes=refundtransactions(cust,String.valueOf(sendRes.transactionID), 0);
                    System.out.println(
                                        "\n\n\nCustumer ID refunded : "+refRes.custnum+"\n"
                                        +"Refund Result : "+refRes.resultCode+"\n"
                                        +"Ref Transaction ID : "+refRes.transactionID+"\n"
                                        +"Result code for receiving : "+payRes.resultCode+"\n"
                                        +"ErrorCode for receiving : "+payRes.errorCode+"\n"
                                        +date + "----Error Message for when paying the receiver---->**** [ " + payRes.errorCode + " ]" + payRes.message
                                      );
                String notFoundRoutingNumMsg=notFoundRoutingNumMsgPref+sellerCheck.getRouting()+notFoundRoutingNumMsgSuffix;
                if(payRes.message.contains(notFoundRoutingNumMsg))
                   payRes.message = "NotExistRoutingNumber_receiver";
                else if(sendRes.errorCode==0 && payRes.resultCode.equalsIgnoreCase("D"))//Declined
                    payRes.message = "TransactionDeclined_receiver"; //the excavator have to contact his bank
                else if(payRes.errorCode==38 || payRes.errorCode==39)
                   payRes.message = "IncorrectBillingInformation_receiver";
                else if(payRes.errorCode!=0)//Error in the e-payment system
                    payRes.message = "TransactionSystemError_receiver"; //The excavator have to do a physical pay or come later
                else 
                    payRes.message= "notIdentifiedProblem_receiver";
                return payRes;
                } else {
                    System.out.println(
                                   "\n\n\nCustumer ID receiver : "+payRes.custnum+"\n"
                                  +"RefNum for receiving : "+payRes.transactionID+"\n"
                                  +"Result code for receiving : "+payRes.resultCode+"\n"
                                  +"ErrorCode for receiving : "+payRes.errorCode+"\n"
                                  );
                    payRes.message = "good";
                    if(this.sendReceipt){
                        sendEmail(sendRes.transactionID, cust.getBillingAddress().getEmail());
                        sendEmail(payRes.transactionID, sell.getBillingAddress().getEmail());
                    }
                    MypaySimpleObj finalResult = new MypaySimpleObj();
                    finalResult.message="good";
                    finalResult.sendRef=sendRes.transactionID;
                    finalResult.payRef=payRes.transactionID;
                    finalResult.sendCustNum=sendRes.custnum;
                    finalResult.payCustNum=payRes.custnum;
                    return finalResult;
                }
            }
            //cust.

        } catch (Exception e) {
            System.out.println("runPayment: " + e.getMessage());
            // TODO Auto-generated catch block
            MypaySimpleObj res = new MypaySimpleObj();
            res.custnum = "";
            res.message = "";
            res.paymentmethodid = e.getMessage();
            e.printStackTrace();
            return res;
        }

    }

    /**
     * Cette methode permet de faire des payments
     * de carte de credit à carte de credit
     * @param customerID type=String
     * @param custCreditCardNum type=String
     * @param custCardCode type=String
     * @param custCardExpDate
     * @param amount type=double
     * @param sellerID type=String
     * @param sellerCreditcardNum
     * @param sellerCardExpDate
     * @param sellerCardCode type=String
     * @param paymentdescription type=String
     * @return return a object of type MypaySimpleObj
     */
    public MypaySimpleObj runPayment(String customerID, String custCreditCardNum, String custCardCode, String custCardExpDate, double amount, String sellerID, String sellerCreditcardNum, String sellerCardCode, String sellerCardExpDate, String paymentdescription) {
        BigInteger custID = new BigInteger(customerID);
        CreditCardData custCard = new CreditCardData();
        custCard.setCardNumber(custCreditCardNum);
        custCard.setCardCode(custCardCode);
        custCard.setCardExpiration(custCardExpDate);
        BigInteger sellID = new BigInteger(sellerID);
        CreditCardData sellerCard = new CreditCardData();
        sellerCard.setCardNumber(sellerCreditcardNum);
        sellerCard.setCardCode(sellerCardCode);
        sellerCard.setCardExpiration(sellerCardExpDate);

        UeSoapServerPortType client = null;
        UeSecurityToken token = null;
	        	// as the endPoint.  Use sandbox.usaepay.com if connecting to
        // the sandbox server.
        // Instantiate security token object (need by all soap methods)
        try {
            client = usaepay.getClient(endPoint);
            token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            //read the customer
            CustomerObject cust = client.getCustomer(token, custID);
            CustomerObject sell = client.getCustomer(token, sellID);

            String payCustType = "CreditCard";
            String paySellType = "CreditCard";

            Date date = DateFunction.getGMTDate();
            MypaySimpleObj res = runtransactions(cust, amount, payCustType, custCard, null, "Cust_to_Camer", paymentdescription);
            if (!res.message.contains("Approved")) {
                System.out.println(date + "-------->" + res.message);
                res.message = "bad";
                return res;
            } else {
                res = runtransactions(sell, amount, paySellType, sellerCard, null, "Camer_to_Cust", paymentdescription);
                if (!res.message.contains("Approved")) {
                    System.out.println(date + "-------->" + res.message);
                    res.message = "bad";
                    return res;
                } else {
                    res.message = "good";
                    return res;
                }
            }
						//cust.

        } catch (Exception e) {
            System.out.println("runPayment: " + e.getMessage());
            // TODO Auto-generated catch block
            MypaySimpleObj res = new MypaySimpleObj();
            res.custnum = "";
            res.message = "";
            res.paymentmethodid = e.getMessage();
            e.printStackTrace();
            return res;
        }

    }
    /**
     * This method is use to send email after transaction.
     * @param refNum
     * @param email
     * @return 
     */
    public boolean sendEmail(int refNum,String email){
        try {
            UeSoapServerPortType client = usaepay.getClient(endPoint);
            UeSecurityToken token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            BigInteger transactionNum=BigInteger.valueOf(refNum);
            Receipt receiptObject=client.getReceiptByName(token, "CustomerReceipt ");
            BigInteger receiptRefNum=receiptObject.getReceiptRefNum();
            client.emailTransactionReceipt(token, transactionNum, receiptRefNum, email);
            return true;
        } catch (Exception e) {
           //Logger.getLogger(PaySimpleAP.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error when sending : "+e.getMessage());
            return false;
        }
    
    }

    /**
     *
     * @param customer custumer
     * @param amount amount to transfert
     * @param type_payment CreditCard ou ACH
     * @param CreditCardData Object paymentMethod of customer
     * @param CheckData Object
     * @param paymentdirection Cust_to_Camer or Camer_to_Cust
     * @param description the reason of the payment
     * @return MypaysimpleObj
     * @throws IllegalArgumentException
     */
    public MypaySimpleObj runtransactions(CustomerObject customer, double amount, String type_payment, CreditCardData card, CheckData check, String paymentdirection, String description) throws IllegalArgumentException {
        MypaySimpleObj usaepayresponse = new MypaySimpleObj();
        usaepayresponse.custnum = customer.getCustNum();
        usaepayresponse.paymentmethodid = "";
        usaepayresponse.message = "";
        // Create response object
        TransactionResponse response = null;
        try {

            UeSoapServerPortType client = usaepay.getClient(endPoint);
            UeSecurityToken token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            BigInteger custNum = new BigInteger(customer.getCustNum());

            TransactionRequestObject params = new TransactionRequestObject();
            // populate transaction details
            TransactionDetail details = new TransactionDetail();
            details.setAmount(amount);
            details.setDescription(description);
            params.setDetails(details);
            params.setCustomerID(customer.getCustNum());
            params.setCustReceipt(!this.sendReceipt);
            params.setBillingAddress(customer.getBillingAddress());
            CustomerObject custumdata = client.getCustomer(token, custNum);
            params.setAccountHolder(custumdata.getBillingAddress().getFirstName() + " " + custumdata.getBillingAddress().getLastName());
            //System.out.println("Method name :" + paymethod.getMethodName());
            //System.out.println("Method name :" + paymethod.getCreditCardData().getCardNumber());
            if (type_payment.equals("CreditCard")) {
                // populate credit card data

                params.setCreditCardData(card);
            }
            if (type_payment.equals("ACH")) {
                // populate check data
                params.setCheckData(check);
            }

            if (paymentdirection.equals("Cust_to_Camer")) {
                // run Sale
                if (type_payment.equals("CreditCard")) {
                    response = client.runSale(token, params);//credit card
                }
                if (type_payment.equals("ACH")) {
                    response = client.runCheckSale(token, params);//ACH
                }
                usaepayresponse.message = "" + response.getResult() + " : " + response.getError();
                usaepayresponse.resultCode=response.getResultCode();
                usaepayresponse.transactionID=response.getRefNum().intValue();
                usaepayresponse.errorCode = (response.getErrorCode() == null ? 0 : response.getErrorCode().intValue());
            }
            if (paymentdirection.equals("Camer_to_Cust")) {
                // run credit
                if (type_payment.equals("CreditCard")) {
                    response = client.runCredit(token, params);//credit card
                }
                if (type_payment.equals("ACH")) {
                    response = client.runCheckCredit(token, params);//ACH
                }
                usaepayresponse.message = "" + response.getResult() + " : " + response.getError();
                usaepayresponse.resultCode=response.getResultCode();
                usaepayresponse.transactionID=response.getRefNum().intValue();
                usaepayresponse.errorCode = (response.getErrorCode() == null ? 0 : response.getErrorCode().intValue());
            }
            //Boolean res=client.deleteCustomer(token, custNum);
            //System.out.println("User Deleted ? : "+res);
        } catch (Exception e) {
            System.out.println("runtransactions: " + e.getMessage());
            usaepayresponse.message = "Error: " + e.getMessage();
            usaepayresponse.resultCode=response.getResultCode();
            usaepayresponse.errorCode = -1;
        }
       /* System.out.println("return usaepayresponse ["
                + "\n response.getErrorCode: " + (response == null ? -1 : response.getErrorCode()) + ""
                + "\n response.getError: " + (response == null ? "" : response.getError()) + ""
                + "\n response.getResult: " + (response == null ? "" : response.getResult())
                + "\n response.isIsDuplicate: " + (response == null ? "" : response.isIsDuplicate())
                + "\n response.getRefNum: " + (response == null ? "" : response.getRefNum())
                + "\n response.getStatus: " + (response == null ? "" : response.getStatus())
                + "\n response.getStatusCode: " + (response == null ? "" : response.getStatusCode())
        + "]");*/
        return usaepayresponse;
    }
    /**
     *
     * @param customer custumer
     * @param amount amount to transfert
     * @param type_payment CreditCard ou ACH
     * @param CreditCardData Object paymentMethod of customer
     * @param CheckData Object
     * @param paymentdirection Cust_to_Camer or Camer_to_Cust
     * @param description the reason of the payment
     * @return MypaysimpleObj
     * @throws IllegalArgumentException
     */
    public MypaySimpleObj refundtransactions(CustomerObject customer,String refNum,double amount) throws IllegalArgumentException {
        MypaySimpleObj usaepayresponse = new MypaySimpleObj();
        usaepayresponse.custnum = customer.getCustNum();
        usaepayresponse.paymentmethodid = "";
        usaepayresponse.message = "";
        // Create response object
        TransactionResponse response = null;
        try {

            UeSoapServerPortType client = usaepay.getClient(endPoint);
            UeSecurityToken token = usaepay.getToken(
                    sourceKey, // source key
                    sourcePin, // source pin  (if assigned by merchant)
                    clientIpAddress // IP address of end client (if applicable)
            );
            BigInteger custNum = new BigInteger(customer.getCustNum());

          
            BigInteger refNumber=new BigInteger(refNum);
            //response=client.refundTransaction(token,refNumber,amount);
            boolean voided=client.voidTransaction(token,refNumber);
            if(voided){
            //Boolean res=client.deleteCustomer(token, custNum);
            //System.out.println("User Deleted ? : "+res);
            usaepayresponse.message = "good";//"" + response.getResult() + " : " + response.getError();
            usaepayresponse.resultCode=String.valueOf(voided);
            usaepayresponse.transactionID=-1;//response.getRefNum().intValue();
           // usaepayresponse.errorCode = (response.getErrorCode() == null ? 0 : response.getErrorCode().intValue());
            }
            else{
                
             response=client.refundTransaction(token,refNumber,amount);
             usaepayresponse.message = "" + response.getResult() + " : " + response.getError();
             usaepayresponse.resultCode=response.getResultCode();
             usaepayresponse.transactionID=response.getRefNum().intValue();
             usaepayresponse.errorCode = (response.getErrorCode() == null ? 0 : response.getErrorCode().intValue());
            }
         } catch (Exception e) {
            System.out.println("refundtransactions: " + e.getMessage());
            usaepayresponse.message = "Error: " + e.getMessage();
            usaepayresponse.resultCode="Void";//response.getResultCode();
            usaepayresponse.errorCode = -1;
        }
        /*System.out.println("return usaepayresponse ["
                + "\n response.getErrorCode: " + (response == null ? -1 : response.getErrorCode()) + ""
                + "\n response.getError: " + (response == null ? "" : response.getError()) + ""
                + "\n response.getResult: " + (response == null ? "" : response.getResult())
                + "\n response.isIsDuplicate: " + (response == null ? "" : response.isIsDuplicate())
                + "\n response.getRefNum: " + (response == null ? "" : response.getRefNum())
                + "\n response.getStatus: " + (response == null ? "" : response.getStatus())
                + "\n response.getStatusCode: " + (response == null ? "" : response.getStatusCode())
        + "]");*/
        return usaepayresponse;
    }
    
    
    public static void main(String[] args) {
	//Setup output structure
	MypaySimpleObj usaepayresponse = new MypaySimpleObj();
	MypaySimpleObj usaepayresponse1 = new MypaySimpleObj();
	PaySimpleAP usaepayapi = new PaySimpleAP();
	// Setup address information
    /*Address address = new Address();
    address.setFirstName("John");
 	address.setLastName("Doe");
 	address.setCompany("Acme INC");
 	address.setStreet("342 Main Street");
 	address.setCity("somewhere");
 	address.setState("CA");
 	address.setZip("91920");
 	address.setCountry("US");
 	address.setEmail("joe@example.com");
 	address.setFax("");
 	address.setPhone("333-444-5555");
 	
    // Setup Credit card data information
    CreditCardData ccdata = new CreditCardData();
    ccdata.setCardType("VISA");
    ccdata.setCardNumber("4444555566667779");
    ccdata.setCardCode ("880");
	ccdata.setCardExpiration("0917");
	ccdata.setAvsStreet("342 Main Street");
	ccdata.setAvsZip("91920");
    // Setup check data information
	CheckData chdata = new CheckData();
	chdata.setAccount("1112223333");
	chdata.setRouting("123456789");
	// Setup amount
	double amount= 23.50;
	// Setup start date
	String start_date ="2014-09-01";
	// Setup type payment
	String type_payment="CreditCard";//"ACH";"CreditCard";
	//Setup payment direction
	String paymentdirection="Cust_to_Camer";//"Camer_to_Cust"
	
	/*usaepayresponse= usaepayapi.subscription(address,amount,type_payment,ccdata,chdata,start_date);
	System.out.println("custumer ID:" + usaepayresponse.custnum);
	System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
	System.out.println("Message:" + usaepayresponse.message);
	
	if(usaepayresponse.message.equals(""))
	{
	   usaepayresponse1= usaepayapi.transactions(usaepayresponse.custnum,address,amount,type_payment,ccdata,chdata,paymentdirection);
	   System.out.println("custumer ID:" + usaepayresponse1.custnum);
	   System.out.println("Method ID:" + usaepayresponse1.paymentmethodid);
	   System.out.println("Message:" + usaepayresponse1.message);
	 }*/
	/*usaepayresponse= usaepayapi.modifyCustomerToOurPaySimpleAccount(        "4238286",
										"Manuela",//firstName, 
										"Tchagou",//lastName, 
										"SOBGUI",//company, 
										"342 Main Street",//street, 
										"yaounde",//city, 
										"CA",//state, 
										"91920",//zipCode,
										"US",//country, 
										"tchagou2e@gmail.com",//email, 
										"",//fax, 
										"333-444-5555",//phone, 
										1,//amount,
										"2014-09-01"//startDate
										);
	System.out.println("custumer ID:" + usaepayresponse.custnum);
	System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
	System.out.println("Message:" + usaepayresponse.message);*/
        usaepayresponse= usaepayapi.runPayment(
                                                "4236108", 
                                                "555555555",
                                                "999999999",
                                                //"987654321",
                                                9999.99,
                                                9999.99,
                                                "4238286",
                                                "444444444",
                                                "987654321",
                                                //"444444444",
                                                "Reglement du Job 1",
                                                "Reglement du Job 1"
        );
        System.out.println("\n\n\n\n\nFinal\n\ncustumer ID:" + usaepayresponse.custnum);
	System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
	System.out.println("Message:" + usaepayresponse.message);
        System.out.println("SendRef:" + usaepayresponse.sendRef);
        System.out.println("PayRef:" + usaepayresponse.payRef);
        //System.out.println("AllTransactionNum:" + usaepayresponse.allTransactionNum);
        //System.out.println("AllCustNum:" + usaepayresponse.allCustNum);
        /*usaepayresponse= usaepayapi.addNewCustomerToOurPaySimpleAccount(
										"Eric",//firstName, 
										"Etienne",//lastName, 
										"Colonnedefeu",//company, 
										"342 Main Street",//street, 
										"yaounde",//city, 
										"CA",//state, 
										"91920",//zipCode,
										"US",//country, 
										"tchagou2e@gmail.com",//email, 
										"",//fax, 
										"333-444-5555",//phone, 
										1,//amount,
										"2014-09-01"//startDate
										);
	System.out.println("custumer ID:" + usaepayresponse.custnum);
	System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
	System.out.println("Message:" + usaepayresponse.message);*/
	 // if(usaepayresponse.message.equalsIgnoreCase("good")){
		/*  usaepayresponse= usaepayapi.addAPaymentMethodeInCreateMode(
				  					 // usaepayresponse.custnum,//custnum, 
                                                                          "4236108",
									  "",//cardType, 
									  "",//cardNumber, 
									  "",//cardCode, 
									  "",//cardExpiration, 
									  "",//avdStreet, 
									  "",//avsZip, 
									  "ACH",//typePayment, 
									  "444444444",//accountNumber, 
									  "444444444",//routingNumber
									  true,//set this payment methode as default
									  true// activate creditCard verification.
									  ) ; 
		  System.out.println("custumer ID:" + usaepayresponse.custnum);
			System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
			System.out.println("Message:" + usaepayresponse.message);*/
                
	  //}*/
         /*usaepayresponse= usaepayapi.addAPaymentMethodeInModificationMode(
				  					  "4236108",//custnum, 
									  "",//cardType, 
									  "",//cardNumber, 
									  "",//cardCode, 
									  "",//cardExpiration, 
									  "",//avdStreet, 
									  "",//avsZip, 
									  "ACH",//typePayment, 
									  "333333333",//accountNumber, 
									  "987654321",//routingNumber
									  false,//set this payment methode as default
									  true// activate creditCard verification.
									  ) ; 
         System.out.println("custumer ID:" + usaepayresponse.custnum);
			System.out.println("Method ID:" + usaepayresponse.paymentmethodid);
			System.out.println("Message:" + usaepayresponse.message);*/
	}
}
