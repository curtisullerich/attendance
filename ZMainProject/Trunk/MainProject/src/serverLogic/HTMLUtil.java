package serverLogic;

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
	
	
}
