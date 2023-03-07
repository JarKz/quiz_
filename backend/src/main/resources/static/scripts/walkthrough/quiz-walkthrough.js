function generateInputs(type, answers){
	type = type.toLowerCase();
	const form = document.getElementById("quiz-walkthrough-form");
	const submitButton = document.getElementById("submit-button");
	if (type === "text"){
		const newInputElement = document.createElement("input");
		newInputElement.type = type;
		newInputElement.name = "answer";
		newInputElement.value = "";
		newInputElement.placeholder = "Enter the answer..";
		form.insertBefore(newInputElement, submitButton);

		const root = document.documentElement;

		root.style.setProperty("--input-width", `${parseInt(getComputedStyle(newInputElement).width)}px`);
		root.style.setProperty("--input-height", `${parseInt(getComputedStyle(newInputElement).height)}px`);
		setEventListenersForInputElements(newInputElement);
	} else if (type === "checkbox") {
		generateAnswers(form, submitButton, type, answers);
	} else if (type === "radio") {
		generateAnswers(form, submitButton, type, answers);
	} else {
		throw new Error("Undefined question type!");
	}
}

function generateAnswers(parentElement, beforeElement, type, answers){
	for (let answer of answers){
		const newInputElement = document.createElement("input");
		newInputElement.type = type;
		newInputElement.name = "answer";
		newInputElement.value = answer;

		const newLabel = document.createElement("label");
		newLabel.appendChild(newInputElement);
		const text = document.createElement("p");
		text.innerHTML = answer;
		text.classList.add("answer-text");
		newLabel.appendChild(text);

		newLabel.classList.add("label-for-answer");

		parentElement.insertBefore(newLabel, beforeElement);
	}
}