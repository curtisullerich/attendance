package comparators;

import java.util.Comparator;

import forms.Form;

public class FormTimeComp implements Comparator<Form>{

	//sorts by date
	@Override
	public int compare(Form o1, Form o2) {
		
		if((o1.getStartTime().compareTo(o2.getStartTime()) == 0))
			return new FormTypeComp().compare(o1, o2);
		else
			return o1.getStartTime().compareTo(o2.getStartTime());
	}

}
