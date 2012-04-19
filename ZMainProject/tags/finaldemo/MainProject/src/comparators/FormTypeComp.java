package comparators;

import java.util.Comparator;

import forms.Form;

public class FormTypeComp implements Comparator<Form>{

	//sorts lexicographically 
	@Override
	public int compare(Form o1, Form o2) {
		
		if((o1.getType().equalsIgnoreCase(o2.getType())))
			return new FormTimeComp().compare(o1, o2);
		else
			return o1.getType().compareTo(o2.getType());
	}

}
