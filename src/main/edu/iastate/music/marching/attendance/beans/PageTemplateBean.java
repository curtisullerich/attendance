package edu.iastate.music.marching.attendance.beans;

import javax.servlet.http.HttpServletRequest;

import edu.iastate.music.marching.attendance.model.AppData;

public class PageTemplateBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1624959847216686921L;

	/**
	 * Attribute name used when applying this bean to a request as an attribute
	 * in method apply
	 */
	public static final String ATTRIBUTE_NAME = "pagetemplate";

	private static final String SESSION_ATTRIBUTE_SHOWMOBILESITE = "PageTemplateBean_showmobilesite";

	private String title;

	private String mJSPPath;

	private boolean mobileBrowser = false;

	private boolean mobileSite = false;
	
	private AppData mAppData;

	/**
	 * No-arg constructor always for a bean
	 * @param appData 
	 * 
	 * @param mJSPPath
	 */
	public PageTemplateBean(String jsp_path, AppData appData) {
		mJSPPath = jsp_path;
		mAppData = appData;
	}

	public boolean isMobileDevice() {
		return mobileBrowser;
	}
	
	public boolean isMobileSite() {
		return mobileSite;
	}

	public String getJspath() {
		return mJSPPath;
	}
	
	public String getTimeZoneID() {
		return mAppData.getTimeZone().getID();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void apply(HttpServletRequest request) {

		// Check if mobile site view is explicitly set
		if ("true".equals(request.getParameter("mobile")))
			request.getSession().setAttribute(SESSION_ATTRIBUTE_SHOWMOBILESITE,
					true);
		else if ("false".equals(request.getParameter("mobile")))
			request.getSession().setAttribute(SESSION_ATTRIBUTE_SHOWMOBILESITE,
					false);

		// Figure out if the end user is on a mobile browser
		String ua = request.getHeader("User-Agent").toLowerCase();
		if (ua.matches(".*(android.+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")
				|| ua.substring(0, 4)
						.matches(
								"1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-")) {
			this.mobileBrowser = true;

			// If client has not set a preference for the mobile site, default
			// to mobile since they are on a mobile device
			if (null == request.getSession().getAttribute(
					SESSION_ATTRIBUTE_SHOWMOBILESITE))
				request.getSession().setAttribute(
						SESSION_ATTRIBUTE_SHOWMOBILESITE, true);
		}

		// Based on the now maybe saved preference, decide whether to show the mobile site
		if (Boolean.TRUE.equals(request.getSession().getAttribute(
				SESSION_ATTRIBUTE_SHOWMOBILESITE)))
			this.mobileSite = true;
		else
			this.mobileSite = false;

		if (request != null)
			request.setAttribute(ATTRIBUTE_NAME, this);
	}
}
