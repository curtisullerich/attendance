<!-- hide script from old browsers

var absentPrepend = "absentStudent";
var eventPrepend = "storedEvent";
var tardyPrepend = "tardyStudent";

function stringContains(sub, str) {            
	return str.indexOf(sub) !== -1;
}

function dateToday() {
	var d = new Date();        
	return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
}

function timeNow() {
	var d = new Date();
	return d.getHours()+":"+d.getMinutes();
} 

//end hiding script from old browsers -->
