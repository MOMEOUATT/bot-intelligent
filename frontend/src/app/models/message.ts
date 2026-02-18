export interface Message {
    id: number;
    content: string;
    isBot: boolean;
    liked?: boolean;
    disliked?: boolean;
    createdAt: Date;
}

export interface MessageDTO {
    conversationId: number;
    content: string;
}
