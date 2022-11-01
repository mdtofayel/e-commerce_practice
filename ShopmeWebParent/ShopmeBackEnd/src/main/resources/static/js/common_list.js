function clearFilter() {
	window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure to delete this user ID " + entityId + "?");
	$("#confirmModal").modal();

}

/*$(document).ready(function(){
			$(".link-delete").on("click", function(e){
				e.preventDefault();
				link = $(this);
				//alert($(this).attr("href"));
				userId = link.attr("userId");
				$("#yesButton").attr("href", link.attr("href"));
				$("#confirmText").text("Are you sure to delete this user ID " + userId +"?");
				$("#confirmModal").modal();
			});
		});
		
		function clearFilter(){
			window.location="[[@{/users}]]"
		}*/ 