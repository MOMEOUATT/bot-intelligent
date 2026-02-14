export interface Message {
    id?: number;
    content: string;
    isBot: boolean;
    createdAt: Date;
}

export interface MessageDTO {
    conversationId: number;
    content: string;
}
