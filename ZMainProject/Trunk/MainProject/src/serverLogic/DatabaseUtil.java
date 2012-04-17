package serverLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import people.User;
import attendance.Absence;
import attendance.AttendanceReport;
import attendance.EarlyCheckOut;
import attendance.Event;
import attendance.Tardy;

import comment.Message;

import forms.Form;

/**
 * This is a class used to add stuff to the database
 * 
 * @author Brandon
 * 
 */
public class DatabaseUtil 
{
	public static void dropATrainOnEm()
	{
		List<Form> forms = getAllForms();
		List<User> users = getStudents();
		List<Absence> absences = getAllAbsences();
		List<Tardy> tardies = getAllTardies();
		List<Event> events = getAllEvents();
		List<AttendanceReport> reports = getAllReports();
		List<Message> messages = getAllMessages();
		
		for (int i = 0; i < reports.size(); i++)
		{
			removeReport(reports.get(i));
		}
		
		for (int i = 0; i < events.size(); i++)
		{
			removeEvent(events.get(i));
		}
		
		for (int i = 0; i < tardies.size(); i++)
		{
			removeTardy(tardies.get(i));
		}
		
		for (int i = 0; i < absences.size(); i++)
		{
			removeAbsence(absences.get(i));
		}
		
		for (int i = 0; i < forms.size(); i++)
		{
			removeForm(forms.get(i));
		}
		
		for (int i = 0; i < messages.size(); i++)
		{
			removeMessage(messages.get(i));
		}
		
		for (int i = 0; i < users.size(); i++)
		{
			if (users.get(i).getType().equalsIgnoreCase("Student"))
				removeUser(users.get(i));
		}
		
	}
	
	public static void removeAllFormsFor(String netID)
	{
		List<Form> forms = getForms(netID);
		for (Form e: forms)
		{
			removeForm(e);
		}
	}
	
	public static void removeAllAbsencesFor(String netID)
	{
		List<Absence> absences = getAbsences(netID);
		for (Absence e: absences)
		{
			removeAbsence(e);
		}
	}
	
	public static void removeAllTardiesFor(String netID)
	{
		List<Tardy> tardies = getTardies(netID);
		for (Tardy e: tardies)
		{
			removeTardy(e);
		}
	}
	
