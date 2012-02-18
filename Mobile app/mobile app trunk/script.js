<!-- hide script from old browsers

/**
* Common code for the Marching Band Attendance Mobile App
*
* @author Todd Wegter, Curtis Ullerich
*
* Latest Rev: 2-18-12
*
* standard key for localStorage: prepend firstname lastname netID date startTime endTime
**/

var absentPrepend = "absentStudent";
var rehearsalPrepend = "storedEvent";
var tardyPrepend = "tardyStudent";


/**
* Determines if a substring (sub) is contained within a string (str)
*
* @author Curtis Ullerich
**/
function stringContains(sub, str) {            
	return str.indexOf(sub) !== -1;
}


/**
* Returns today's date in format YYYY-MM-DD where number less than 10 are preceeded by 0
*
* @return Date in specified format
*
* @author Curtis Ullerich, Todd Wegter
**/
function dateToday() {
	var d = new Date();
	var month = d.getMonth()+1;
	var date = d.getDate();
	//add 0 for sortability
	if(parseInt(month)<10)
		month = "0"+month;
	if(parseInt(date)<10)
		date = "0"+date;
	return d.getFullYear()+"-"+month+"-"+date;
}

/**
* Returns the current time in 24 hour format: HHMM
*
* @return The time in the specified format
*
* @author Curtis Ullerich, Todd Wegter
**/
function timeNow() {
	var d = new Date();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	//add 0 for sortability
	if(parseInt(hours)<10)
		hours = "0"+hours;
	if(parseInt(minutes)<10)
		minutes = "0"+minutes;
	return hours+minutes;
} 

/**
* This functions returns the current time in 12 hour format: HH:MM am/pm
*
* @return The time in the specified format
*
* @author Todd Wegter
**/
function twelveHourTimeNow(){
	var d = new Date()
	var hours = d.getHours();
	var ampm;
	//decide if am or pm
	if((parseInt(hours)) < 12)
		ampm = "am";
	else
		ampm = "pm";
	//use 12 for 0, else just mod the time by 12 to get the hours
	if(parseInt(hours) == 0)
		hours = 12;
	else
		hours = parseInt(hours)%12;
	var minutes = d.getMinutes();
	//add 0 for sortability
	if(parseInt(minutes)<10)
		minutes = "0"+minutes;
	return hours+":"+minutes+" "+ampm;
}

/**
* Returns true if localStorage contains an event of the specified type, date, and start and endtimes
* (eventType is stored in the key in the firstname place)
*
* @param eventType - "rehearsal" or "performance"
* @param date - specified date
* @param starttime - specified start time of event
* @param endtime - specified end time of event
*
* @return true if the specified event exists
*
* @author Todd Wegter
**/
function localStorageContainsEvent(eventType, date, starttime, endtime){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(eventPrepend,key) && keyDelimiter(key, "firstname")==eventType && keyDelimiter(key, "starttime")==starttime && keyDelimiter(key, "endtime")==endtime && keyDelimiter(key, "date")==date)
			return true;
	}
	return false;
}

//will return name assoc. with netID from database
function getFirstName(netID){
	return "firstname";
}

//will return name assoc. with netID from database
function getLastName(netID){
	return "lastname";
}

/**
* Stores an entry with the standard key: prepend firstname lastname netID date startTime endTime
*
* @author Todd Wegter
**/
function storeEntry(prepend, firstname, lastname, netID, date, startTime, endTime, entry){
	var key = prepend+" "+firstname+" "+lastname+" "+netID+" "+date+" "+startTime+" "+endTime;
	localStorage.setItem(key,entry);
}

/**
* Clears all items in localStorage which have the specified prepend
*
* @author Todd Wegter
**/
function clearLocalStoragewithPrepend(prepend){
	//loop through whole localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		//get the key
		var key = localStorage.key(i);
		//remove item if the key has the defined prepend
		if(stringContains(prepend,key))
		{
			localStorage.removeItem(key);
			l--; //accounts for removal of entry in length of localStorage
			i--; //accounts for removal of entry in position of search
		}
	}
}

/**
* This function delineates the specified standard format key into an array 
* (index 0=prepend, 1=firstname, 2=lastname, 3=netID, 4=date, 5=startTime, 6=endTime).
* It then returns the desired component of the key
*
* The options for return subcomponent of the key are "date", "netID", "lastname", "firstname", "dateandtime", "starttime", and "endtime"
* The entire key is returned if the delimitee does not match one of the specified options
*
* @param key - the key to be split
* @param delimitee - the desired component of the key (i.e. date, etc.)
*
* @return The desried specific component of the key (i.e. 2012-02-18, etc.)
*
* @author Todd Wegter
**/
function keyDelimiter(key,delimitee){
	var keyArray = new Array();
	keyArray = key.split(" ");
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
	return key;
}

/**
* Stores the specified contents of the localStorage to an array (useful for sorting etc.)
* Items stored have keys which contain the specified prepend and searchKey.
*
* @param prepend - the prepend for the key of the elements to be stored
* @param searchKey - the other component of the key for the elements to be stored
* @param delimitee - the desired component of the key to be stored in the array
* 
* @return A 2D array: each row has two entries -> [i][0] is the desired component of the key
*											   -> [i][1] is the value associated with the un-cut key
*
* This allows the array to be sorted using array.sort() -> sorts by the 0th column, then the 1th column (for duplicates in 0th column)
*
* @author Todd Wegter
**/
function storeToArray(prepend, searchKey, delimitee){
	var index = 0;
	var array = new Array();
	//search through all localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		//if the key contains the prepend and the searchKey, store it and the desired part of the key into the array
		if(stringContains(prepend,key) && stringContains(searchKey,key))
		{
			array[index++] = [keyDelimiter(key,delimitee),localStorage.getItem(key)];
		}
	}
	return array;
}

/**
* Takes a 2D array created with storeToArray(prepend, searchKey, delimitee) and compiles its values (not the keys) to a string
*
* @param array - the array to stringify
*
* @return The string of the array
**/
function storageArrayToString(array){
	var returnString = "";
	for(var i = 0, l = array.length; i<l; i++)
	{
		//add value for each row to the string
		var value = array[i][1];
		returnString += value + "</br>";
	}
	return returnString;
}

//end hiding script from old browsers -->
