/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package toolsAndTransversalFunctionnalities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ERIC TCHAGOU
 */
@Stateless

public class GestionnaireEntite {
   // @PersistenceContext 
    private EntityManager em;// ceci est le gestionnaire d'entite(Entity Manager)
    private EntityManagerFactory emf;// ceci est la fabrique du gestionnaire d'entite
   //@PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
          this.emf=emf;
    }

    @PersistenceContext 
    public void setEm(EntityManager em) {
       this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }
    public void creatEntityManager() {
         //em=emf.createEntityManager();;
    }
    public void beginTransaction(){//cette methode permet de debuter une transaction
        em=emf.createEntityManager();
        em.getTransaction().begin();;
    }
    public void endTransaction(){//cette methode permet de fermer une transaction
           em.getTransaction().commit();
           em.close();
    }
    public void closeEm(){
        em.flush();
       // em.close();
    }
    public void rollbackTransaction(){
        em.getTransaction().rollback();
    }
    
    public void evictAll(){
        em.getEntityManagerFactory().getCache().evictAll();
    }
    
    
}
