package com.messenger;
// MessageListener is an interface for classes that wish to
// receive new chat messages.

public interface MessageListener 
{
   // receive new chat message
   public void messageReceived( String from, String message );
}
