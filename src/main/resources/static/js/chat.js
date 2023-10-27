function loadRooms() {
    fetch("/chat/rooms")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            let roomsElement = document.getElementById("rooms");
            data.forEach(message => {
                let roomElement = document.createElement("li");
                roomElement.textContent = "Chat with " + message.senderId;
                roomElement.addEventListener("click", function() {
                    location.href = "/chat/start/" + message.id; // 해당 상품에 대한 대화방 입장
                });
                roomsElement.appendChild(roomElement);
            });
        }).catch(error => {
        console.log('There was a problem with the fetch operation:', error.message);
    });
}

window.onload = function() {
    loadRooms();
    // 기존의 웹소켓 연결 로직 (만약 있을 경우)
};
