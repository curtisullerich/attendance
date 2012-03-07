package practice;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;

public class DatastoreUtil 
{
	protected static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	
	//This method may need to take in all the params of the person
	//constructor so we can use this method with text boxes
	public static void addPerson(Person toAdd)
	{
		//Checks to see if this person is already in the data store
		Entity person = createEntityFromPerson(toAdd);
		try
		{
			datastore.put(person);
		}
		catch (NullPointerException e)
		{
			//CRITICAL FAILURE!
		}
	}
	
	//Can change this to return a person by calling the createPersonFromEntity
	public static Entity findEntity(String netID)
	{
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
	
	public static void deleteAll()
	{
		Query q = new Query("Person");
		List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(10)/*FetchOptions.Builder.withDefaults()*/);
		//Get each entity and remove it
		for (Entity result: results)
		{
			datastore.delete(result.getKey());
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
		for (int i = 0; i < list.size(); i++)
		{
			addPerson(list.get(i));
		}
	}
	
	//Figure out why this isn't getting everyone like it's supposed to 
	public static List<Person> getAll()
	{
		List<Person> toReturn = new LinkedList<Person>();
		//Creates a query of type Person
		Query q = new Query("Person");
		List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(10)/*FetchOptions.Builder.withDefaults()*/);
		//Get each entity and convert it to a person and add it to the returned list
		for (Entity result: results)
		{
			System.out.println(createPersonFromEntity(result));
			toReturn.add(createPersonFromEntity(result));
		}
		
		return toReturn;
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
		entity.setProperty("firstName", person.getFirstName()); //firstName
		entity.setProperty("lastName", person.getLastName()); //lastName
		entity.setProperty("password", person.getPassword()); //password
		entity.setProperty("major", person.getMajor()); //major
		entity.setProperty("advisor", person.getAdvisor()); //advisor
		entity.setProperty("position", person.getPosition()); //position
		
		return entity;
	}
	
	private static Person createPersonFromEntity(Entity ent)
	{
		String netId = (String) ent.getProperty("netId");
		String firstName = (String) ent.getProperty("firstName");
		String lastName = (String) ent.getProperty("lastName");
		String password = (String) ent.getProperty("password");
		String major = (String) ent.getProperty("major");
		String advisor = (String) ent.getProperty("advisor");
		String position = (String) ent.getProperty("position");
		
		
		Person toRet = new Person(netId, firstName, lastName, password, major, advisor, position);
		return toRet;
		
	}
}
