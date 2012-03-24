<!-- hide script from old browsers

/**
 * Common code for the Marching Band Attendance Mobile App
 * 
 * @author Todd Wegter, Curtis Ullerich
 * 
 * Latest Rev: 3-3-12
 * 
 * standard key for localStorage: prepend firstname lastname netID date startTime endTime rank
 * rank is a 6 digit number with leading 0s
 */
var absentPrependPerformance = "absentStudentPerformance";
var absentPrependRehearsal = "absentStudentRehearsal";
var rehearsalPrepend = "storedRehearsal";
var performancePrepend = "storedPerformance";
var tardyPrepend = "tardyStudent";
var studentPrepend = "studentRecord";
var loginPrepend = "storedLogin";

//debug enable thingy
var loginDebug = false;
var debug = true;


/**
 * Makes browser go back (same as hitting browser's back button
 * @author Todd Wegter
 * @date 3/22/12
 */
function goBack()
{
	window.history.back()
}

/**
 * Determines if a substring (sub) is contained within a string (str)
 * 
 * @author Curtis Ullerich
 */
function stringContains(sub, str) {            
	return str.indexOf(sub) !== -1;
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
 * Returns the current time in 24 hour format: HHMM
 * 
 * @return The time in the specified format
 * 
 * @author Curtis Ullerich, Todd Wegter
 */
function timeNow() {
	var d = new Date();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	// add 0 for sortability
	if(parseInt(hours,10)<10)
		hours = "0"+hours;
	if(parseInt(minutes,10)<10)
		minutes = "0"+minutes;
	return hours+minutes;
} 

/**
 * This functions returns the current time in 12 hour format: HH:MM am/pm
 * 
 * @return The time in the specified format
 * 
 * @author Todd Wegter
 */
function twelveHourTimeNow(){
	var d = new Date()
	var hours = d.getHours();
	var ampm;
	// decide if am or pm
	if((parseInt(hours,10)) < 12)
		ampm = "am";
	else
		ampm = "pm";
	// use 12 for 0, else just mod the time by 12 to get the hours
	if(parseInt(hours,10) == 0 || parseInt(hours,10) == 12)
		hours = 12;
	else
		hours = parseInt(hours,10)%12;
	var minutes = d.getMinutes();
	// add 0 for sortability
	if(parseInt(minutes,10)<10)
		minutes = "0"+minutes;
	return hours+":"+minutes+" "+ampm;
}

/**
 * Returns true if localStorage contains an event of the specified type, date,
 * and start and endtimes (eventType is stored in the key in the firstname
 * place)
 * 
 * @param eventType -
 *            "rehearsal" or "performance"
 * @param date -
 *            specified date
 * @param starttime -
 *            specified start time of event
 * @param endtime -
 *            specified end time of event
 * 
 * @return true if the specified event exists
 * 
 * @author Todd Wegter
 */
function localStorageContainsEvent(eventType, date, starttime, endtime){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if((stringContains(rehearsalPrepend,key) || stringContains(performancePrepend,key)) && keyDelimiter(key, "firstname")==eventType && keyDelimiter(key, "starttime")==starttime && keyDelimiter(key, "endtime")==endtime && keyDelimiter(key, "date")==date)
			return true;
	}
	return false;
}

/**
 * Returns true if localStorage contains a studen with the specified netID
 *
 * @param netID
 *			- netID of desired student
 * 
 * @return true if the specified student netID
 * 
 * @author Todd Wegter
 */
function localStorageContainsStudent(netID){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(studentPrepend, key) && keyDelimiter(key, "netID")==netID)
			return true;
	}
	return false;
}


/**
 * Will return first name associated with the given netID from the localStorage
 * 
 * @param the netID to which the desired name corresponds
 * @return the first name associated with the netID, null if netID doesn't exist
 * @author Todd Wegter
 * @date 3/9/12
 */
