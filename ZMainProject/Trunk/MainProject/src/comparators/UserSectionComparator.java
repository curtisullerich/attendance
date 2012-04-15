package comparators;

import java.util.Comparator;

import people.User;

public class UserSectionComparator implements Comparator<User> {

	// sorts by last name, first name, section
	@Override
	public int compare(User o1, User o2) {
		if ((o1.getSection().compareTo(o2.getSection()) == 0)) {
			if ((o1.getLastName().compareTo(o2.getLastName()) == 0)) {
				return (o1.getFirstName().compareTo(o2.getFirstName()));
			} else {
				return (o1.getLastName().compareTo(o2.getLastName()));
			}
		} else {
			return (o1.getSection().compareTo(o2.getSection()));
		}
	}

}
