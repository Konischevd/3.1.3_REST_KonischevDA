/**
 * Функция запроса данных с сервера
 */
function sendRequest(url, method, body) {
    const headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: method,
        body: body,
        headers: headers
    }).then(response => {
        if (response.ok) {
            return response.json();
        } else {
            alert("Ошибка HTTP: " + response.status);
        }
    })
}

/**
 * Функция создания ячейки в ряду
 */
function cellCreation(row, object, field) {
    let cell = document.createElement('td');
    cell.textContent = object[field];
    row.append(cell);
}

/**
 * Функция вставки данных всех пользователей в таблицу
 */
function insertUsersRows (arrayOfUsers) {
    let myButtons =
        '<td><a class="btn" data-action="edit" style="color: white; background-color: #17a2b8">Edit</a></td>\n' +
        '<td><a class="btn" data-action="delete" style="color: white; background-color: #dc3545">Delete</a></td>';

    let allUsersTableBody = document.getElementById('allUsersTableBody');

    for (let user of arrayOfUsers) {
        let row = document.createElement('tr');

        cellCreation(row, user, 'id');
        cellCreation(row, user, 'firstName');
        cellCreation(row, user, 'lastName');
        cellCreation(row, user, 'age');
        cellCreation(row, user, 'email');
        cellCreation(row, user, 'roles');

        row.insertAdjacentHTML('beforeend', myButtons);

        allUsersTableBody.append(row);
    }
}

/**
 * Функция вставки данных текущего пользователя в таблицу
 */
function insertUserRow (user) {
    let row = document.getElementById('currentUserTableBodyRow');
    cellCreation(row, user, 'id');
    cellCreation(row, user, 'firstName');
    cellCreation(row, user, 'lastName');
    cellCreation(row, user, 'age');
    cellCreation(row, user, 'email');
    cellCreation(row, user, 'roles');
}

/**
 * Функция - достать куки
 */
function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}


// Отправляем запрос на всех пользователей, вставляем инфо в таблицу
if (getCookie('isAdmin') === 'true') {
    sendRequest('http://localhost:8080/users', 'GET')
        .then(data => {
            insertUsersRows(data);
        });
}

// Отправляем запрос на текущего пользователя, вставляем инфо в таблицу
sendRequest(`http://localhost:8080/users/${getCookie('UserId')}`, 'GET')
    .then(data => {
        insertUserRow(data);
    });

// Отправляем POST запрос - создание нового пользователя
// Обработка формы - создание нового пользователя
function newUser(form) {
    let user = {};
    user.firstName = form.firstName.value;
    user.lastName = form.lastName.value;
    user.age = Number(form.age.value);
    user.email = form.email.value;
    user.password = form.password.value;
    user.roles = Array.from(form.roles)
        .filter(option => option.selected)
        .map(option => option.value);

    alert(user);
    console.log(user);

    let jsonUser = JSON.stringify(user);
    alert(jsonUser);
    console.log(jsonUser)


    sendRequest('http://localhost:8080/users', 'POST', jsonUser)
        .then();
}


