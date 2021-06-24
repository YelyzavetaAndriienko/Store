
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

function goToDeleteGroup() {

}

function createProduct() {
    var jsonData = new Object();
    jsonData.command = "1";
    jsonData.groupId = $('#groupId').val();
    jsonData.name = $('#name').val();
    jsonData.description = $('#description').val();
    jsonData.manufacturer = $('#manufacturer').val();
    jsonData.amount = $('#amount').val();
    jsonData.price = $('#price').val();

    let serverUrl = window.location.href;
    serverConnectFunc(serverUrl, JSON.stringify(jsonData));
}

function changeFunc() {
    var selectBox = document.getElementById("selectBox");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;

    var jsonData = new Object();
    jsonData.group = selectedValue;

    let serverUrl = window.location.href;
    serverConnectFunc(serverUrl, JSON.stringify(jsonData));
}