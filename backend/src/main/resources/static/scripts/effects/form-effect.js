function setEventListenersForInputElements(inputElement) {
	inputElement.addEventListener("focusin", (event) => {
		const effectElement = document.createElement("span");
		let rectangle = event.currentTarget.getBoundingClientRect();
		let rectangleFormPage = event.currentTarget.parentNode.getBoundingClientRect();
		effectElement.style.top = `${rectangle.top - rectangleFormPage.top}px`;
		effectElement.style.left = `${rectangle.left - rectangleFormPage.left}px`;
		event.currentTarget.parentNode.insertBefore(effectElement, event.currentTarget.nextSibling);
		effectElement.classList.add("show-color-outline");
	}, false);
	
	inputElement.addEventListener("focusout", () => {
		let allEffectElements = document.getElementsByClassName("show-color-outline");
		for (let element of allEffectElements){
			element.classList = [];
			element.classList.add("erase-color-outline");
			setTimeout(() => {
				element.remove();
			}, 400);
		}
	}, false);
}

let inputElements = document.getElementsByTagName("input");

const root = document.documentElement;
let isFirst = true;

for (let inputElement of inputElements){
	if (isFirst && inputElement.type === "text"){
		root.style.setProperty("--input-width", `${parseInt(getComputedStyle(inputElement).width) + 4}px`);
		root.style.setProperty("--input-height", `${parseInt(getComputedStyle(inputElement).height) + 2}px`);
		isFirst = false;
	}
	setEventListenersForInputElements(inputElement);
}

const errorMessage = document.getElementsByClassName("error");

if (errorMessage.length !== 0){
	const inputElementsForSetBorderColor = document.getElementsByTagName("input");
	for (const element of inputElementsForSetBorderColor){
		element.style.borderColor = "rgba(255, 0, 0, 0.9)";
	}
}