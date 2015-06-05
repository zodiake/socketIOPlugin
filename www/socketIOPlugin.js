var socketio = {
    connect: function() {
        cordova.exec(
            function(data) {
                alert(data);
            },
            null,
            'SocketIOPlugin',
            'connect', []
        );
    }
};

module.exports = socketio;
