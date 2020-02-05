/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package toolsAndTransversalFunctionnalities;

import java.util.List;

/**
 *
 * @author erman
 */
public class ResultBackend extends Result{
    
    protected List<Object> resultObjList;
    protected List<Object[]> resultArraysList;

    public List<Object> getResultObjList() {
        return resultObjList;
    }

    public void setResultObjList(List<Object> resultObjList) {
        this.resultObjList = resultObjList;
    }

    public List<Object[]> getResultArraysList() {
        return resultArraysList;
    }

    public void setResultArraysList(List<Object[]> resultArraysList) {
        this.resultArraysList = resultArraysList;
    }
    
    
    
}
