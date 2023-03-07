const className = "result";

function generateResult(answers, rightAnswers, currentAnswer, type, walkthrough, parentElementNumber){
	if (type === "checkbox"){
		generateFields(answers, rightAnswers, type, parentElementNumber);
	} else if (type === "radio"){
		generateFields(answers, rightAnswers, type, parentElementNumber);
	} else if (type === "text") {
		generateOneField(rightAnswers, type, parentElementNumber);
	}
	generateCurrentAnswerElement(currentAnswer, walkthrough, parentElementNumber);
}

function generateFields(answers, rightAnswers, type, parentElementNumber){
	const parentElement = document.getElementsByClassName("result")[parentElementNumber];
	for (const answer of answers){
		const newField = document.createElement("input");
		newField.disabled = true;
		newField.type = type;
		if (rightAnswers.indexOf(answer) !== -1){
			newField.checked = true;
		}

		const newLabel = document.createElement("label");
		newLabel.disabled = true;
		newLabel.classList.add("label-answer");
		newLabel.appendChild(newField);
		const text = document.createElement("p");
		text.innerHTML = answer;
		text.classList.add("answer-text");
		newLabel.appendChild(text);

		parentElement.appendChild(newLabel);
	}
}

function generateOneField(answer, type, parentElementNumber){
	const newLabel = document.createElement("label");
	newLabel.disabled = true;
	newLabel.classList.add("label-answer");

	const text = document.createElement("p");
	text.innerHTML = "Right answer: ";
	text.classList.add("text-for-input");
	newLabel.appendChild(text);

	const newField = document.createElement("input");
	newField.disabled = true;
	newField.value = answer;
	newField.type = type;
	newLabel.appendChild(newField);

	const parentElement = document.getElementsByClassName("result")[parentElementNumber];
	parentElement.appendChild(newLabel);
}

function generateCurrentAnswerElement(currentAnswer, walkthrough, parentElementNumber){
	const currentAnswerElement = document.createElement("p");
	currentAnswerElement.innerHTML = "Your answer: " + currentAnswer;
	currentAnswerElement.classList.add("current-answer");
	if (walkthrough === "positive"){
		currentAnswerElement.classList.add("positive");
	} else if (walkthrough === "negative"){
		currentAnswerElement.classList.add("negative");
	} else {
		throw new Error("Illegal state! Walkthrough type as invalid!");
	}

	const parentElement = document.getElementsByClassName("result")[parentElementNumber];
	parentElement.appendChild(currentAnswerElement);
}