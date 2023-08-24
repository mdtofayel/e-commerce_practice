var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldContryName;
var fieldCountryCode;

$(document).ready(function(){
	buttonLoad =$("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName")
	fieldContryName = $("#fieldCountryName")
	fieldCountryCode = $("#fieldCountryCode")
	
	buttonLoad.click(function(){
		loadCountries();
	});
	
	dropDownCountry.on("change", function(){
		changeFormStateToSelectedCountry();
	});
	
	buttonAddCountry.click(function(){
		if(buttonAddCountry.val()== "New"){
			changeFormStateToNew();
		}else{
			
			addCountry();
		}
		
		
	});
	
	buttonUpdateCountry.click(function (){
		updateCountry();
	});
	
	buttonDeleteCountry.click(function (){
		deleteCountry();
	} );
	
});

function deleteCountry(){
	var optionValue =  dropDownCountry.val().split("-")[0];
	url = contextPath + "countries/delete/"+optionValue;
	

	$.get(url,function(){
		$("#dropDownCountries option[value='" +dropDownCountry.val()+ "']").remove();
		changeFormStateToNew();		
	}).done(function(){
		showToastMessage("Selected Country have been Deleted.");
	}).fail(function(){
		showToastMessage("ERROR: Cound not connected to the server");
	});
}

function updateCountry(){
	url = contextPath +"countries/save";	
	countryName = fieldContryName.val();
	countryCode = fieldCountryCode.val();	
	
	countryId = dropDownCountry.val().split("-")[0];
	
	//console.log(countryId);
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);	
		},
		data: JSON.stringify(jsonData),
		contentType:'application/json'
	}).done(function(countryId){
		$("#dropDownCountries option:selected").val(countryId + "-" +countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated.");
		changeFormStateToNew();
	}).fail(function (){
		showToastMessage("ERROR: Could not connect to server or encountered an error.")
	});
}
function addCountry(){
	url = contextPath +"countries/save";	
	countryName = fieldContryName.val();
	countryCode = fieldCountryCode.val();	
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);	
		},
		data: JSON.stringify(jsonData),
		contentType:'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId,countryCode, countryName);
		showToastMessage("The new country has been added.");
	}).fail(function (){
		showToastMessage("ERROR: Could not connect to server or encountered an error.")
	});
	
}
function selectNewlyAddedCountry(countryId,countryCode, countryName){
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	
	$("#dropDownCountries option[value='" +optionValue + "']").prop("slected", true);
	
	fieldContryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToNew(){
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	
	buttonUpdateCountry.prop("disabled",true);
	buttonDeleteCountry.prop("disabled",true);
	
	fieldContryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry(){
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled",false);
	buttonDeleteCountry.prop("disabled",false);
	
	labelCountryName.text("Selected Country:")
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldContryName.val(selectedCountryName);
	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function loadCountries(){
	url = contextPath + "countries/list";
	$.get(url,function(responseJSON){
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country){
			optionValue = country.id + " - "+ country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		showToastMessage("Al countries have been loaded.");
	}).fail(function(){
		showToastMessage("ERROR: Cound not connected to the server");
	});
}
function showToastMessage(message){
	$("#toastMeassage").text(message);
	$(".toast").toast('show');
}

