package edu.iastate.music.marching.attendance.beans;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.model.store.User;

public class NavigationBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -875480508816984630L;

	private static Map<String, Object> rootMapSingleton = null;

	private Map<String, Object> map;

	private User user;

	private NavigationBean(Map<String, Object> yamlRoot, User user) {
		this.map = yamlRoot;
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	public static NavigationBean getInstance(User currentUser) {

		Map<String, Object> map = rootMapSingleton;

		if (map == null) {
			YamlReader reader;
			try {
				reader = new YamlReader(new FileReader(
						"WEB-INF/navigation.yaml"));

				Object root = reader.read();
				map = (Map<String, Object>) root;

			} catch (YamlException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return new NavigationBean(map, currentUser);
	}

	public String getHref() {
		if (this.map.containsKey("href"))
			return this.map.get("href").toString();
		else
			return null;
	}

	public String getText() {
		if (this.map.containsKey("text"))
			return this.map.get("text").toString();
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public List<NavigationBean> getLinks() {
		ArrayList<NavigationBean> links = new ArrayList<NavigationBean>();

		if (!this.map.containsKey("links"))
			return links;

		Object object = this.map.get("links");

		if (!(object instanceof ArrayList))
			return links;

		ArrayList<Object> yamlList = (ArrayList<Object>) object;

		for (Object o : yamlList) {
			if (o instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) o;

				if (!map.containsKey("auth") || checkAuth(map.get("auth")))
					links.add(new NavigationBean(map, this.user));

			}
		}

		return links;
	}

	private boolean checkAuth(Object authDescriptor) {

		if (authDescriptor instanceof ArrayList) {
			@SuppressWarnings("unchecked")
			ArrayList<String> auths = (ArrayList<String>) authDescriptor;

			for (String auth : auths) {
				if (this.user != null
						&& this.user.getType().toString().toLowerCase()
								.equals(auth.toLowerCase()))
					return true;
				else if ("Admin".equals(auth)) {
					UserService userService = UserServiceFactory
							.getUserService();
					if (userService.isUserLoggedIn()
							&& userService.isUserAdmin())
						return true;
				}
			}

			return false;
		}

		return false;
	}

}
