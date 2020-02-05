/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckBookingManagement.metier;

import toolsAndTransversalFunctionnalities.Result;

/**
 *
 * @author erman
 */
public interface ITruckBookingManagementWs {
    
    /**
     *
     * @param bookingDate
     * @param truckID
     * @param basketID
     * @param clientID
     * @return
     */
    public String bookNewTruck(String bookingDate, int truckID, int basketID, int clientID);
    
    
    /**
     *
     * @param truckBookingID
     * @param bookingDate
     * @param truckID
     * @param basketID
     * @param clientID
     * @return
     */
    public String editBookingDate(int truckBookingID, String bookingDate, int truckID, int basketID, int clientID);
    
    /**
     *
     * Retourne tous les trucks reserves d'un truckOwner et confirmes par les clients
     * @param accountID identifiant du truckOwner
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfBookingTruck(int accountID, int index, int nombreMaxResultat);
    
    
    /**
     *
     * Retourne tous les trucks reserves d'un truckOwner et non confirmes par les clients
     * @param accountID identifiant du truckOwner
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfNotBookingTruck(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param truckBookingID
     * @return
     */
    public String removeTruckBooking(int truckBookingID);
    
    /**
     *
     * @param basketID
     * @return
     */
    public String deletedBasket(int basketID);
    
    /**
     *
     * @param basketID
     * @return
     */
    public String confimedBasket(int basketID);
    
    
    /**
     *
     * @param basketID
     * @return
     */
    public String completBasket(int basketID);
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfBookingInfoByTruckID(int truckID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfValidatedBookingByTruckID(int truckID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param truckID
     * @return
     */
    public Result getUnavailabilityDatesByTruckID(int truckID);
    
    /**
     *
     * @param truckBookingID
     * @return
     */
    public String confirmTruckBooking(int truckBookingID);
    
    /**
     *
     * @param truckBookingID
     * @return
     */
    public String rejectBookingForTruck(int truckBookingID);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfValidatTruckBook(int accountID, int index, int nombreMaxResultat);
    
    
    /**
     * Retourne tous les paniers d'un utilisateur trier par ordre decroissant de date de creation.
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfBasketByAccountID(int accountID, int index, int nombreMaxResultat);
    
    /**
     * Retourne tous les paniers validés d'un utilisateur trier par ordre decroissant de date de creation.
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfValidBasketByAccountID(int accountID, int index, int nombreMaxResultat);
    
    /**
     *
     * Retourne tous les paniers non validés d'un utilisateur trier par ordre decroissant de date de creation.
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfUnValidBasketByAccountID(int accountID, int index, int nombreMaxResultat);

    /**
     *
     * Retourne tous les camions confirmes d'un panier d'un utilisateur.
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat);
    

    /**
     *
     * Retourne tous les camions confirmes d'un panier d'un utilisateur.
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfConfirmTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat);
    
    /**
     *
     * Retourne tous les camions non confirmes d'un panier d'un utilisateur.
     * @param accountID identifiant du client
     * @param basketID identifiant du panier
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfNotConfirmTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat);
    
    
    /**
     *
     * Retourne tous les camions d'un panier d'un utilisateur.
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckByBasketID(int accountID, int basketID, int index, int nombreMaxResultat);
    
    /**
     *
     * Retourne tous les camions d'un panier d'un utilisateur.
     * @param accountID
     * @param basketID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckByBasketIDFrontEnd(int accountID, int basketID, int index, int nombreMaxResultat);

    /**
     *
     * @param truckBookingID
     * @return
     */
    public Result getBookingDateByID(int truckBookingID);
    
    /**
     *
     * @param truckBookingID
     * @param submitReview
     * @return
     */
    public String editSubmitReview(int truckBookingID, int submitReview);
    
    
    /**
     *
     * @param truckBookingID
     * @param reveiwComment
     * @return
     */
    public String editReviewComment(int truckBookingID, String reveiwComment);
    
    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfTruckOwnerReviewComment(int truckID, int index, int nombreMaxResult);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckBook(int accountID, int index, int nombreMaxResultat);

    /**
     *
     * @param truckBookingID
     * @param bookingPrice
     * @return
     */
    public String editBookingPrice(int truckBookingID, float bookingPrice);

    /**
     *
     * @param message
     * @param truckID
     * @param subject
     * @param name
     * @param email
     * @return
     */
    public String contactTruckOwner(String message, int truckID, String subject, String name, String email);
    
    /**
     * Cette fonction permet de contacter le truckOwner avec la possibilité de mettre
     * l'emetteur en copie du mail.
     *
     * @param message
     * @param truckID
     * @param subject
     * @param name
     * @param email
     * @param isCopy
     * @return
     */
    public String contactTruckOwnerNEW(String message, int truckID, String subject, String name, String email, boolean isCopy);

    /**
     *
     * @param clientID
     * @param truckBookingID
     * @return
     */
    public String payTruckBookingByClient(int clientID, int truckBookingID);

    /**
     *
     * @param truckOwnerID
     * @param truckBookingID
     * @return
     */
    public String payTruckBookingByTruckOwner(int truckOwnerID, int truckBookingID);
    
    /**
     *
     * @param accountID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckBookHistory(int accountID, int index, int nombreMaxResultat);

    /**
     *
     * @param truckID
     * @param index
     * @param nombreMaxResult
     * @return
     */
    public Result getARangeOfTruckReviewComment(int truckID, int index, int nombreMaxResult);
     
}
