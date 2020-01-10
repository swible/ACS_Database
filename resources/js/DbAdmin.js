    var list = document.getElementById('table_select'),
  arr = [111, 222, 333, 444, 555],
  item = document.createElement('option');
for (var i = 0; i < arr.length; i++) {
  item.text = arr [i];
  list.appendChild(item.cloneNode(true));
}

function LoadTableList(){
    var list = document.getElementById('table_select'),
      arr = [111, 222, 333, 444, 555],
      item = document.createElement('option');
    for (var i = 0; i < arr.length; i++) {
      item.text = arr [i];
      list.appendChild(item.cloneNode(true));
    }
}

$(document).ready(

LoadTableList();
$("#table_select").on('click'){
    LoadTableList();
}

let chooseTableBtn = "chooseTableBtn";



)