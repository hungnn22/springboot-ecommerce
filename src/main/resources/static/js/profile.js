fileUpload.onchange = evt => {
	const [file] = fileUpload.files
	if (file) {
		avatar.src = URL.createObjectURL(file)
	}
}