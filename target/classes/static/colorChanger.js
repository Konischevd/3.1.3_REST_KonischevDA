function bgColorChanger(element, maxDelta, maxColors, timeOut) {
    let rand = (min, max) => Math.round(Math.random() * (max - min) + min);
    let red = rand(0, 255);
    let green = rand(0, 255);
    let blue = rand(0, 255);

    let rRate = rand(- maxDelta/2, maxDelta/2);
    let gRate = rand(- maxDelta/2, maxDelta/2);
    let bRate = rand(- maxDelta/2, maxDelta/2);

    let setBG = (element, r, g, b) => element.style.background = `rgb(${r}, ${g}, ${b})`;

    for (let i = 0; i <= maxColors ; i++) {
        setTimeout(setBG, i * timeOut, element, red, green, blue);

        if (red + rRate < 0 || red + rRate > 255) {
            rRate = -rRate;
        }
        if (green + gRate < 0 || green + gRate > 255) {
            gRate = -gRate;
        }
        if (blue + bRate < 0 || blue + bRate > 255) {
            bRate = -bRate;
        }

        red += rRate;
        green += gRate;
        blue += bRate;
    }
    setTimeout(setBG, maxColors * timeOut + timeOut, element, 255, 255, 255);
}

// Мигающий при фон при клике
// tbody.addEventListener('click', {
//     handleEvent(evt) {
//         bgColorChanger(evt.target.closest('tr'), 20, 60, 50);
//     }
// })