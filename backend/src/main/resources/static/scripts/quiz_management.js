document.addEventListener("click", (event) => {
	if (!recursiveCheckParentNodeForm(event.target) && ["button", "span"].indexOf(event.target.localName) === -1) {
        document.getElementById("quiz-creator").style.display = "none";
        document.getElementById("quiz-editor").style.display = "none";
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

function showQuizCreatorForm(){
    let elem = document.getElementById("quiz-creator");
    elem.style.display = "flex";
}

function showQuizEditorForm(id){
    let elem = document.getElementById("quiz-editor");
    for (let child of elem.childNodes){
	    elem.style.display = "flex";
        if (child.name == "id"){
            child.value = id;
        }
    }
}