package comparators;

import java.util.Comparator;

import forms.Form;

public class FormTimeComp implements Comparator<Form>{

	//sorts by date
	@Override
	public int compare(Form o1, Form o2) {
		
		if((o1.getStartTime().compareTo(o2.getStartTime()) == 0))
		{
			FormTypeComp formType = new FormTypeComp();
			//if have the same date, sort by netID
			if(formType.compare(o1, o2)==0)
			{
				return new FormNetIDComp().compare(o1, o2);
			}
			return formType.compare(o1, o2);
		}
		else
			return o1.getStartTime().compareTo(o2.getStartTime());
	}

}