function getFirstName(netID){
	var key = returnFirstLocalKey(studentPrepend, netID)
	if(key != null)
		var firstName = keyDelimiter(key, "firstname");
	return firstName;
}

/**
 * Will return last name associated with the given netID from the localStorage
 * TODO
 * @param the netID to which the desired name corresponds
 * @return the last name associated with the netID, null if netID doesn't exist
 * @author Todd Wegter
 * @date 3/9/12
 */
function getLastName(netID){
	var key = returnFirstLocalKey(studentPrepend, netID)
	if(key != null)
		var lastName = keyDelimiter(key, "lastname");
	return lastName;
}

/**
 * Will return the rank associated with the given netID from the localStorage
 * 
 * @param the netID to which the desired rank corresponds
 * @return the rank associated with the netID specified, null if netID doesn't exist
 * @author Todd Wegter
 * @date 3/9/12
 */
function getRank(netID){
	var key = returnFirstLocalKey(studentPrepend, netID)
	if(key != null)
		var rank = keyDelimiter(key, "rank");
	return rank;
}


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
 * Removes an entry with the standard key: prepend firstname lastname netID date
 * startTime endTime
 * 
 * @author Curtis Ullerich
 */
function removeEntry(prepend, firstname, lastname, netID, date, startTime, endTime, rank){
	var key = prepend+" "+firstname+" "+lastname+" "+netID+" "+date+" "+startTime+" "+endTime+" "+rank;
    if (localStorage.getItem(key) == null) {
        return false;
    }   
	localStorage.removeItem(key);
	return true;
}


/**
 * Removes all items from localStorage that have the given prepend. TODO test
 * this.
 * 
 * @author CurtisUllerich
 * @date 3/3/12
 * @param the string prepend to remove
 */
function removeByPrepend(prepend) {
    var array = storeToArray(prepend, " ", "key");
    for (var i = 0; i < array.length; i++) {
        localStorage.removeItem(array[i][0]);
    }    
}



/**
 * Clears all items in localStorage which have the specified prepend
 * 
 * @author Todd Wegter
 */
function clearLocalStoragewithPrepend(prepend){
	// loop through whole localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		// get the key
		var key = localStorage.key(i);
		// remove item if the key has the defined prepend
		if(stringContains(prepend,key))
		{
			localStorage.removeItem(key);
			l--; // accounts for removal of entry in length of localStorage
			i--; // accounts for removal of entry in position of search
		}
	}
}

/**
 * This function delineates the specified value into an array
 * (index 0=prepend or netID, 1=date, 2=startTime, 3=endTime). 
 * It then returns the desired component of the value
 * 
 * The options for return subcomponent of the value are 0, 1, 2, or 3. The entire
 * value is returned if the delimitee does not match one of the specified options
 * 
 * @param value -
 *            the value to be split
 * @param delimitee -
 *            the desired component of the value (i.e. date, etc.)
 * 
 * @return The desried specific component of the value (i.e. 2012-02-18, etc.)
 * 
 * @author Todd Wegter
 */
function valueDelimiter(value,delimitee){
	if (value == null || delimitee == null) {
	    return null;
	}
	
	var valueArray = new Array();
	
	valueArray = value.split(" ");
	
	if(delimitee==0){
		return valueArray[0];
	}
	else if(delimitee==1){
		return valueArray[1];
	}
	else if(delimitee==2){
		return valueArray[2];
	}
	else if(delimitee==3){
		return valueArray[3];
	}
	return value;
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

/**
 * Returns the key of the first matching item in LocalStorage
 * 
 * @param prepend - 
 *		the prepedn for the key of the elements to be stored
 * @param searchKey - 
 *			the other component of the the key to be returned
 * @return the key of the desired locally stored element, null if none is found
 *
 */
function returnFirstLocalKey(prepend, searchKey){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		// if the key contains the prepend and the searchKey, store it and the
		// desired part of the key into the array
		if(stringContains(prepend,key) && stringContains(searchKey,key))
		{
			return key;
		}
	}
	return null;
}


