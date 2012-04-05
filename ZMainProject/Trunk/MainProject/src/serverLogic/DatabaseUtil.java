package serverLogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import attendance.Event;

import people.User;

//addAttendanceReport(AttendanceReport report);
//getAttendanceReport(String netID);

/**
 * This is a class used to add stuff to the database
 * 
 * @author Brandon
 * 
 */
public class DatabaseUtil 
{
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
	
//	
//	/**
//	 * A Method to add a person type to the database
//	 * @param p the Person to add
//	 */
//	public static void addPerson(Person p) {
//		EntityManager em = EMFService.get().createEntityManager();
//		em.persist(p);
//		em.close();
//	}
//	
//	/**
//	 * A Method to add a Student type to the database
//	 * @param s the Student to add
//	 */
//	public static void addStudent(Student s)
//	{
//		EntityManager em = EMFService.get().createEntityManager();
//		em.persist(s);
//		em.close();
//	}
//
//	
//	/**
//	 * A method that gets a person based off the given netID
//	 * 
//	 * @param netID
//	 *            the netID registered with a student
//	 * @return either the person with that netID or null if not found
//	 */
//	public static Person getPerson(String netID) {
//
//		EntityManager em = EMFService.get().createEntityManager();
//		Query q = em.createQuery("select p from Person p where p.netID = :netID");
//		q.setParameter("netID", netID);
//
//		Person p;
//		// See if that person actually exists
//		try {
//			p = (Person) q.getSingleResult();
//		} catch (NoResultException e) {
//			p = null;
//		}
//		em.close();
//		return p;
//	}

//	public static String[] listAll() 
//	{
//		EntityManager em = EMFService.get().createEntityManager();
//		Query q = em.createQuery("select m from Person m");
//		List<Person> people = q.getResultList();
//		String[] toRet = new String[people.size()];
//		for (int i = 0; i < people.size(); i++) {
//			Person p = people.get(i);
//			toRet[i] = p.toString();
//		}
//		em.close();
//		return toRet;
//	}

}
