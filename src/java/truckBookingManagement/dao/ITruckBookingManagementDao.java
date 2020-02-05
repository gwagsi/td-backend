/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckBookingManagement.dao;

import toolsAndTransversalFunctionnalities.Result;
import util.exception.TruckBookingManagementException;

/**
 *
 * @author erman
 */
public interface ITruckBookingManagementDao {
    
    /**
     *
     * @param bookingDate
     * @param truckID
     * @param basketID
     * @param clientID identifiant de celui qui veut reserver le camion
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String bookNewTruck(String bookingDate, int truckID, int basketID, int clientID)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @param bookingDate
     * @param truckID
     * @param basketID
     * @param clientID identifiant de celui qui veut reserver le camion
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String editBookingDate(int truckBookingID, String bookingDate, int truckID, int basketID, int clientID)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfBookingTruck(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfNotBookingTruck(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfValidatTruckBook(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String removeTruckBooking(int truckBookingID)throws TruckBookingManagementException;
    
    /**
     *
     * @param basketID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String deletedBasket(int basketID)throws TruckBookingManagementException;
    
    /**
     *
     * @param basketID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String confimedBasket(int basketID)throws TruckBookingManagementException;
    
    /**
     *
     * @param basketID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String completBasket(int basketID)throws TruckBookingManagementException;

    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfBookingInfoByTruckID(int truckID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfValidatedBookingByTruckID(int truckID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getUnavailabilityDatesByTruckID(int truckID)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String confirmTruckBooking(int truckBookingID)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String rejectBookingForTruck(int truckBookingID)throws TruckBookingManagementException;

    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfBasketByAccountID(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfValidBasketByAccountID(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfUnValidBasketByAccountID(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;

    /**
     *
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfConfirmTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfNotConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    
    /**
     *
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getBookingDateByID(int truckBookingID)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @param submitReview
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String editSubmitReview(int truckBookingID, int submitReview)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @param reviewComment
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String editReviewComment(int truckBookingID, String reviewComment)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfTruckOwnerReviewComment(int truckID, int index, int nombreMaxResult)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfTruckBook(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckBookingID
     * @param bookingPrice
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String editBookingPrice(int truckBookingID, float bookingPrice)throws TruckBookingManagementException;

    /**
     *
     * @param message
     * @param truckID
     * @param subject
     * @param name
     * @param email
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String contactTruckOwner(String message, int truckID, String subject, String name, String email)throws TruckBookingManagementException;

    /**
     *
     * @param clientID
     * @param truckBookingID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String payTruckBookingByClient(int clientID, int truckBookingID)throws TruckBookingManagementException;
    
    /**
     *
     * @param truckOwnerID
     * @param truckBookingID
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public String payTruckBookingByTruckOwner(int truckOwnerID, int truckBookingID)throws TruckBookingManagementException;
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfTruckBookHistory(int accountID, int index, int nombreMaxResultat)throws TruckBookingManagementException;

    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResult
     * @return
     * @throws util.exception.TruckBookingManagementException
     */
    public Result getARangeOfTruckReviewComment(int truckID, int index, int nombreMaxResult)throws TruckBookingManagementException;

}
