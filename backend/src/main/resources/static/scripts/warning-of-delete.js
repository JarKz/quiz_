const deleteButtons = document.getElementsByClassName("delete-button");

for (const deleteButton of deleteButtons){
	deleteButton.addEventListener("click", (event) => {
		event.preventDefault();
		
		let hiddenButton = document.createElement("button");
		hiddenButton.classList.add("hidden-delete-button");
		hiddenButton.id = "hidden-delete-button";
		event.currentTarget.parentNode.insertBefore(hiddenButton, event.currentTarget);
		
		const formDeleteWarning = document.getElementById("delete-warning");
		formDeleteWarning.style.display = "flex";
	}, false);
}

const acceptButtonForDelete = document.getElementById("accept-button-for-delete");

acceptButtonForDelete.addEventListener("click", () => {
	document.getElementById("hidden-delete-button").click();
}, false);

const declineButtonForDelete = document.getElementById("decline-button-for-delete");

declineButtonForDelete.addEventListener("click", () => {
	document.getElementById("hidden-delete-button").remove();
	setTimeout(() => {
		document.getElementById("delete-warning").style.display = "none";
	}, 200);
	
}, false);