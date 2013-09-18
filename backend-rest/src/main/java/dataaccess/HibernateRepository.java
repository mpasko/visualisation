/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;

/**
 *
 * @author marcin
 */
public class HibernateRepository extends Repository{
	//private SessionFactory sessionFactory;
	/*
	public Repository (SessionFactory factory){
	    Connector connector = new Connector();
	    SessionFactory factory = connector.tryConnect();
		this.sessionFactory=factory;
		starter=null;
	}
	*/ 
	/*
	private static Directory getMaxIDDirectory(Session session){
		Criteria criteria = session.createCriteria(Directory.class);
		criteria.addOrder(Order.desc("dbid"));
		criteria.setMaxResults(1);
		Directory dir = (Directory)criteria.uniqueResult();
		if(dir == null){
			dir = new Directory();
			session.save(dir);
		}
		return dir;
	}
	*/ 
	
	@Override
	public Directory getDirectory() throws Exception {
		/*
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Directory dir = getMaxIDDirectory(session);
		transaction.commit();
		session.close();
		return dir;
		*/
		return null;
	}

	@Override
	public void saveExperiment(InputPair experiment) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void saveResult(OutputPair result) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void onStop() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
