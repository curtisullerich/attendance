<!-- hide script from old browsers

/**
 * Common code for the Marching Band Attendance Mobile App
 * 
 * @author Todd Wegter, Curtis Ullerich
 * 
 * Latest Rev: 3-24-12
 * 
 * standard key for localStorage: prepend firstname lastname netID date startTime endTime rank
 * rank is a 6 digit number with leading 0s
 */

//Prepend variables
var absentPrependPerformance = "absentStudentPerformance";
var absentPrependRehearsal = "absentStudentRehearsal";
var rehearsalPrepend = "storedRehearsal";
var performancePrepend = "storedPerformance";
var tardyPrepend = "tardyStudent";
var studentPrepend = "studentRecord";
var loginPrepend = "storedLogin";

//debug enable thingy
var loginDebug = true;
var debug = true;


/**
 * Makes browser go back (same as hitting browser's back button)
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
 * @param sub
 * 			- contained string
 * @param str
 * 			- containing string
 * @returns True if sub is contained within str
 * @author Curtis Ullerich
 * @date 1-31-2012
 */
function stringContains(sub, str) {            
	return str.indexOf(sub) != -1;
}


/**
 * Returns today's date in format YYYY-MM-DD where number less than 10 are preceeded by 0
 * 
 * @return Date in YYYY-MM_DD format
 * @author Curtis Ullerich, Todd Wegter
 * @date 1-31-2012
 */
function dateToday() {
	var d = new Date();
	//d.getMonth() returns value of 0-11, so we must add 1
	var month = d.getMonth()+1;
	var date = d.getDate();
	// add preceding 0 for sortability
	if(parseInt(month,10)<10)
		month = "0"+month;
	if(parseInt(date,10)<10)
		date = "0"+date;
	return d.getFullYear()+"-"+month+"-"+date;
}

/**
 * Returns the current time in 24 hour format: HHMM
 * 
 * @return The time in 24 hour HHMM format
 * @author Curtis Ullerich, Todd Wegter
 * @date 1-31-2012
 */
function timeNow() {
	var d = new Date();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	// add preceding 0 for sortability
	if(parseInt(hours,10)<10)
		hours = "0"+hours;
	if(parseInt(minutes,10)<10)
		minutes = "0"+minutes;
	//ensure the return is a string
	return ""+hours+minutes;
} 

/**
 * This functions returns the current time in 12 hour format: HH:MMam/pm
 * 
 * @return The time in HH:MMamp/pm format
 * @author Todd Wegter
 * @date 1-31-2012
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
	return hours+":"+minutes+""+ampm;
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
 * @returns True if the specified event exists
 * @author Todd Wegter
 * @date 2-10-2012
 */
function localStorageContainsEvent(eventType, date, starttime, endtime){
	//iterate through all of localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		//check if the key matches the desired event
		if((stringContains(rehearsalPrepend,key) || stringContains(performancePrepend,key)) && keyDelimiter(key, "firstname")==eventType && keyDelimiter(key, "starttime")==starttime && keyDelimiter(key, "endtime")==endtime && keyDelimiter(key, "date")==date)
			return true;
	}
	return false;
}

/**
 * Returns true if localStorage contains a student with the specified netID
 *
 * @param netID
 *			- netID of desired student
 * @returns true if the specified student netID
 * @author Todd Wegter
 * @date 2-10-2012
 */
function localStorageContainsStudent(netID){
	//iterate through all of localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		//check if the key matches the desired student
		if(stringContains(studentPrepend, key) && keyDelimiter(key, "netID")==netID)
			return true;
	}
	return false;
}


/**
 * Returns the first name associated with the given netID from the localStorage
 * 
 * @param netID
 * 			- the netID to which the desired name corresponds
 * @returns The first name associated with the netID, null if netID doesn't exist
 * @author Todd Wegter
 * @date 3-9-2012
 */
function getFirstName(netID){
	var key = returnFirstLocalKey(studentPrepend, netID);
	var firstName = null;
	if(key != null)
		firstName = keyDelimiter(key, "firstname");
	return firstName;
}

/**
 * Returns the last name associated with the given netID from the localStorage
 *
 * @param netID
 * 			- the netID to which the desired name corresponds
 * @returnsTthe last name associated with the netID, null if netID doesn't exist
 * @author Todd Wegter
 * @date 3-9-2012
 */
function getLastName(netID){
	var key = returnFirstLocalKey(studentPrepend, netID);
	var lastName = null;
	if(key != null)
		lastName = keyDelimiter(key, "lastname");
	return lastName;
}

/**
 * Returns the rank associated with the given netID from the localStorage
 * 
 * @param netID
 * 			- the netID to which the desired rank corresponds
 * @returns The rank associated with the netID specified, null if netID doesn't exist
 * @author Todd Wegter
 * @date 3-9-2012
 */
function getRank(netID){
	var key = returnFirstLocalKey(studentPrepend, netID);
	var rank = null;
	if(key != null)
		rank = keyDelimiter(key, "rank");
	return rank;
}


/**
 * Returns the netID associated with the given university ID 
 * 
 * @param univID
 * 			- the given University ID
 * @returns the associated netID; null if no match is found
 * @author Todd Wegter
 * @date 3-30-2012
 */
function getNetID(univID){
	//hash the university id
	var hashedUnivID = Sha1.hash(univID,true).toUpperCase();;
	//iterate through all of localStorage
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		//check if the key matches the desired student
		if(stringContains(studentPrepend, key) && localStorage.getItem(key) == hashedUnivID)
			return keyDelimiter(key,"netID");
	}
	return null;
}

