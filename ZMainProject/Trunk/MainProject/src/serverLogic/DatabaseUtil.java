package serverLogic;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import people.User;
import attendance.Absence;
import attendance.AttendanceReport;
import attendance.Event;
import attendance.Tardy;
import forms.Form;

/**
 * This is a class used to add stuff to the database
 * 
 * @author Brandon
 * 
 */
public class DatabaseUtil 
{
	public static void addAttendanceReport(AttendanceReport report)
	{
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(report);
		em.close();
	}
	
	public static AttendanceReport getAttendanceReport(String netID)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from AttendanceReport a where a.netID = :netID");
		q.setParameter("netID", netID);
		AttendanceReport result;
		try
		{
			result = (AttendanceReport) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			result = null;
		}
		em.close();
		return result;
	}
	
	public static void addEvent(Event toAdd)
	{
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(toAdd);
		em.close();
	}
	
	public static void addUser(User guy)
	{
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(guy);
		em.close();
	}
	
	public static User getUser(String netID)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select p from User p where p.netID = :netID");
		q.setParameter("netID", netID);
		
		User result;
		// See if that person actually exists
		try {
			result = (User) q.getSingleResult();
		} catch (NoResultException e) {
			result = null;
		}
		em.close();
		return result;
	}
	
	public static String[] listAllUsers() 
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from User m");
		List<User> people = (List<User>) q.getResultList();
		String[] toRet = new String[people.size()];
		for (int i = 0; i < people.size(); i++) {
			User p = people.get(i);
			if (!p.getType().equalsIgnoreCase("Director"))
				toRet[i] = p.toString();
		}
		em.close();
		return toRet;
	}

	public static void addAbsence(Absence newAbsence) {
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(newAbsence);
		em.close();	
	}

	public static void addTardy(Tardy newTardy) {
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(newTardy);
		em.close();	
	}

	public static List<Absence> getAbsences(String netID) {

		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Absence a");
		q.setParameter("netID", netID);
		List<Absence> absences;
		try
		{
			absences = (List<Absence>) q.getResultList();
		}
		catch (NoResultException e)
		{
			absences = null;
		}
		em.close();
		return absences;
	}

	public static List<Tardy> getTardies(String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from Tardy t");
		q.setParameter("netID", netID);
		List<Tardy> tardies;
		try
		{
			tardies = (List<Tardy>) q.getResultList();
		}
		catch (NoResultException e)
		{
			tardies = null;
		}
		em.close();
		return tardies;
	}

	public static List<Form> getForms(String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from AttendanceReport a where a.netID = :netID");
		q.setParameter("netID", netID);
		List<Form> result;
		try
		{
			result = (List<Form>) q.getResultList();
		}
		catch (NoResultException e)
		{
			result = null;
		}
		em.close();
		return result;
	}
	
	public static void addForm(Form form)
	{
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(form);
		em.close();	
	}

//	public static File getFile(String attachedFile) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public static String addFile(File attachedFile) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}