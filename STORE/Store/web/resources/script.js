
var serverHostName = window.location.hostname;
var serverProtocolName = window.location.protocol;
var portName = window.location.port;
if (portName.length == 0) {
  portName = "8080";
}
if (serverHostName === "localhost")
{
    serverPath = serverProtocolName + "//" + serverHostName + ":" + portName + "/Store/";
}
else
{
    serverPath = serverProtocolName + "//" + serverHostName;
}

function serverConnectFunc(serverUrl, jsonData) {
    $.ajax({
        url: serverUrl + "/",
        type: 'POST',
        data: jsonData,

        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}

function showAllNames()
{
    var jsonData = new Object();
    jsonData.command = "0";

    serverConnectFunc(serverPath, JSON.stringify(jsonData));
}

function addGroup()
{
    var jsonData = new Object();
    jsonData.command = "1";
    jsonData.name = $('#name').val();
    jsonData.description = $('#description').val();

    let serverUrl = window.location.href;
    serverConnectFunc(serverUrl, JSON.stringify(jsonData));
}

function deleteGroup() {
    let jsonData = new Object();
    jsonData.name = $('#name').val();
    let jsonStr = JSON.stringify(jsonData);

    $.ajax({
        url: "http://localhost:8080/Store/delete",
        type: 'DELETE',
        data: jsonStr,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}

function updateProduct(){
    var jsonData = new Object();
    jsonData.command = "1";
    jsonData.oldname = $('#oldname').val();
    jsonData.gname = $('#gname').val();
    jsonData.name = $('#name').val();
    jsonData.description = $('#description').val();
    jsonDataP.manufacturer = $('#manufacturer').val();
    jsonDataP.amount = $('#amount').val();
    jsonDataP.price = $('#price').val();

    let jsonStr = JSON.stringify(jsonData);
    $.ajax({
        url: "http://localhost:8080/Store/updateProduct",
        type: 'POST',
        data: jsonStr,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert("There is nothing to update. The product " + $('#oldname').val() + " not found");
        }
    });
}

function updateGroup() {
    var jsonData = new Object();
    jsonData.command = "1";
    jsonData.gname = $('#gname').val();
    jsonData.name = $('#name').val();
    jsonData.description = $('#description').val();
    let jsonStr = JSON.stringify(jsonData);
    $.ajax({
        url: "http://localhost:8080/Store/update",
        type: 'POST',
        data: jsonStr,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert("There is nothing to update. The group " + $('#gname').val() + " not found");
        }
    });
}

function createProduct() {
    let jsonDataP = new Object();
    jsonDataP.gname = $('#gname').val();
    jsonDataP.name = $('#name').val();
    jsonDataP.description = $('#description').val();
    jsonDataP.manufacturer = $('#manufacturer').val();
    jsonDataP.amount = $('#amount').val();
    jsonDataP.price = $('#price').val();

    let jsonStrP = JSON.stringify(jsonDataP);

    $.ajax({
        url: "http://localhost:8080/Store/CreateProduct",
        type: 'POST',
        data: jsonStrP,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}

function deleteProduct() {
    let jsonData = new Object();
    jsonData.name = $('#name').val();
    let jsonStr = JSON.stringify(jsonData);

    $.ajax({
        url: "http://localhost:8080/Store/deleteProduct",
        type: 'DELETE',
        data: jsonStr,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}

function addToProduct() {
    let jsonDataP = new Object();
    jsonDataP.name = $('#name').val();
    jsonDataP.amountToAdd = $('#amountToAdd').val();

    let jsonStrP = JSON.stringify(jsonDataP);

    $.ajax({
        url: "http://localhost:8080/Store/addToProduct",
        type: 'POST',
        data: jsonStrP,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}

function writeOffFromProduct() {
    let jsonDataP = new Object();
    jsonDataP.name = $('#name').val();
    jsonDataP.amountToWriteOff = $('#amountToWriteOff').val();

    let jsonStrP = JSON.stringify(jsonDataP);

    $.ajax({
        url: "http://localhost:8080/Store/writeOffFromProduct",
        type: 'POST',
        data: jsonStrP,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}



function showProductOfThisGroup(){
    let jsonDataP = new Object();
    jsonDataP.name = $('#name').val();
    let jsonStrP = JSON.stringify(jsonDataP);
    $.ajax({
        url: "http://localhost:8080/Store/allProductsInGroup",
        type: 'POST',
        data: jsonStrP,
        dataType: 'json',
        async: true,

        success: function (event) {
            switch (event["answer"])
            {
                case "ok":
                    alert("success");
                    break;
            }
        },
        error: function (xhr, status, error) {
            alert(error);
        }
    });
}



function changeFunc() {
    var selectBox = document.getElementById("selectBox");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;

    var jsonData = new Object();
    jsonData.group = selectedValue;

    let serverUrl = window.location.href;
    serverConnectFunc(serverUrl, JSON.stringify(jsonData));
}