	public static Form getFormByID(Long id)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Form a where a.id = :id");
		q.setParameter("id", id);
		Form result;
		try
		{
			result = (Form) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			result = null;
		}
		em.close();
		return result;
	}
	
	public static void removeForm(Form f)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Form toRem = em.find(Form.class, f.getID());
		em.remove(toRem);
		em.close();
	}
	
	/**
	 * A Method to remove an object from the database
	 * @param o the object to remove
	 */
	public static void removeAbsence(Absence o)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Absence toRem = em.find(Absence.class, o.getID());
		em.remove(toRem);
		em.close();
	}
	
	public static void removeTardy(Tardy o)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Tardy toRem = em.find(Tardy.class, o.getID());
		em.remove(toRem);
		em.close();
	}
	
	public static void removeReport(AttendanceReport o)
	{
		EntityManager em = EMFService.get().createEntityManager();
		AttendanceReport toRem = em.find(AttendanceReport.class, o.getID());
		em.remove(toRem);
		em.close();
	}
	
	public static void removeEvent(Event o)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Event toRem = em.find(Event.class, o.getId());
		em.remove(toRem);
		em.close();
	}
	
	public static void removeMessage(Message o)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Message toRem = em.find(Message.class, o.getID());
		em.remove(toRem);
		em.close();
	}
	
	public static void removeMyMessages(String netID)
	{
		List<Message> toRemove = getMyMessages(netID);
		for (Message m: toRemove)
		{
			removeMessage(m);
		}
	}
	
	public static void removeUser(User o)
	{
		//TODO make this remove all absences tardies and stuff for the student
		EntityManager em = EMFService.get().createEntityManager();
		User toRem = em.find(User.class, o.getID());
		em.remove(toRem);
		em.close();
	}
	
	public static void addAttendanceReport(AttendanceReport report)
	{
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(report);
		em.close();
	}
	
	public static List<AttendanceReport> getAllReports()
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from AttendanceReport a");
		List<AttendanceReport> results = (List<AttendanceReport>) q.getResultList();
		return results;
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
	
	public static boolean userExists(String netID)
	{
		if(getUser(netID) == null)
			return false;
		return true;
	}
	
	public static User getDirector()
	{	
		EntityManager em = EMFService.get().createEntityManager();
		String type = "Director";
		Query q = em.createQuery("select p from User p where p.type = :type");
		q.setParameter("type", type);
		
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
	
	public static boolean directorExists()
	{
		if(getDirector() == null)
			return false;
		return true;
	}
	
	public static User getStudentStaff()
	{	
		EntityManager em = EMFService.get().createEntityManager();
		String type = "TA";
		Query q = em.createQuery("select p from User p where p.type = :type");
		q.setParameter("type", type);
		
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
	
	public static boolean studentStaffExists()
	{
		if(getStudentStaff() == null)
			return false;
		return true;
	}
	
	public static ArrayList<String> listAllUsers() 
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from User m");
		List<User> people = (List<User>) q.getResultList();
		ArrayList<String> toRet = new ArrayList<String>();
		for (int i = 0; i < people.size(); i++) {
			User p = people.get(i);
			if (!p.getType().equalsIgnoreCase("Director")){
				if(p.getType().equalsIgnoreCase("TA"))
					toRet.add(p.toStringTA());
				else
					toRet.add(p.toString());
			}
		}
		return toRet;
	}

	/**
	 * Returns a list of all students' netIDs.
	 * @author Curtis Ullerich
	 * @date 4/9/12
	 * @return
	 */
	public static String[] listAllStudents() //TODO sort this alphabetically
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from User m");
		List<User> people = (List<User>) q.getResultList();
		String[] toRet = new String[people.size()];
		for (int i = 0; i < people.size(); i++) {
			User p = people.get(i);
			if (p.getType().equalsIgnoreCase("Student"))
				toRet[i] = p.getNetID();
		}
		return toRet;
	}

	public static List<User> getStudents() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from User t");
		List<User> users;
		List<User> ret = new ArrayList<User>();
		try
		{
			users = (List<User>) q.getResultList();
			for (User u : users) {
				if (u.getType().equals("Student")) {
					ret.add(u);
				}
			}
		}
		catch (NoResultException e)
		{
			users = null;//TODO shouldn't we just make this an empty list then?
		}
		return ret;
	}

	
	
	public static void addMessage(Message m) {
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(m);
		em.close();	
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
		Query q = em.createQuery("select a from Absence a where a.netID = :netID");
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
		return absences;
	}

	/**
	 * 
	 * 
	 * @author Curtis Ullerich
	 * @date 4/13/12
	 * @param id
	 * @return
	 */
	public static Event getEventByID(Long id) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Event a where a.id = :id");
		q.setParameter("id", id);
		Event event;
		try
		{
			event = (Event) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			event = null;
		}
		em.close();
		return event;
		
	}
	
	/**
	 * @author Curtis Ullerich
	 * @date 4/14/12
	 * @param id
	 * @return
	 */
	public static Message getMessageByID(Long id) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Message a where a.id = :id");
		q.setParameter("id", id);
		Message message;
		try
		{
			message = (Message) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			message = null;
		}
		em.close();
		return message;
	}
	
	
	/**
	 * 
	 * 
	 * @author Curtis Ullerich
	 * @date 4/13/12
	 * @param id
	 * @return
	 */
	public static User getUserByID(Long id) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from User a where a.id = :id");
		q.setParameter("id", id);
		User user;
		try
		{
			user = (User) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			user = null;
		}
		em.close();
		return user;
		
	}
	
	
	public static Absence getAbsenceByID(Long id)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Absence a where a.id = :id");
		q.setParameter("id", id);
		Absence absence;
		try
		{
			absence = (Absence) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			absence = null;
		}
		em.close();
		return absence;
	}
	
	public static Tardy getTardyByID(Long id)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from Tardy t where t.id = :id");
		q.setParameter("id", id);
		Tardy tardy;
		try
		{
			tardy = (Tardy) q.getSingleResult();
		}
		catch (NoResultException e)
		{
			tardy = null;
		}
		
		em.close();
		return tardy;
	}

	public static List<Tardy> getTardies(String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from Tardy t where t.netID = :netID");
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
		return tardies;
	}


	
	/**
	 * Returns a list of early check outs for the given user.
	 * 
	 * @author Curtis Ullerich
	 * @date 4/10/12
	 * @param netID
	 * @return
	 */
	public static List<EarlyCheckOut> getEarlyCheckOuts(String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from EarlyCheckOut t where t.netID = :netID");
		q.setParameter("netID", netID);
		List<EarlyCheckOut> ecos;
		try
		{
			ecos = (List<EarlyCheckOut>) q.getResultList();
		}
		catch (NoResultException e)
		{
			ecos = null;
		}
		return ecos;
	}

	
	public static List<Form> getForms(String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Form a where a.netID = :netID");
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
		return result;
	}
	
	public static List<Form> getForms(String type, String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select a from Form a where a.netID = :netID and a.type = :type");
		q.setParameter("netID", netID);
		q.setParameter("type", type);
		List<Form> result;
		try
		{
			result = (List<Form>) q.getResultList();
		}
		catch (NoResultException e)
		{
			result = null;
		}
		return result;
	}
	
	public static void addForm(Form form)
	{
		EntityManager em = EMFService.get().createEntityManager();
		em.persist(form);
		em.close();	
	}
	
	/**
	 * 
	 * Returns a list of all new (unread) Messages for this user.
	 * 
	 * @author Curtis Ullerich
	 * @date 4/12/12
	 * @param netID
	 * @return
	 */
	public static List<Message> getNewMessages(String netID) {
		EntityManager em = EMFService.get().createEntityManager();
		List<Message> result = new LinkedList<Message>();
		Query q = em.createQuery("select m from Message m where m.recipientNetID = :netID");
		q.setParameter("netID", netID);
		List<Message> messages = (List<Message>) q.getResultList();
		if (messages == null) System.err.println("MESSAGES WAS NULL");
		ArrayList<Message> toRemove = new ArrayList<Message>();
		for (Message m : messages) {
			if (m== null) {
				System.err.println("a message was null, bro");
			} else {
			if (m.readBy(netID)) {
				toRemove.add(m);
			}
			}
		}
		messages.removeAll(toRemove);

		return messages;
	}
	
	public static List<Message> getMyMessages(String netID)
	{
		EntityManager em = EMFService.get().createEntityManager();
		List<Message> result = new LinkedList<Message>();
		List<Message> toRet = new LinkedList<Message>();
		Query q = em.createQuery("select m from Message m");
		result = (List<Message>)q.getResultList();
		for (Message m : result)
		{
			if (m.getSenderNetID().equalsIgnoreCase(netID) || m.getRecipientNetID().equalsIgnoreCase(netID))
			{
				if (!toRet.contains(m))
					toRet.add(m);
			}
			for (String reader: m.getReaders())
			{
				if (reader.equalsIgnoreCase(netID))
				{
					if (!toRet.contains(m))
					{
						toRet.add(m);
						break;
					}
				}
			}
		}
		return toRet;
	}
	
	
	public static List<Message> getMessages(String[] messageIDs) {
		EntityManager em = EMFService.get().createEntityManager();
		List<Message> result = new LinkedList<Message>();
		if(messageIDs == null) {
			System.out.println("messageIDs was null");
		}
		for (int i = 0; i < messageIDs.length; i++)
		{
			if (messageIDs[i] != null && !messageIDs[i].equals("")) {
				Long id = Long.parseLong(messageIDs[i]);
				Query q = em.createQuery("select m from Message m where m.id = :id");
				q.setParameter("id", id);
		
				try
				{
					result.addAll((List<Message>) q.getResultList());
				}
				catch (NoResultException e)
				{
					result = null;
				}
				//em.close; <- always leave this open!
			}
		}
		return result;
	}
	
	public static void refreshMessage(Message m) {
		EntityManager em = EMFService.get().createEntityManager();
		Message databaseMessage = em.find(Message.class, m.getID());
		databaseMessage.copyAllFrom(m);
		em.persist(databaseMessage);
		em.close();
	}
	public static void refreshUser(User u) {
		EntityManager em = EMFService.get().createEntityManager();
		User databaseUser = em.find(User.class, u.getID());
		databaseUser.copyAllFrom(u);
		em.persist(databaseUser);
		em.close();
	}
	
	public static void refreshForm(Form f) {
		EntityManager em = EMFService.get().createEntityManager();
		Form databaseMessage = em.find(Form.class, f.getID());
		databaseMessage.copyAllFrom(f);
		em.persist(databaseMessage);
		em.close();
	}

	
	/**
	 * Returns a List of all Users in the database.
	 * @author Curtis Ullerich
	 * @return
	 */
	public static List<User> getAllUsers() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from User m");
		List<User> users = (List<User>) q.getResultList();
		return users;
	}

	
	/**
	 * Returns a List of all Events in the database.
	 * @author Curtis Ullerich
	 * @return
	 */
	public static List<Event> getAllEvents() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from Event m");
		List<Event> events = (List<Event>) q.getResultList();
			
		return events;
	}
	
	/**
	 * Returns a List of all Tardies in the database.
	 * @author Curtis Ullerich
	 * @return
	 */
	public static List<Tardy> getAllTardies() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from Tardy m");
		List<Tardy> tardies = (List<Tardy>) q.getResultList();
			
		return tardies;
	}
	
	/**
	 * Returns a List of all Absences in the database.
	 * @author Curtis Ullerich
	 * @return
	 */
	public static List<Absence> getAllAbsences() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from Absence m");
		List<Absence> absences = (List<Absence>) q.getResultList();
			
		return absences;
	}

	/**
	 * Returns a List of all EarlyCheckOuts in the database.
	 * @author Curtis Ullerich
	 * @return
	 */
	public static List<EarlyCheckOut> getAllEarlyCheckOuts() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from EarlyCheckOut m");
		List<EarlyCheckOut> eocs = (List<EarlyCheckOut>) q.getResultList();
			
		return eocs;
	}
		
	/**
	 * Returns a List of all Forms in the database for this user.
	 * @author Curtis Ullerich
	 * @return
	 */
	public static List<Form> getAllForms() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select m from Form m");
		List<Form> forms = (List<Form>) q.getResultList();
			
		return forms;
	}
	
	public static List<Message> getAllMessages()
	{
		EntityManager em = EMFService.get().createEntityManager(); Query q = em.createQuery("select m from Message m"); List<Message> messages = (List<Message>) q.getResultList(); return messages;
	}
	//TODO same for all Forms
	
	
//	public static File getFile(String attachedFile) {
//		return null;
//	}
//
//	public static String addFile(File attachedFile) {
//		return null;
//	}
	
}