package comparators;

import java.util.Comparator;

import people.User;

public class UserComparator implements Comparator<User> {

	// sorts by last name, first name, section
	@Override
	public int compare(User o1, User o2) {
		if ((o1.getLastName().toUpperCase().compareTo(o2.getLastName().toUpperCase()) == 0)) {
			if ((o1.getFirstName().toUpperCase().compareTo(o2.getFirstName().toUpperCase()) == 0)) {
				return (o1.getSection().toUpperCase().compareTo(o2.getSection().toUpperCase()));
			} else {
				return o1.getFirstName().toUpperCase().compareTo(o2.getFirstName().toUpperCase());
			}
		} else {
			return o1.getLastName().toUpperCase().compareTo(o2.getLastName().toUpperCase());
		}
	}

}
