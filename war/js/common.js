/**
 * Common code for the Marching Band Attendance Website
 * 
 * @author Todd Wegter, Curtis Ullerich, Daniel Stiner
 * 
 */

/**
 * 
 */
function bug_report_form_onsubmit()
{
	var forms = [];
	var count = 0;
	$('form').each(function(index) {
		if($(this).attr('id') != 'bugreportform')
		{
			count++;
			var id = $(this).attr('id');
			
			if(!id)
			{
				id = "form" + count;
			}
			
			var form = {
					id: id,
					fields: {}
			}
			
			$(this).find("input").each(function(index2) {
				form.fields[$(this).attr('name')] = $(this).val();
			});
			
			forms.push(form);
		}
	});
	
	$("#bugreportform input[name=FieldValues]").val(JSON.stringify({ forms: forms }));
	
	return true;
}

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
 * Sends the browser to a specific url after a given delay,
 * showing a pretty message during the delay
 * @author Daniel Stiner
 * @date 6/16/12
 */
function redirectWithDelay(url, message, delay)
{
	if(!url) return;
	delay = delay || 3000;

	if(message)
		$('#loading').text(message);

	$('#loading').fadeIn().delay(3000).fadeOut(function () {
		window.location = url;
	});
}

/**
 * Checks email address validity
 * 
 * http://stackoverflow.com/questions/2855865/jquery-validate-e-mail-address-regex
 * 
 * @author StackOverflow
 * @date 3/22/12
 */
function isValidEmailAddress(emailAddress) {
    var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    return pattern.test(emailAddress);
};