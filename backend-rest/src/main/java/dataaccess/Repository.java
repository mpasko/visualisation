/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import agh.aq21gui.model.management.Directory;
import agh.aq21gui.model.management.InputPair;
import agh.aq21gui.model.management.OutputPair;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

/**
 *
 * @author marcin
 */
public class Repository {
	private final SessionFactory sessionFactory;
	
	private static Repository __sole_instance = null;
	
	public static Repository getRepository() throws NoDatabaseConfiguredException{
		if(__sole_instance == null){
			Connector connector = new Connector();
			SessionFactory factory = connector.tryConnect();
			__sole_instance = new Repository(factory);
		}
		return __sole_instance;
	}
	
	public Repository (SessionFactory factory){
		this.sessionFactory=factory;
	}
	
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

	public Directory getDirectory() {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Directory dir = getMaxIDDirectory(session);
		transaction.commit();
		session.close();
		return dir;
	}
	
	public void saveExperiment(InputPair experiment){
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Directory dir = getMaxIDDirectory(session);
		dir.getexperiments().add(experiment);
		session.saveOrUpdate(dir);
		transaction.commit();
		session.close();
	}
	
	public void saveResult(OutputPair result){
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Directory dir = getMaxIDDirectory(session);
		dir.getresults().add(result);
		session.saveOrUpdate(dir);
		transaction.commit();
		session.close();
	}
}
