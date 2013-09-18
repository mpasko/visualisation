/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

/**
 *
 * @author marcin
 */
public class Connector {
	/*
	static Logger log = Logger.getLogger("Hibernate");
	Configuration configuration = null;
	SessionFactory sessionFactory = null;
	File configFile;
	
	
	public SessionFactory tryConnect() throws NoDatabaseConfiguredException{
		try {
			configFile = new File("./hibernate.cfg.xml");
			configuration = new Configuration();
			configuration.configure(configFile);
				
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

	//		Session session = sessionFactory.openSession();

		} catch (Throwable t) {
			printLongerTrace(t);
			throw new NoDatabaseConfiguredException();
		}
		return sessionFactory;
	}
	
	static void printLongerTrace(Throwable t){
		/* *x/
		log.log(Level.SEVERE, "[{0}]:{1}", new Object[]{t.getClass().getName(), t.getMessage()});
	    for(StackTraceElement e: t.getStackTrace()) {
			log.severe(e.toString());
		}
		/* *x/
		t.printStackTrace();
	}
	*/ 
}
