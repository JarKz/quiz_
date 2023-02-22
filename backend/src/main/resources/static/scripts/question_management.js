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

function showQuestionCreatorForm(quizId){
    let elem = document.getElementById("question-creator");
    elem.style.display = "flex";
    for (let child of elem.childNodes){
        if (child.name == "quiz_id"){
            child.value = quizId;
        }
    }
}

function showQuestionEditorForm(quizId, questionId, content, type, rightAnswer, wrongAnswers){
    let elem = document.getElementById("question-editor");
    elem.style.display = "flex";
    fullEraseWrongAnswerFields();
    if (wrongAnswers.length > 1){
        let totalNeededToAdd = wrongAnswers.length - 1;
        let button = document.getElementById("editor-new-wrong-answer");
        for (let count = 0; count < totalNeededToAdd; count++){
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
        } else if (child.name == "right-answer"){
            child.value = rightAnswer;
        } else if (child.name == "wrong-answers"){
            child.value = wrongAnswers.pop();
        }
    }
    document.getElementById("editor-selected-type").value = type.toLowerCase();
    document.getElementById("editor-current-type").innerHTML = type.slice(0,1) + type.slice(1).toLowerCase();
}

function fullEraseWrongAnswerFields(){
    let button = document.getElementById("erase-in-editor-wrong-answer");
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

function setEventListenersForTypeList(className, arrowElementId, destinationId, textDestinationId){
	const selectTypeElements = document.getElementsByClassName(className);
	
	for (const selectType of selectTypeElements){
		selectType.addEventListener("click", (event) => {
			const currentTarget = event.currentTarget;
			const destinationElement = document.getElementById(destinationId);
			destinationElement.value = currentTarget.innerHTML;
			const otherDestinationElement = document.getElementById(textDestinationId);
			otherDestinationElement.innerHTML = currentTarget.innerHTML;
			
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

setEventListenersForTypeList("for-creator-type", "creator-arrow-select", "creator-selected-type", "creator-current-type");
setEventListenersForTypeList("for-editor-type", "editor-arrow-select", "editor-selected-type", "editor-current-type");

function setEventListenerForWrongAnswerButton(buttonId, classNameWrongAnswer, formId, buttonsId){
	let wrongAnswerInputField = document.getElementById(buttonId);
	wrongAnswerInputField.addEventListener("click", (event) => {
		event.preventDefault();
		let wrongAnswerInputs = document.getElementsByClassName(classNameWrongAnswer);
		if (wrongAnswerInputs.length < 5){
			let newInput = document.createElement("input");
			newInput.type = "text";
			newInput.name = "wrong-answers";
			newInput.classList.add(classNameWrongAnswer);
			newInput.placeholder = "Enter the wrong answer";
			setEventListenersForInputElements(newInput);
	
			let formEditor = document.getElementById(formId);
			formEditor.insertBefore(newInput, document.getElementById(buttonsId));
		}
	}, false);
}

setEventListenerForWrongAnswerButton("creator-new-wrong-answer",
									  "creator-wrong-answer",
									  "question-creator",
									  "create-erase-wrong-answers-button-in-creator");
									  
setEventListenerForWrongAnswerButton("editor-new-wrong-answer",
									  "editor-wrong-answer",
									  "question-editor",
									  "create-erase-wrong-answers-button-in-editor");


function setEventListenerForEraseWrongAnswerButton(buttonId, classNameWrongAnswer){
	let eraseWrongAnswerButton = document.getElementById(buttonId);
	eraseWrongAnswerButton.addEventListener("click", (event) => {
	    event.preventDefault();
	    let wrongAnswerInputs = document.getElementsByClassName(classNameWrongAnswer);
	    if (wrongAnswerInputs.length > 1){
	        wrongAnswerInputs[wrongAnswerInputs.length - 1].remove();
	    }
	}, false);
	
}

setEventListenerForEraseWrongAnswerButton("erase-in-creator-wrong-answer", "creator-wrong-answer");
setEventListenerForEraseWrongAnswerButton("erase-in-editor-wrong-answer", "editor-wrong-answer");