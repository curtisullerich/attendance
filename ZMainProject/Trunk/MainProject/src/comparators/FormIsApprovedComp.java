package comparators;

import java.util.Comparator;

import forms.Form;

public class FormIsApprovedComp implements Comparator<Form> {

	//sorts by unapproved/approved, then by date (larger date first)
	@Override
	public int compare(Form o1, Form o2) {
//		if((o1.isApproved() && o2.isApproved()) || (!o1.isApproved() && !o2.isApproved()))
//			return new FormTimeComp().compare(o1, o2);
//		else
//			return o1.isApproved() ? -1 : 1;
		return 0;//if we need this, then implement it
	}

}
