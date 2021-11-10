const VALID_FILE_TYPES = [
	"image/apng",
	"image/bmp",
	"image/gif",
	"image/jpeg",
	"image/pjpeg",
	"image/png",
	"image/svg+xml",
	"image/tiff",
	"image/webp",
	"image/x-icon"
];

const VALID_FILE_EXTENSION = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];


const input = document.querySelector('#image_uploads');
const preview = document.querySelector('.preview');

input.style.opacity = 0;

input.addEventListener('change', handleChooseImage);

function handleChooseImage() {
	while (preview.firstChild) {
		preview.removeChild(preview.firstChild);
	}

	const curFiles = input.files;
	if (curFiles.length === 0) {
		const para = document.createElement('p');
		para.textContent = 'No files currently selected for upload';
		preview.appendChild(para);
	} else {
		const list = document.createElement('ol');
		preview.appendChild(list);

		for (const file of curFiles) {
			const listItem = document.createElement('li');
			const para = document.createElement('p');
			if (validFileType(file)) {
				para.textContent = `${file.name}(${getFileSize(file.size)}).`;
				const image = document.createElement('img');
				image.src = URL.createObjectURL(file);
				listItem.appendChild(image);
				listItem.appendChild(para);
			} else {
				para.textContent = `${file.name}: Not a valid file type. Update your selection.`;
				listItem.appendChild(para);
			}

			list.appendChild(listItem);
		}
	}
}


function validFileType(file) {
	var fileName = document.getElementById("image_uploads").value;
	var idxDot = fileName.lastIndexOf(".") + 1;
	var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
	if (extFile == "jpg" || extFile == "jpeg" || extFile == "png") {
		//TO DO
	} else {
		alert("Only jpg/jpeg and png files are allowed!");
	}
	return VALID_FILE_TYPES
		.includes(file.type);
}

function getFileSize(number) {
	if (number < 1024) {
		return number + 'bytes';
	} else if (number >= 1024 && number < 1048576) {
		return (number / 1024).toFixed(1) + 'KB';
	} else if (number >= 1048576) {
		return (number / 1048576).toFixed(1) + 'MB';
	}
}


function handleSubmitForm(oForm) {
	var arrInputs = oForm.getElementsByTagName("input");
	for (var i = 0; i < arrInputs.length; i++) {
		var oInput = arrInputs[i];
		if (oInput.type == "file") {
			var sFileName = oInput.value;
			if (sFileName.length > 0) {
				var blnValid = false;
				for (var j = 0; j < VALID_FILE_EXTENSION.length; j++) {
					var sCurExtension = VALID_FILE_EXTENSION[j];
					if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
						blnValid = true;
						break;
					}
				}

				if (!blnValid) {
					alert("Sorry, File upload is invalid, allowed extensions are: " + VALID_FILE_EXTENSION.join(", "));
					return false;
				}
			}
		}
	}

	return true;
}