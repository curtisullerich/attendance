<!-- hide script from old browsers

/**
 * Common code for the Marching Band Attendance Main App
 * 
 * @author Todd Wegter
 * 
 * Latest Rev: 3-23-12
 */

/**
 * Stores an entry with the standard key: prepend firstname lastname netID date
 * startTime endTime
 * 
 * @author Todd Wegter
 */
function storeEntry(prepend, firstname, lastname, netID, date, startTime, endTime, rank, entry){
	var key = prepend+" "+firstname+" "+lastname+" "+netID+" "+date+" "+startTime+" "+endTime+" "+rank;
	localStorage.setItem(key,entry);
}

/**
 * Returns today's date in format YYYY-MM-DD where number less than 10 are
 * preceeded by 0
 * 
 * @return Date in specified format
 * 
 * @author Curtis Ullerich, Todd Wegter
 */
function dateToday() {
	var d = new Date();
	var month = d.getMonth()+1;
	var date = d.getDate();
	// add 0 for sortability
	if(parseInt(month,10)<10)
		month = "0"+month;
	if(parseInt(date,10)<10)
		date = "0"+date;
	return d.getFullYear()+"-"+month+"-"+date;
}


//end hiding script from old browsers -->