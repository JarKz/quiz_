const buttonsForEffect = document.getElementsByClassName("ripple-effect");

for (const buttonForEffect of buttonsForEffect){
	buttonForEffect.addEventListener("click", (event) => {
		const buttonRectangle = event.currentTarget.getBoundingClientRect();
	
		const circle = document.createElement("span");
		const diameter = Math.max(buttonRectangle.width, buttonRectangle.height);
		const radius = diameter / 2;
	
		circle.style.width = circle.style.height = `${diameter}px`;
		circle.style.left = `${event.clientX - (buttonRectangle.left + radius)}px`;
		circle.style.top = `${event.clientY - (buttonRectangle.top + radius)}px`;
		circle.classList.add("ripple");
	
		const ripple = event.currentTarget.getElementsByClassName("ripple")[0];
	
		if (ripple) {
		  ripple.remove();
		}
		
		buttonForEffect.appendChild(circle);
	 });
}
