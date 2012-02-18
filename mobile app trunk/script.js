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
	return d.getHours()+":"+d.getMinutes();
} 

//will return name assoc. with netID from database
function getName(netID){
	return "firstname lastname";
}

function localStorageContains(searchKey){
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(searchKey,key))
			return true;
	}
	return false;
}

/**
* Stores an entry with the standard key: prepend firstname lastname netID date startTime endTime
**/
function storeEntry(prepend, netID, date, startTime, endTime, entry){
	var key = prepend+" "+getName(netID)+" "+netID+" "+date+" "+startTime+" "+endTime;
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
function keyDelineator(key,delineator){
	var keyArray = new Array();
	keyArray = key.split(" ");
	if(delineator=="date"){
		return keyArray[4];
	}
	else if(delineator=="netID"){
		return keyArray[3];
	}
	else if(delineator=="lastname"){
		return keyArray[2];
	}
	else if(delineator=="firstname"){
		return keyArray[1];
	}
	else if(delineator=="dateandtime"){
		return keyArray[4]+keyArray[5]+keyArray[6];
	}
	else if(delineator=="time"){
		return keyArray[5];
	}
	return key;
}

function storeToArray(prepend, searchKey, delineator){
	var index = 0;
	var array = new Array();
	for(var i = 0, l = localStorage.length; i<l; i++)
	{
		var key = localStorage.key(i);
		if(stringContains(prepend,key) && stringContains(searchKey,key))
		{
			array[index++] = [keyDelineator(key,delineator),localStorage.getItem(key)];
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
