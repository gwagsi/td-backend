/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truckDriverBinding.metier;

import toolsAndTransversalFunctionnalities.Result;



/**
 *
 * @author erict
 */
public interface ITruckDriverBindingWs {

    /**
     *
     * @param clientID
     * @param truckID
     * @param driverID
     * @return
     */
    public String bindTruckAndDriver(int clientID,int truckID,int driverID);

    /**
     *
     * @param clientID
     * @param index
     * @param maxtuple
     * @return
     */
    public Result getABindingRange(int clientID,int index,int maxtuple);

    /**
     *
     * @param bindingID
     * @return
     */
    public String activateBinding(int bindingID);

    /**
     *
     * @param bindingID
     * @return
     */
    public String deleteBinding(int bindingID);
    
    /**
     *
     * @param clientID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfTruckByUser(int clientID, int index, int nombreMaxResultat);
    
    /**
     *
     * @param clientID
     * @param index
     * @param nombreMaxResultat
     * @return
     */
    public Result getARangeOfDriverByUser(int clientID, int index, int nombreMaxResultat);
}
