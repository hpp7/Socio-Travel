package com.cockpunchers.commutator2;

import java.util.Date;

public class ChatMessage {
    private boolean incoming;
    private String text;
    private Date time;
    private String sender;

    public ChatMessage(String text, Date time, boolean incoming) {
        this(text, null, time, incoming);
    }

    public ChatMessage(String text, String sender, Date time, boolean incoming) {
        this.text = text;
        this.sender = sender;
        this.time = time;
        this.incoming = incoming;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public String getText() {
    	text = text.replace("&apos;", "'");
    	text = text.replace("&quot;","\"");
    	text = text.replace("&amp;", "&");
    	text = text.replace("&lt;", "<");
    	text = text.replace("&gt;", ">");
    	return text;
    }

    public Date getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }
}
