package com.example.chatapplication

class Message {
    var message: String? = null
    var senderId: String? = null
    var groupId: String? = null
    var senderName: String? = null

    constructor() {}

    constructor(message: String?, senderId: String?, groupId: String?, senderName: String?) {
        this.message = message
        this.senderId = senderId
        this.groupId = groupId
        this.senderName = senderName
    }
    // Getter and Setter methods

}
