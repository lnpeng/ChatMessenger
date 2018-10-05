package com.messenger;
// MessageManger is an interface for objects capable of managing
// communications with a message server.

import com.messenger.MessageListener;

public interface MessageManager
{      
   // connect to message server and route incoming messages
   // to given MessageListener
   public void connect( MessageListener listener );
   
   // disconnect from message server and stop routing
   // incoming messages to given MessageListener
   public void disconnect( MessageListener listener );
   
   // send message to message server
   public void sendMessage( String from, String message );  
}
