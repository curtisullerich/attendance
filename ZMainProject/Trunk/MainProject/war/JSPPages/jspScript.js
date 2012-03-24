<!-- hide script from old browsers

/**
 * Common code for the Marching Band Attendance Main App
 * 
 * @author Todd Wegter
 * 
 * Latest Rev: 3-23-12
 */

//debug enable thingy
var loginDebug = true;
var debug = true;

/**
 * Stores an entry with the standard key: prepend firstname lastname netID date startTime endTime rank
 * Pipes "|" should be used to denote a blank field
 * 
 * @param prepend
 * 			- string which denotes the type of entry
 * @param firstname
 * 			- the entry's first name
 * @param lastname
 * 			- the entry's last name
 * @param netID
 * 			- the entry's netID
 * @param date
 * 			- the date of the entry
 * @param startTime
 * 			- the start time of the entry
 * @param endTime
 * 			- the end time of the entry
 * @param rank
 * 			- the rank of the entry
 * @param entry
 * 			- the value to be stored (this is primarily used for printing the entry)
 * @returns true if successful
 * @author Todd Wegter
 * @date 1-31-12
 */
function storeEntry(prepend, firstname, lastname, netID, date, startTime, endTime, rank, entry){
	var key = prepend+" "+firstname+" "+lastname+" "+netID+" "+date+" "+startTime+" "+endTime+" "+rank;
	localStorage.setItem(key,entry);
	return true;
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

/**
 * 
 * @returns
 */
function validateTA(){
	// get TA checkin
	if(loginDebug)
		return true;
	
	var name=prompt("Please enter your TA netID:","TA netID");
	
	if (name!=null && name!="")
	{
	   var password=prompt("Please enter your TA password:","password");
			
		if (password!=null && password != "")
		{
			for (var i = 0; i < localStorage.length; i++) {
				var key = localStorage.key(i);

				//if this is the right netid, then hash the plaintext password and check it
				if (keyDelimiter(key,"netID") == name) {
					if (localStorage[key] == Sha1.hash(password,true)) {
						return true;
					} 
				} 
			}
			alert("Invalid netID or password");
			parent.location="FieldAppMain.html";
			return false;
		}
		else
		{
			parent.location="FieldAppMain.html";
			return false;
		}
	}
	else
	{
		parent.location="FieldAppMain.html";
		return false;
	}
}

/**
 * This function delineates the specified standard format key into an array
 * (index 0=prepend, 1=firstname, 2=lastname, 3=netID, 4=date, 5=startTime,
 * 6=endTime, 7=rank). It then returns the desired component of the key
 * 
 * The options for return subcomponent of the key are "date", "netID",
 * "lastname", "firstname", "dateandtime", "starttime", "endtime", and "rank". The entire
 * key is returned if the delimitee does not match one of the specified options
 * 
 * @param key -
 *            the key to be split
 * @param delimitee -
 *            the desired component of the key (i.e. date, etc.)
 * 
 * @return The desried specific component of the key (i.e. 2012-02-18, etc.)
 * 
 * @author Todd Wegter
 */
function keyDelimiter(key,delimitee){
	if (key == null || delimitee == null) {
	    return null;
	}
	
	var keyArray = new Array();
	
	keyArray = key.split(" ");
	
	if (delimitee=="key") {
	  return key;// so we can access a subsection of the full localStorage as
				 // key-value pairs, in tact
	}
	
	if(delimitee=="date"){
		return keyArray[4];
	}
	else if(delimitee=="netID"){
		return keyArray[3];
	}
	else if(delimitee=="lastname"){
		return keyArray[2];
	}
	else if(delimitee=="firstname"){
		return keyArray[1];
	}
	else if(delimitee=="dateandtime"){
		return keyArray[4]+keyArray[5]+keyArray[6];
	}
	else if(delimitee=="starttime"){
		return keyArray[5];
	}
	else if(delimitee=="endtime"){
		return keyArray[6];
	}
	else if (delimitee=="name"){
	    return keyArray[1] + " " + keyArray[2];
	}
	else if (delimitee=="rank"){
	    return keyArray[7];
	}
	return key;
}

//end hiding script from old browsers -->