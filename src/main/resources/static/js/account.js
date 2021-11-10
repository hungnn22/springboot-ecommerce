const checkbox = document.getElementById('agree')
const btn = document.querySelector("#btnRegister")

checkbox.addEventListener('change', (event) => {
	if (event.currentTarget.checked) {
		btn.classList.remove("disabled")
	} else {
		if (!btn.classList.contains("disabled")) {
			btn.classList.add("disabled")
		}
	}
})