document.addEventListener("click", (event) => {
	if (["button", "span"].indexOf(event.target.localName) === -1 && event.target.parentNode.localName != "ol"){
		const creatorSelectTypesList = document.getElementById("creator-select-types");
		const editorSelectTypesList = document.getElementById("editor-select-types");
		if (creatorSelectTypesList.style.display != "none"){
			creatorSelectTypesList.classList.add("smooth-erase-select");
			
			const arrow = document.getElementById("creator-arrow-select");
			arrow.classList.remove("arrow-right");
			arrow.classList.add("arrow-down");
			
			setTimeout(()=>{
				creatorSelectTypesList.classList.remove("smooth-erase-select");
				creatorSelectTypesList.style.display = "none";
			}, 380);
		} else if (editorSelectTypesList.style.display != "none"){
			const arrow = document.getElementById("editor-arrow-select");
			arrow.classList.remove("arrow-right");
			arrow.classList.add("arrow-down");
			
			editorSelectTypesList.classList.add("smooth-erase-select");
			setTimeout(()=>{
				editorSelectTypesList.classList.remove("smooth-erase-select");
				editorSelectTypesList.style.display = "none";
			}, 380);
		}
	}
	
	if (!recursiveCheckParentNodeForm(event.target) && ["button", "span"].indexOf(event.target.localName) === -1) {
        document.getElementById("question-creator").style.display = "none";
        document.getElementById("question-editor").style.display = "none";
        document.getElementById("delete-warning").style.display = "none";
	}
}, false);

function recursiveCheckParentNodeForm(element){
	if (element != null){
		if (element.localName === "form"){
			return true;
		} else {
			return recursiveCheckParentNodeForm(element.parentNode);
		}
	}
	return false;
}

const creatorFormId = "creator";
const editorFormId = "editor";

const buttonsForRightAnswers = "buttons-for-right-answers";
const buttonsForWrongAnswers = "buttons-for-wrong-answers";

const createRightAnswerButtonId = "create-right-answer";
const eraseRightAnswerButtonId = "erase-right-answer";
const createWrongAnswerButtonId = "create-wrong-answer";
const eraseWrongAnswerButtonId = "erase-wrong-answer";

const rightAnswerClass = "right-answer";
const wrongAnswerClass = "wrong-answer";
const rightAnswerInputName = "right-answers";
const wrongAnswerInputName = "wrong-answers";

function showQuestionCreatorForm(quizId){
    let elem = document.getElementById("question-creator");
    elem.style.display = "flex";
    for (let child of elem.childNodes){
        if (child.name == "quiz_id"){
            child.value = quizId;
        }
    }
	manageFormButtonsInDependsOnType("default", creatorFormId);
}

function showQuestionEditorForm(quizId, questionId, content, type, rightAnswers, wrongAnswers){
    let elem = document.getElementById("question-editor");
    elem.style.display = "flex";

	manageFormButtonsInDependsOnType(type.toLowerCase(), editorFormId);

    fullEraseWrongAnswerFields(editorFormId);
    if (wrongAnswers.length > 0){
        let button = document.getElementById(`${editorFormId}-${createWrongAnswerButtonId}`);
        for (let count = 0; count < wrongAnswers.length; count++){
            button.click();
        }
    }

	fullEraseRightAnswerFields(editorFormId);
	if (rightAnswers.length > 1){
		const button = document.getElementById(`${editorFormId}-${createRightAnswerButtonId}`);
		for (let count = 0; count < rightAnswers.length - 1; count++){
			button.click();
		}
	}

    for (let child of elem.childNodes){
        if (child.name == "quiz_id"){
            child.value = quizId;
        } else if (child.name == "question_id"){
            child.value = questionId;
        } else if (child.name == "content"){
            child.value = content;
        } else if (child.name == "right-answers"){
            child.value = rightAnswers.pop();
        } else if (child.name == "wrong-answers"){
            child.value = wrongAnswers.pop();
        }
    }
    document.getElementById("editor-selected-type").value = type.toLowerCase();
    document.getElementById("editor-current-type").innerHTML = type.slice(0,1) + type.slice(1).toLowerCase();
}

