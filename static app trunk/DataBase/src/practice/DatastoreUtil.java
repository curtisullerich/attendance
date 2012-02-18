package practice;

import java.util.List;
import java.util.logging.*;

import com.google.appengine.api.datastore.*;

public class DatastoreUtil 
{
	private static final Logger logger = Logger.getLogger(DatastoreUtil.class.getCanonicalName());
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	//This method may need to take in all the params of the person
	//constructor so we can use this method with text boxes
	public static void addPerson(Person toAdd)
	{
		//Checks to see if this person is already in the data store
		Entity person = createEntityFromPerson(toAdd);
		logger.log(Level.INFO, "Saving Person");
		datastore.put(person);
	}
	
	public static Entity findEntity(String netID)
	{
		logger.log(Level.INFO, "Searching for the Person");
		//Makes the key for the netId you are looking for
		Key key = KeyFactory.createKey("Person", netID);
		try
		{
			return datastore.get(key);
		}
		catch (EntityNotFoundException e)
		{
			return null;
		}
	}
	
	public static void deleteEntity(String netId)
	{	
		try
		{
			datastore.delete(KeyFactory.createKey("Person", netId));
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Entity is not in the datastore");
		}
	}
	
	public static void addList(List<Person> list)
	{
		
	}
	
	public static List<Person> getAll()
	{
		//May need to add all the properties to the Entity so create a 
		//full person object to add to the list
		return null;
	}
	
	//Returns null if that person is already there
	private static Entity createEntityFromPerson(Person person)
	{
		Entity entity = findEntity(person.getNetID());
		//Person is found
		if (entity != null)
		{
			return null;
		}
		
		//Person is not found so add a new one
		entity = new Entity("Person", person.getNetID());
		entity.setProperty("netId", person.getNetID());
		entity.setProperty("firstName", person.getFirstName());
		entity.setProperty("lastName", person.getLastName()); //lastName
		entity.setProperty("password", person.getPassword()); //password
		entity.setProperty("major", person.getMajor()); //major
		entity.setProperty("advisor", person.getAdvisor()); //advisor
		entity.setProperty("position", person.getPosition()); //position
		
		return entity;
	}
}
