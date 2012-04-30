package comparators;

import java.util.Comparator;

import forms.Form;

public class FormStatusComp implements Comparator<Form> {

	//sorts by pending/unapproved/approved, then by date (larger date first)
	@Override
	public int compare(Form o1, Form o2) {
		return 0; //TODO if we need this, we should implement it
//		if ((o1.getStatus().equals(o2.getStatus()) {
//			
//		} else if (o1.getStatus()) {
//			
//			
//		} else if () {
//			
//		}
//		
//		
//		if((o1.isApproved() && o2.isApproved()) || (!o1.isApproved() && !o2.isApproved()))
//			return new FormTimeComp().compare(o1, o2);
//		else
//			return o1.isApproved() ? -1 : 1;
	}

}