function fullEraseWrongAnswerFields(formType){
    const button = document.getElementById(`${formType}-${eraseWrongAnswerButtonId}`);
    for (let i = 0; i < 5; i++){
        button.click();
    }
}

function fullEraseRightAnswerFields(formType){
	const button = document.getElementById(`${formType}-${eraseRightAnswerButtonId}`);
	for (let i = 0; i < 4; i++){
		button.click();
	}
}

function setEventListenersForListTypeButton(buttonId, arrowElementId, listTypeId){
	const selectTypeButton = document.getElementById(buttonId);
	
	selectTypeButton.addEventListener("click", (event) => {
		event.preventDefault();
		
		const arrow = document.getElementById(arrowElementId);
		arrow.classList.remove("arrow-down");
		arrow.classList.add("arrow-right");
		
		const listOfTypes = document.getElementById(listTypeId);
		listOfTypes.style.display = "flex";
		let currentButtonRectangle = event.currentTarget.getBoundingClientRect();
		listOfTypes.style.left = `${currentButtonRectangle.width + 2}px`;
	},false);
}

setEventListenersForListTypeButton("creator-select-type-button", "creator-arrow-select", "creator-select-types");
setEventListenersForListTypeButton("editor-select-type-button", "editor-arrow-select", "editor-select-types");

function setEventListenersForTypeList(formType, className, arrowElementId, destinationId, textDestinationId){
	const selectTypeElements = document.getElementsByClassName(className);
	
	for (const selectType of selectTypeElements){
		selectType.addEventListener("click", (event) => {
			const currentTarget = event.currentTarget;
			const destinationElement = document.getElementById(destinationId);
			destinationElement.value = currentTarget.innerHTML;
			const otherDestinationElement = document.getElementById(textDestinationId);
			otherDestinationElement.innerHTML = currentTarget.innerHTML;

			manageFormButtonsInDependsOnType(event.currentTarget.innerHTML.toLowerCase(), formType);
			
			const arrow = document.getElementById(arrowElementId);
			arrow.classList.remove("arrow-right");
			arrow.classList.add("arrow-down");
			
			currentTarget.classList.add("click-animation");
			currentTarget.parentNode.classList.add("smooth-erase-select");
			setTimeout(()=>{
				currentTarget.classList.remove("click-animation");
				currentTarget.parentNode.classList.remove("smooth-erase-select");
				currentTarget.parentNode.style.display = "none";
			}, 380);
		},false)
	}
}

setEventListenersForTypeList(creatorFormId, "for-creator-type", "creator-arrow-select", "creator-selected-type", "creator-current-type");
setEventListenersForTypeList(editorFormId, "for-editor-type", "editor-arrow-select", "editor-selected-type", "editor-current-type");

function manageFormButtonsInDependsOnType(inputType, formType){
	//Description: TEXT have only right answer.
	//Then need disable increment and decrement buttons for right and not right answers;
	const createRightAnswerButton = document.getElementById(`${formType}-${createRightAnswerButtonId}`);
	const eraseRightAnswerButton = document.getElementById(`${formType}-${eraseRightAnswerButtonId}`);
	const createWrongAnswerButton = document.getElementById(`${formType}-${createWrongAnswerButtonId}`);
	const eraseWrongAnswerButton = document.getElementById(`${formType}-${eraseWrongAnswerButtonId}`);

	if (inputType === "text"){
		fullEraseRightAnswerFields(formType);
		fullEraseWrongAnswerFields(formType);
		createRightAnswerButton.disabled = true;
		eraseRightAnswerButton.disabled = true;
		createWrongAnswerButton.disabled = true;
		eraseWrongAnswerButton.disabled = true;
	}
	//CHECKBOX have one or more right and not answers.
	//Then need enable increment and decrement buttons for right ande not right answers;
	else if (inputType == "checkbox"){
		createRightAnswerButton.disabled = false;
		eraseRightAnswerButton.disabled = false;
		createWrongAnswerButton.disabled = false;
		eraseWrongAnswerButton.disabled = false;
	}
	//RADIO have only one right answer and more not right answers.
	//Then need enable increment and decrement buttons for only not right answers;
	else if (inputType == "radio"){
		fullEraseRightAnswerFields(formType);
		createRightAnswerButton.disabled = true;
		eraseRightAnswerButton.disabled = true;
		createWrongAnswerButton.disabled = false;
		eraseWrongAnswerButton.disabled = false;
	} else if (inputType == "default") {
		makeDefaults(formType);
		fullEraseWrongAnswerFields(formType);
		fullEraseRightAnswerFields(formType);
		createRightAnswerButton.disabled = false;
		eraseRightAnswerButton.disabled = false;
		createWrongAnswerButton.disabled = false;
		eraseWrongAnswerButton.disabled = false;
		createWrongAnswerButton.click();
	} else {
		throw new Error("Invalid type of answer!");
	}
}

