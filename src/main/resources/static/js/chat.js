function loadRooms() {
    fetch("/chat/rooms")
        .then(response => response.json())
        .then(data => {
            let roomsElement = document.getElementById("rooms");
            data.forEach(message => {
                let roomElement = document.createElement("li");
                roomElement.textContent = "Chat with " + message.senderId;
                roomElement.addEventListener("click", function() {
                    location.href = "/chat/start/" + message.productId; // 해당 상품에 대한 대화방 입장
                });
                roomsElement.appendChild(roomElement);
            });
        });
}

window.onload = function() {
    loadRooms();
    // 기존의 웹소켓 연결 로직 (만약 있을 경우)
};
