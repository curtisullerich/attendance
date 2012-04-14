package serverLogic;

import attendance.Absence;
import attendance.Tardy;

public class HTMLUtil {
	
	/**
	 * Returns a row of an HTML table.
	 * @author Curtis Ullerich
	 * @date 4/9/12
	 * @param entries
	 * @return
	 */
	public static String makeTableRow(String[] entries) {
		String ret = "<tr>";
		for (int i = 0; i < entries.length; i++) {
			 ret+= "<td>"+entries[i]+"</td>";
		}
		ret += "</tr>";
		return ret;
	}
	
	public static String messagePageHeader(String parentType, long parentID) {
		
		String ret = "";
		if (parentType.equals("Tardy")) {
			Tardy tardy = DatabaseUtil.getTardyByID(parentID);
			ret += "<table><tr><td></td><td><p><b>Tardy</b></td></tr>"
					+"<tr><td>Student: </td><td>"+tardy.getNetID()+"</td></tr>"
					+"<tr><td>Type: </td><td>"+tardy.getType()+"</td></tr>"
					+"<tr><td>Status: </td><td>"+tardy.getStatus()+"</td></tr>"
					+"<tr><td>Time: </td><td>"+tardy.getTime().get12Format()+"</td></tr>"
					+"<tr><td>Date: </td><td>"+tardy.getTime().getDate().toString()+"</td></tr>"
					+"</table>";

		} else if (parentType.equals("Absence")) {
			Absence absence = DatabaseUtil.getAbsenceByID(parentID);
			ret += "<table><tr><td></td><td><p><b>Absence</b><br/></td></tr>"
					+"<tr><td>Student:</td><td> "+absence.getNetID()+"</td></tr>"
					+"<tr><td>Type:</td><td> "+absence.getType()+"</td></tr>"
					+"<tr><td>Status:</td><td> "+absence.getStatus()+"</td></tr>"
					+"<tr><td>Start time:</td><td> "+absence.getStartTime().get24Format()+"</td></tr>"
					+"<tr><td>End time:</td><td> "+absence.getEndTime().get24Format()+"</td></tr>"
					+"<tr><td>Date:</td><td> "+absence.getStartTime().getDate().toString()+"</td></tr>"
					+"</table>";
			
		} else if (parentType.equals("EarlyCheckOut")) {
//			EarlyCheckOut eoc = DatabaseUtil.getEarlyCheckOutByID(parentID);
			
		} else if (parentType.equals("FormA")) {
			//TODO
		} else if (parentType.equals("FormB")) {
			
		} else if (parentType.equals("FormC")) {
			
		} else if (parentType.equals("FormD")) {
			
		} else {
			System.err.println("And, we have a type of message that we didn't account for. In HTMLUtil.messagePageHeader.");
		}

		
		
		return ret;
		
	}
}
