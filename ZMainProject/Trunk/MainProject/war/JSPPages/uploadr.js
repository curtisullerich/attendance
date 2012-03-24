<!-- hide script from old browsers

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

// end hiding script from old browsers -->
