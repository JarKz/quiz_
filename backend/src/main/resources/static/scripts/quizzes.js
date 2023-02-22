const currentPage = document.getElementById("current-page").value;

const buttonToPrevPage = document.getElementById("button-to-previous-page");
const buttonToNextPage = document.getElementById("button-to-next-page");

if (parseInt(currentPage) <= 1){
    buttonToPrevPage.disabled = true;
}

if (parseInt(currentPage) >= parseInt(totalPages)){
    buttonToNextPage.disabled = true;
}

buttonToPrevPage.addEventListener("click", (event) => {
    const leftArrow = event.currentTarget.getElementsByClassName("left-arrow")[0];
    leftArrow.classList.add("moving-to-left-arrow");

    const inputElement = document.getElementById("current-page");
    inputElement.value = parseInt(inputElement.value) - 1;
}, false);

buttonToNextPage.addEventListener("click", (event) => {
    const rightArrow = event.currentTarget.getElementsByClassName("right-arrow")[0];
    rightArrow.classList.add("moving-to-right-arrow");

    const inputElement = document.getElementById("current-page");
    inputElement.value = parseInt(inputElement.value) + 1;
}, false);

const inputElement = document.getElementById("current-page");

inputElement.addEventListener("keyup", (event) => {
    if (event.code === "Enter"){
        document.getElementById("moving-on-pages").submit();
    }
}, false);