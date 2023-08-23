

dropDownBrands = $("#brand");
dropDownCategories = $("#category");

$("#shortDescription").richText();
$("#fullDescription").richText();

$(document).ready(function() {
	dropDownBrands.change(function() {
		dropDownCategories.empty();
		getCategories();
	});
	getCategoriesForNewForm();
});
function getCategoriesForNewForm(){
	catIdField = $("#categoryId");
	editMode = false;
	if(catIdField.length){
		editMode = ture;
	}
	if(!editMode) getCategories();
}
function getCategories() {
	brandId = dropDownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropDownCategories);
		});
	});
}

function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	csrfValue = $("input[name = '_csrf']").val();

	
	params = { id: productId, name: productName, _csrf: csrfValue };
	
	$.post(checkUniqueUrl, params, function(response) {
		if (response == "OK") {

			form.submit();
		} else if (response == "Duplicate") {
			showWarningModal("There is another Product having same name " + productName);
		} else {
			showErrorModal("Unknown response from the server.");
		}
	}).fail(function() {
		showErrorModal("Could not connect the server");
	});

	return false;
}
