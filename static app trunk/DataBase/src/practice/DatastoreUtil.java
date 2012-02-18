package practice;

import java.util.List;
import java.util.logging.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.search.Util;

public class DatastoreUtil 
{
	private static final Logger logger = Logger.getLogger(DatastoreUtil.class.getCanonicalName());
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	//This method may need to take in all the params of the person
	//constructor so we can use this method with text boxes
	public static void addPerson(Person toAdd)
	{
		//Checks to see if this person is already in the data store
		Key key = KeyFactory.createKey("Person", toAdd.getNetID());
		Entity person;
		try 
		{	  
		  	  person = datastore.get(key);
		} 
		catch (EntityNotFoundException e) 
		{
		  	  person = null;
		}
		//If the person wasn't found in the database
		if (person == null)
		{
			person = new Entity("Person", toAdd.getNetID());
			//Can add other properties with
			//person.setProperty("name", something else);
		}
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
	
	public static void addList(List<Person> list)
	{
		
	}
	
	public static List<Person> getAll()
	{
		//May need to add all the properties to the Entity so create a 
		//full person object to add to the list
		return null;
	}
}
