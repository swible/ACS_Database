var selectBtn;
var executeBtn;
var textInput;
var resultLbl;
var queryResultTable;

window.addEventListener("load", init, false);

function init() {

    selectBtn = document.getElementById("selectQueryBtn");
    selectBtn.addEventListener("click",selectQuery);

    executeBtn = document.getElementById("executeBtn");
    executeBtn.addEventListener("click",executeQuery);

    textInput = document.getElementById("textinput");
    resultLbl = document.getElementById("resultLbl");
    queryResultTable = document.getElementById("queryResultTable");
}

function executeQuery() {
    resultLbl.innerHTML = null;
    var xhr = new XMLHttpRequest();
    var url = "/sqlExecute";
    xhr.open("POST", url, true);
    // xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                if (json.ok) {
                    resultLbl.innerHTML = "Запрос успешно выполнен";
                } else {
                    resultLbl.innerHTML = "Ошибка при выполнении запроса: " + json.errorText;
                }
            } else {
                resultLbl.innerHTML = "Http error status: " + xhr.status;
            }
        }
    };
    xhr.send(textInput.value);
}

function selectQuery() {
    resultLbl.innerHTML = null;
    queryResultTable.innerHTML = "";
    var xhr = new XMLHttpRequest();
    var url = "/sqlSelect";
    xhr.open("POST", url, true);
    // xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                if (json.ok) {
                    var table = queryResultTable;
                    var row = table.insertRow();
                    for( var i = 0; i < json.colNames.length; i++ ) {
                        var cell = row.insertCell();
                        cell.appendChild(document.createTextNode(json.colNames[i]));
                    }

                    for( var i = 0; i < json.colValues.length; i++ ) {

                        var child = json.colValues[i];
                        var row = table.insertRow();
                        Object.keys(child).forEach(function(k) {
                            console.log(k);
                            var cell = row.insertCell();
                            cell.appendChild(document.createTextNode(child[k]));
                        })
                    }
                } else {
                    resultLbl.innerHTML = "Ошибка при выполнении запроса: " + json.errorText;
                }
            } else {
                resultLbl.innerHTML = "Http error status: " + xhr.status;
            }
        }
    };
    xhr.send(textInput.value);
}
