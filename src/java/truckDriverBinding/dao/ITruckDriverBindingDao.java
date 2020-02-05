/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckDriverBinding.dao;

import entities.Drive;
import entities.Driver;
import entities.Truck;
import java.util.List;
import util.exception.TruckDriverBindingException;

/**
 *
 * @author erict
 */
public interface ITruckDriverBindingDao {
    
    /**
     *
     * @param accountID
     * @param truckID
     * @param driverID
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public String bindTruckAndDriver(int accountID,int truckID,int driverID) throws TruckDriverBindingException;

    /**
     *
     * @param accountID
     * @param index
     * @param maxtuple
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public List<Drive> getABindingRange(int accountID, int index, int maxtuple)throws TruckDriverBindingException;

    /**
     *
     * @param accountID
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public List<Drive> getAllBinding(int accountID)throws TruckDriverBindingException;

    /**
     *
     * @param bindingID
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public String activateBinding(int bindingID)throws TruckDriverBindingException;

    /**
     *
     * @param bindingID
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public String deleteBinding(int bindingID)throws TruckDriverBindingException;
    
    /**
     *
     * @param userID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public List<Truck> getARangeOfTruckByUser(int userID, int index, int nombreMaxResultat)throws TruckDriverBindingException;
    
    /**
     *
     * @param userID
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public List<Truck> getAllTruckByUser(int userID)throws TruckDriverBindingException;
    
    /**
     *
     * @param userID
     * @param index
     * @param nombreMaxResultat
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public List<Driver> getARangeOfDriverByUser(int userID, int index, int nombreMaxResultat)throws TruckDriverBindingException;
    
    /**
     *
     * @param userID
     * @return
     * @throws util.exception.TruckDriverBindingException
     */
    public List<Driver> getAllDriverByUser(int userID)throws TruckDriverBindingException;

}
