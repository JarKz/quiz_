const editButton = document.getElementById("edit-button");
const saveButton = document.getElementById("save-button");
const cancelButton = document.getElementById("cancel-button");

const inputNickname = document.getElementById("nickname");
const inputName = document.getElementById("name");

const oldNameAndNickname = {};

editButton.addEventListener("click", (event) => {
    event.preventDefault();

    oldNameAndNickname.nickname = inputNickname.value;
    inputNickname.disabled = false;
    oldNameAndNickname.name = inputName.value;
    inputName.disabled = false;

    saveButton.disabled = false;
    saveButton.classList.remove("disabled-button");
    cancelButton.disabled = false;
    cancelButton.classList.remove("disabled-button");
}, false);

cancelButton.addEventListener("click", (event) => {
    event.preventDefault();
    saveButton.disabled = true;
    saveButton.classList.add("disabled-button");

    inputNickname.value = oldNameAndNickname.nickname;
    inputNickname.disabled = true;
    inputName.value = oldNameAndNickname.name;
    inputName.disabled = true;

    cancelButton.disabled = true;
    cancelButton.classList.add("disabled-button");
}, false);

const navbarFields = document.getElementsByClassName("navbar-field");

const userInfoForm = document.getElementById("user-info");
const passwordForm = document.getElementById("password-change");
const roleForm = document.getElementById("role-change");

for (const navbarField of navbarFields){
    if (navbarField.classList.contains("user-info")){
        navbarField.addEventListener("click", (event) => {
            for (const fieldToRemoveClass of navbarFields){
                fieldToRemoveClass.classList.remove("active-field");
            }
            event.currentTarget.classList.add("active-field");
            userInfoForm.style.display = "grid";
            passwordForm.style.display = "none";
            roleForm.style.display = "none";
        }, false);
    } else if (navbarField.classList.contains("password")){
        navbarField.addEventListener("click", (event) => {
            for (const fieldToRemoveClass of navbarFields){
                fieldToRemoveClass.classList.remove("active-field");
            }
            event.currentTarget.classList.add("active-field");
            userInfoForm.style.display = "none";
            passwordForm.style.display = "grid";
            roleForm.style.display = "none";
        }, false);
    } else if (navbarField.classList.contains("role")){
        navbarField.addEventListener("click", (event) => {
            for (const fieldToRemoveClass of navbarFields){
                fieldToRemoveClass.classList.remove("active-field");
            }
            event.currentTarget.classList.add("active-field");
            userInfoForm.style.display = "none";
            passwordForm.style.display = "none";
            roleForm.style.display = "grid";
        }, false);
    } else {
        throw new Error("Form not exists!");
    }
}