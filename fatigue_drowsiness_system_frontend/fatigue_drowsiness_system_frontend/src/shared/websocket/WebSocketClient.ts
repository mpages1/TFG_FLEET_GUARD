import {Client, IMessage} from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient: Client | null = null;

export const connectWebSocket = (
    userId: number,
    onStatusUpdate: (status: any) => void,
    onMetricsUpdate?: (metrics: any) => void
) => {
    const socket = new SockJS('http://localhost:8080/ws');

    stompClient = new Client({
        webSocketFactory: () => socket as WebSocket,
        reconnectDelay: 5000,
        connectHeaders: {
            userId: String(userId),
        },
        onConnect: () => {
            console.log('✅ WebSocket connected');

            stompClient?.subscribe(`/topic/driver/${userId}/status`, (message: IMessage) => {
                const body = JSON.parse(message.body);
                console.log("Status message:", body);
                onStatusUpdate(body);
            });

            if (onMetricsUpdate) {
                stompClient?.subscribe(`/topic/driver/${userId}/metrics`, (message: IMessage) => {
                    const body = JSON.parse(message.body);
                    console.log("Metrics message:", body);
                    onMetricsUpdate(body);
                });
            }
        },
        onStompError: (frame) => {
            console.error('STOMP error:', frame.headers['message'], frame.body);
        }
    });

    stompClient.activate();
};

export const disconnectWebSocket = () => {
    if (stompClient && stompClient.connected) {
        stompClient.deactivate();
        console.log('WebSocket disconnected');
    }
};