function makeDefaults(formType){
	document.getElementById(`${formType}-selected-type`).value = "";
	document.getElementById(`${formType}-current-type`).innerHTML = "Select type...";
}

function setEventListenerForCreateAnswerButton(targetButtonId, formId, className, buttonsId){
	const targetButton = document.getElementById(targetButtonId);
	targetButton.addEventListener("click", (event) => {
		event.preventDefault();
		const oneKindInputs = document.getElementsByClassName(`${formId}-${className}`);
		if (oneKindInputs.length <= 5){
			const isWrongAnswer = className === wrongAnswerClass;
			const newInput = createNewInputElement("text", formId, isWrongAnswer);

			const form = document.getElementById(`question-${formId}`);
			form.insertBefore(newInput, document.getElementById(`${formId}-${buttonsId}`));
		}
	}, false);
}

function createNewInputElement(type, formId, isWrongAnswer){
	const newInput = document.createElement("input");
	newInput.type = type;
	if (isWrongAnswer){
		newInput.name = wrongAnswerInputName;
		newInput.classList.add(`${formId}-${wrongAnswerClass}`);
		newInput.placeholder = "Enter the wrong answer";
	} else {
		newInput.name = rightAnswerInputName;
		newInput.classList.add(`${formId}-${rightAnswerClass}`);
		newInput.placeholder = "Enter the right answer";
	}
	setEventListenersForInputElements(newInput);

	return newInput;
}

setEventListenerForCreateAnswerButton(`${creatorFormId}-${createWrongAnswerButtonId}`,
									  creatorFormId,
									  wrongAnswerClass,
									  buttonsForWrongAnswers);
setEventListenerForCreateAnswerButton(`${creatorFormId}-${createRightAnswerButtonId}`,
									creatorFormId,
									rightAnswerClass,
									buttonsForRightAnswers);

setEventListenerForCreateAnswerButton(`${editorFormId}-${createWrongAnswerButtonId}`,
									  editorFormId,
									  wrongAnswerClass,
									  buttonsForWrongAnswers);
setEventListenerForCreateAnswerButton(`${editorFormId}-${createRightAnswerButtonId}`,
									editorFormId,
									rightAnswerClass,
									buttonsForRightAnswers);

function setEventListenerForEraseAnswer(type, targetButtonId, className){
	let targetButton = document.getElementById(`${type}-${targetButtonId}`);
	targetButton.addEventListener("click", (event) => {
		event.preventDefault();
		let oneKindInputs = document.getElementsByClassName(`${type}-${className}`);
		if (oneKindInputs.length == 0){
			//pass
		} else if (oneKindInputs[0].name === wrongAnswerInputName){
			if (oneKindInputs.length > 0){
				oneKindInputs[oneKindInputs.length - 1].remove();
			}
		} else {
			if (oneKindInputs.length > 1){
				oneKindInputs[oneKindInputs.length - 1].remove();
			}
		}
	}, false);
	
}

setEventListenerForEraseAnswer(creatorFormId, eraseWrongAnswerButtonId, wrongAnswerClass);
setEventListenerForEraseAnswer(editorFormId, eraseWrongAnswerButtonId, wrongAnswerClass);

setEventListenerForEraseAnswer(creatorFormId, eraseRightAnswerButtonId, rightAnswerClass);
setEventListenerForEraseAnswer(editorFormId, eraseRightAnswerButtonId, rightAnswerClass);