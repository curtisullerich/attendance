package comparators;

import java.util.Comparator;

import forms.Form;

public class FormNetIDComp implements Comparator<Form>{

	@Override
	public int compare(Form o1, Form o2) {
		return o1.getNetID().compareTo(o2.getNetID());
	}

}
