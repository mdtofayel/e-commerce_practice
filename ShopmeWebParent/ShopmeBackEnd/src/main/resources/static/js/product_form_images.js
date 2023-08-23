
var extraImagesCount = 0;



$(document).ready(function() {
	$("input[name='extraImage']").each(function(index) {

		$(this).change(function() {
			if (!checkFileSize(this)) {
				return;
			}
			showExtraImageThumbnail(this, index);
		});
	});
	/*$("#extraImage1").change(function() {
		if (!checkFileSize(this)) {
			return;
		}
		

	});*/
	
	$("a[name='linkForRemoveExtraImage']").each(function(index){
		$(this).click(function(){
			removeExtraImage(index);
		})
	});
});

function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	
	fileName = file.name;
	
	imageNameHiddenField =$("#imageName"+index);
	if(imageNameHiddenField.length){
		imageNameHiddenField.val(fileName);
	}
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
	if (index >= extraImagesCount - 1) {
		addNextExtraImageSection(index + 1);
	}


}

function addNextExtraImageSection(index) {
	htmlExtraImages = `<div class="col border m-3 p-2"  id="divExtraImage${index}">
				<div id="extraImageHeader${index}"><label> Extra Image #${index + 1}: </label></div>
				<div class="m-2">
					<img id="extraThumbnail${index}" alt="extra image #${index + 1} preview" class="img-fluid"
						src="${defaultImageThumbnailSrc}"/>
				</div>
				<div>
					<input type="file"  name="extraImage" 
					   		onchange="showExtraImageThumbnail(this,${index})"
					   		accept="image/png, image/jpeg"/>
				</div>
			</div>`;
	htmlExtraImagesRemove = `
				<a class="btn fas fa-times-circle fa-2x icon-dark float-right"
					href="javascript:removeExtraImage(${index - 1})"
					title="Remove this image"></a>
			`
	$("#divProductImages").append(htmlExtraImages);

	$("#extraImageHeader" + (index - 1)).append(htmlExtraImagesRemove);
	extraImagesCount++;
}
function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();

}



