package com.cybercom.librarytest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LocalEntityManagerFactory implements ServletContextListener {
	
	private static final boolean SHOULD_INIT_TEST_DATA = true;
	
    private static EntityManagerFactory emf;
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        emf = Persistence.createEntityManagerFactory("LibraryPU");
        if (SHOULD_INIT_TEST_DATA) {
        	TestData.initTestData(emf.createEntityManager());
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	if (emf != null) {
    		emf.close();
    	}
    }
    public static EntityManager createEntityManager() {  
        if (emf == null) {
            throw new IllegalStateException("Context is not initialized yet.");  
        }
        return emf.createEntityManager();
    }
}
