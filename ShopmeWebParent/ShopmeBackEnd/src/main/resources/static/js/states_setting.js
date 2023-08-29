var buttonLoad4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function(){
	buttonLoad4States =$("#buttonLoadCountriesForStates");
	dropDownCountry4States = $("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName")
	
	buttonLoad4States.click(function(){
		loadCountries4States();
	});
	
	
	dropDownCountry4States.on("change", function(){
		loadStates4Country();
	});
	
	dropDownStates.on("change", function(){
		changeFormStateToSelectedState();
	});
	
	
	buttonAddState.click(function(){
		if(buttonAddState.val()== "New"){
			changeFormStateToNew();
		}else{
			
			addState();
		}
		
		
	});
	
	buttonUpdateState.click(function (){
		updateState();
	});
	
	buttonDeleteState.click(function (){
		deleteState();
	} );
	
});
function deleteState(){
	stateId  =  dropDownStates.val();
	url = contextPath + "state/delete/"+stateId;

	$.get(url,function(){
		$("#dropDownStates option[value='" +stateId+ "']").remove();
		changeFormStateToNew();		
	}).done(function(){
		showToastMessage("Selected State have been Deleted.");
	}).fail(function(){
		showToastMessage("ERROR: Cound not connected to the server");
	});
}

function updateState(){
	url = contextPath +"states/save";	
	stateId = dropDownStates.val();
	stateName = fieldStateName.val();	
	
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	countryName = selectedCountry.text();
	
	jsonData = {id:stateId, name: stateName, country:{id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);	
		},
		data: JSON.stringify(jsonData),
		contentType:'application/json'
	}).done(function(stateId){
		selectNewlyUpdatedState(stateId,stateName);
		showToastMessage("The state has been updated.");
		changeFormStateToNew();
	}).fail(function (){
		showToastMessage("ERROR: Could not connect to server or encountered an error.")
	});
}


function addState(){
	url = contextPath +"states/save";	
	stateName = fieldStateName.val();
	
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	countryName = selectedCountry.text();
	
	jsonData = {name: stateName, country:{id: countryId, name: countryName}};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);	
		},
		data: JSON.stringify(jsonData),
		contentType:'application/json'
	}).done(function(stateId){
		selectNewlyAddedState(stateId,stateName);
		showToastMessage("The new country has been added.");
	}).fail(function (){
		showToastMessage("ERROR: Could not connect to server or encountered an error.")
	});
	
}

function loadStates4Country(){
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	url = contextPath +"states/list_by_country/" +countryId;
	countryName = selectedCountry.text();
	
	$.get(url,function(responseJSON){
		dropDownStates.empty();
		
		$.each(responseJSON, function(index, state){
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	}).done(function(){
		changeFormStateToNew();
		showToastMessage("Al state have been loaded for country" + countryName);
	}).fail(function(){
		showToastMessage("ERROR: Cound not connected to the server");
	});
}

function loadCountries4States(){
	url = contextPath + "countries/list";
	$.get(url,function(responseJSON){
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country){
			optionValue = country.id + " - "+ country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountriesForStates);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		showToastMessage("Al countries have been loaded.");
	}).fail(function(){
		showToastMessage("ERROR: Cound not connected to the server");
	});
}


function selectNewlyAddedState(stateId, stateName){
	optionValue = stateId;
	$("<option>").val(optionValue).text(stateName).appendTo(dropDownStates);
	
	$("#dropDownStates option[value='" +optionValue + "']").prop("slected", true);
	
	fieldStateName.val("").focus();
}
function selectNewlyUpdatedState(stateId, stateName){
	optionValue = stateId;
	$("<option>").val(optionValue).text(stateName).appendTo(dropDownStates);
	
	$("#dropDownStates option[value='" +optionValue + "']").prop("slected", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToNew(){
	buttonAddState.val("Add");
	labelStateName.text("State Name:");
	
	buttonUpdateState.prop("disabled",true);
	buttonDeleteState.prop("disabled",true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState(){
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled",false);
	buttonDeleteState.prop("disabled",false);
	
	labelStateName.text("Selected State:")
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}

function showToastMessage(message){
	$("#toastMeassage").text(message);
	$(".toast").toast('show');
}