/**
 * Stores the specified contents of the localStorage to an array (useful for
 * sorting etc.) Items stored have keys which contain the specified prepend and
 * searchKey.
 * 
 * @param prepend -
 *            the prepend for the key of the elements to be stored
 * @param searchKey -
 *            the other component of the key for the elements to be stored
 * @param delimitee -
 *            the desired component of the key to be stored in the array
 * 
 * @return A 2D array: each row has two entries -> [i][0] is the desired
 *         component of the key -> [i][1] is the value associated with the
 *         un-cut key
 * 
 * This allows the array to be sorted using array.sort() -> sorts by the 0th
 * column, then the 1th column (for duplicates in 0th column)
 * 
 * @author Todd Wegter
 */
function storeToArray(prepend, searchKey, delimitee){
	var index = 0;
	var array = new Array();
	// search through all localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		// if the key contains the prepend and the searchKey, store it and the
		// desired part of the key into the array
		if(stringContains(prepend,key) && stringContains(searchKey,key))
		{
			array[index++] = [keyDelimiter(key,delimitee),localStorage.getItem(key)];
		}
	}
	return array;
}


/**
 * Stores keys with desired prepend to array to be sorted by desired element
 *
 * @param prepend -
 *            the prepend for the key of the elements to be stored
 * @param delimitee -
 *            the desired component of the key to be stored in the array
 * 
 * @return A 2D array: each row has two entries -> [i][0] is the desired
 *         component of the key -> [i][1] is the full key
 * 
 * This allows the array to be sorted using array.sort() -> sorts by the 0th
 * column, then the 1th column (for duplicates in 0th column)
 * 
 * @author Todd Wegter
 */
function storeKeysToArray(prepend, delimitee){
	var index = 0;
	var array = new Array();
	// search through all localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		// if the key contains the prepend and the searchKey, store it and the
		// desired part of the key into the array
		if(stringContains(prepend,key))
		{
			array[index++] = [keyDelimiter(key,delimitee),key];
		}
	}
	return array;
}

/**
 * Takes a 2D array created with storeToArray(prepend, searchKey, delimitee) and
 * compiles its values (not the keys) to a string
 * 
 * @param array - the array to stringify
 * 
 * @return The string of the array
 */
function storageArrayToString(array){
	var returnString = "";
	for(var i = 0, l = array.length; i<l; i++)
	{
		// add value for each row to the string
		var value = array[i][1];
		returnString += value + "</br>";
	}
	return returnString;
}

/**
 * Adds a row to the specified table with the given info
 *
 * @param table - table to be added to
 * @param key - key to be added to table
 * @param col1 - (string) the segment of the key to be stored in column 1
 * @param col2 - (string) the segment of the key to be stored in column 2
 * @param col3 - (string) the segment of the key to be stored in column 3
 * @param col4 - (string) the segment of the key to be stored in column 4
 *
 * @author Todd Wegter
 * @date 3-18-12
 */
function addRowToTable(table,value)
{
	if (!document.getElementsByTagName) return;
	tabBody=document.getElementsByTagName("TBODY").item(table);
	row=document.createElement("TR");
	cell1 = document.createElement("TD");
	cell2 = document.createElement("TD");
	cell3 = document.createElement("TD");
	cell4 = document.createElement("TD");
	textnode1=document.createTextNode(valueDelimiter(value,0));
	textnode2=document.createTextNode(valueDelimiter(value,1));
	textnode3=document.createTextNode(valueDelimiter(value,2));
	textnode4=document.createTextNode(valueDelimiter(value,3));
	cell1.appendChild(textnode1);
	cell2.appendChild(textnode2);
	cell3.appendChild(textnode3);
	cell4.appendChild(textnode4);
	row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	row.appendChild(cell4);
	tabBody.appendChild(row);
	return;
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

// end hiding script from old browsers -->