/**
 * Stores an entry with the standard key: "prepend firstname lastname netID date startTime endTime rank"
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
 * @returns True if successful
 * @author Todd Wegter
 * @date 2-10-2012
 */
function storeEntry(prepend, firstname, lastname, netID, date, startTime, endTime, rank, entry){
	var key = prepend+" "+firstname+" "+lastname+" "+netID+" "+date+" "+startTime+" "+endTime+" "+rank;
	localStorage.setItem(key,entry);
}

/**
 * Removes an entry with the standard key: "prepend firstname lastname netID date startTime endTime rank"
 * 
 *@param prepend
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
 * @returns True if the entry was removed, false if it did not exist
 * @author Curtis Ullerich
 * @date 2-10-2012
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
 * Clears all items in localStorage which have the specified prepend
 * 
 * @param prepend
 * 			- entries with this prepend in their keys are removes
 * @returns True if successful
 * @author Todd Wegter
 * @date 2-10-12
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
	return true;
}

/**
 * This function delineates the specified value into an array
 * (index 0=prepend or netID, 1=date, 2=startTime, 3=endTime). 
 * It then returns the desired component of the value
 * 
 * The options for return subcomponent of the value are 0, 1, 2, or 3. The entire
 * value is returned if the delimitee does not match one of the specified options
 * 
 * @param value
 *            - the value to be split
 * @param delimitee
 *            - the desired component of the value (i.e. date, etc.)
 * @returns The desried specific component of the value (i.e. 2012-02-18, etc.)
 * @author Todd Wegter
 * @date 3-3-2012
 */
function valueDelimiter(value, delimitee){
	if (value == null || delimitee == null) {
	    return null;
	}
	
	var valueArray = new Array();
	
	//splits the value into multiple strings, delimited by spaces, and stores them into an array
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
 * @param key
 *            - the key to be split
 * @param delimitee -
 *            - the desired component of the key (i.e. date, etc.)
 * @returns The desried specific component of the key (i.e. 2012-02-18, etc.)
 * @author Todd Wegter
 * @date 2-10-2012
 */
function keyDelimiter(key,delimitee){
	if (key == null || delimitee == null) {
	    return null;
	}
	
	var keyArray = new Array();
	
	//splits the key into multiple strings, delimited by spaces, and stores them into an array
	keyArray = key.split(" ");
	
	if (delimitee=="key") {
	  return key;		// so we can access a subsection of the full localStorage as key-value pairs, in tact
	}
	else if(delimitee=="date"){
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
 * @param prepend
 *				- the prepend for the key of the elements to be stored
 * @param searchKey 
 *				- the other component of the the key to be returned
 * @returns The key of the desired locally stored element, null if none is found
 * @author Todd Wegter
 * @date 3-3-2012
 */
function returnFirstLocalKey(prepend, searchKey){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		// if the key contains the prepend and the searchKey, return it
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
		// if the key contains the prepend and the searchKey, store it and the value into the array
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
 * @param prepend 
 * 				- the prepend for the key of the elements to be stored
 * @param delimitee
 *				- the desired component of the key to be stored in the array
 * 
 * @returns A 2D array: each row has two entries -> [i][0] is the desired
 *         component of the key -> [i][1] is the full key
 * 
 * This allows the array to be sorted using array.sort() -> sorts by the 0th
 * column, then the 1th column (for duplicates in 0th column)
 * 
 * @author Todd Wegter
 * @date 3-3-2012
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
 * @param array 
 * 				- the array to stringify
 * @returns The string of the array
 * @author Todd Wegter
 * @date 3-3-2012
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
 * Adds a row to the specified table with the given info (pretty printing)
 *
 * @param table 
 * 				- table to be added to
 * @param key 
 * 				- key to be added to table
 * @param col1
				- (string) the segment of the key to be stored in column 1
 * @param col2 
 * 				- (string) the segment of the key to be stored in column 2
 * @param col3 
 * 				- (string) the segment of the key to be stored in column 3
 * @param col4 
 * 				- (string) the segment of the key to be stored in column 4
 * @returns True if successful
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
	return true;
}

/**
 * Checks the netID and password fields on a page against the database, hides the login div, and shows the main div (not terribly secure, but good enough...)
 * 
 * @returns False (otherwise onsubmit calls to this function refresh the page, which is not desired)
 * @author Todd Wegter
 * @date 3-24-2012
 */ 
function confirmTACredentials(){
	//creates dummy TA value if loginDebug is true: netID is TA, password is password
	if (loginDebug){
		storeEntry(loginPrepend, "|", "|", "TA", "|", "|", "|", "|", Sha1.hash("password",true).toUpperCase());
	}
	//gets the netID and password from the page
	var name = document.getElementById("TA").value;
	var password = document.getElementById("password").value;
	if (name!=null && name!="")
	{
		if (password!=null && password != "")
		{
			for (var i = 0; i < localStorage.length; i++) {
				var key = localStorage.key(i);
				//if this is the right netid, then hash the plaintext password and check it
				if(stringContains(loginPrepend, key)){
					if (keyDelimiter(key,"netID") == name) {
						if (localStorage[key] == Sha1.hash(password,true).toUpperCase()) {
							document.getElementById("login").hidden = true;
							document.getElementById("main").hidden = false;
							//removes dummy TA value if loginDebug is true
							if (loginDebug)
								localStorage.removeItem(key);
							return false;
						} 
					}
				}
			}
			alert("Invalid netID or password");
			return false;
		}
		else
		{
			alert("Please enter a password");
			return false;
		}
	}
	else
	{
		alert("Please enter a TA netID");
		return false;
	}
}

// end hiding script from old browsers -->
