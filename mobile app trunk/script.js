<!-- hide script from old browsers

var absentPrepend = "absentStudent";
var eventPrepend = "storedEvent";
var tardyPrepend = "tardyStudent";

function stringContains(sub, str) {            
	return str.indexOf(sub) !== -1;
}

function dateToday() {
	var d = new Date();
	var month = d.getMonth()+1;
	var date = d.getDate();
	if(parseInt(month)<10)
		month = "0"+month;
	if(parseInt(date)<10)
		date = "0"+date;
	return d.getFullYear()+"-"+month+"-"+date;
}

function timeNow() {
	var d = new Date();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	if(parseInt(hours)<10)
		hours = "0"+hours;
	if(parseInt(minutes)<10)
		minutes = "0"+minutes;
	return hours+minutes;
} 

function twelveHourTimeNow(){
	var d = new Date()
	var hours = d.getHours();
	var ampm;
	if((parseInt(hours)) < 12)
		ampm = "am";
	else
		ampm = "pm";
	if(parseInt(hours) == 0)
		hours = 12;
	else
		hours = parseInt(hours)%12;
	var minutes = d.getMinutes();
	if(parseInt(minutes)<10)
		minutes = "0"+minutes;
	return hours+":"+minutes+" "+ampm;
}

//will return name assoc. with netID from database
function getFirstName(netID){
	return "firstname";
}

//will return name assoc. with netID from database
function getLastName(netID){
	return "lastname";
}

function localStorageContains(prepend, searchKey){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(prepend,key) && stringContains(searchKey,key))
			return key;
	}
	return null;
}


function localStorageContainsEvent(time){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(eventPrepend,key) && keyDelimiter(key, "starttime")<time && keyDelimiter(key, "endtime")>time)
			return true;
	}
	return false;
}

/**
* Stores an entry with the standard key: prepend firstname lastname netID date startTime endTime
**/
function storeEntry(prepend, firstname, lastname, netID, date, startTime, endTime, entry){
	var key = prepend+" "+firstname+" "+lastname+" "+netID+" "+date+" "+startTime+" "+endTime;
	localStorage.setItem(key,entry);
}

function clearLocalStoragewithPrepend(prepend){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(prepend,key))
		{
			localStorage.removeItem(key);
			l--; //accounts for removal of entry in length of localStorage
			i--; //accounts for removal of entry in position of search
		}
	}
}

/**
* This function delineates the key into an array (index 0=prepend, 1=firstname, 2=lastname, 3=netID, 4=date, 5=startTime, 6=endTime).
* It then returns the desired component of the key
**/
function keyDelimiter(key,delimiter){
	var keyArray = new Array();
	keyArray = key.split(" ");
	if(delimiter=="date"){
		return keyArray[4];
	}
	else if(delimiter=="netID"){
		return keyArray[3];
	}
	else if(delimiter=="lastname"){
		return keyArray[2];
	}
	else if(delimiter=="firstname"){
		return keyArray[1];
	}
	else if(delimiter=="dateandtime"){
		return keyArray[4]+keyArray[5]+keyArray[6];
	}
	else if(delimiter=="starttime"){
		return keyArray[5];
	}
	else if(delimiter=="endtime"){
		return keyArray[6];
	}
	return key;
}

function storeToArray(prepend, searchKey, delimiter){
	var index = 0;
	var array = new Array();
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(prepend,key) && stringContains(searchKey,key))
		{
			array[index++] = [keyDelimiter(key,delimiter),localStorage.getItem(key)];
		}
	}
	return array;
}

function storageArrayToString(array){
	var returnString = "";
	for(var i = 0, l = array.length; i<l; i++)
	{
		var value = array[i][1];
		returnString += value + "</br>";
	}
	return returnString;
}

//end hiding script from old browsers -->
