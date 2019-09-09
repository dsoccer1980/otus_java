let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#create-form").show();
    } else {
        $("#create-form").hide();
    }
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        stompClient.subscribe('/topic/response', (user) => showUser(JSON.parse(user.body)));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendUser = () => {
    stompClient.send("/app/user", {}, JSON.stringify({'name': $("#nameId").val(), 'age': $("#ageId").val()}))
}

const getUsers = () => stompClient.send("/app/users", {}, {})

const showUser = (user) => {
    if (user) {
        $("#listUsers").append("<tr><td>" + user.id + "</td><td>" + user.name + "</td><td>" + user.age + "</td></tr>");
    }
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#addId").click(sendUser);
    $("#getId").click(getUsers);
    $("#create-form").hide();
});