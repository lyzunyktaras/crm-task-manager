import SockJS from "sockjs-client";
import {Client} from "@stomp/stompjs";

class WebSocketService {
    constructor() {
        this.client = null;
        this.subscriptions = new Map();
    }

    connect(onGlobalMessageCallback) {
        const token = localStorage.getItem("token");

        this.client = new Client({
            webSocketFactory: () => new SockJS(`http://localhost:8080/ws?token=${token}`),
            debug: (str) => console.log(str),
            onConnect: () => {
                console.log("Connected to WebSocket");

                this.client.subscribe("/user/queue/notification", (message) => {
                    const parsedMessage = JSON.parse(message.body);
                    onGlobalMessageCallback(parsedMessage);
                });
            },
            onStompError: (frame) => {
                console.error("STOMP error:", frame.headers["message"]);
                console.error("Details:", frame.body);
            },
        });
        this.client.activate();
    }

    subscribeToTaskChat(taskId, onMessageCallback) {
        if (this.client && this.client.connected) {
            const destination = `/topic/tasks/${taskId}/chat`;
            if (!this.subscriptions.has(taskId)) {
                const subscription = this.client.subscribe(destination, (message) => {
                    const parsedMessage = JSON.parse(message.body);
                    onMessageCallback(parsedMessage);
                });
                this.subscriptions.set(taskId, subscription);
            }
        } else {
            console.error("WebSocket client is not connected.");
        }
    }

    unsubscribeFromTaskChat(taskId) {
        if (this.subscriptions.has(taskId)) {
            const subscription = this.subscriptions.get(taskId);
            subscription.unsubscribe();
            this.subscriptions.delete(taskId);
        }
    }

    sendMessageToTaskChat(taskId, message) {
        if (this.client && this.client.connected) {
            const destination = `/topic/tasks/${taskId}/chat`;
            console.warn(message)
            this.client.publish({
                destination: destination,
                body: JSON.stringify(message),
            });
        } else {
            console.error("WebSocket client is not connected.");
        }
    }

    disconnect() {
        if (this.client) {
            this.client.deactivate();
            this.subscriptions.clear();
        }
    }
}

const webSocketService = new WebSocketService();
export default webSocketService;
