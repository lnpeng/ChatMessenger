package com.messenger.sockets.client;
// Sends a message to the chat server in a separate Runnable.

import java.io.IOException;
import java.util.Formatter;
import java.net.Socket;

import static com.messenger.sockets.SocketMessengerConstants.*;

public class MessageSender implements Runnable 
{
   private Socket clientSocket; // Socket over which to send message
   private String messageToSend; // message to send

   public MessageSender( Socket socket, String userName, String message ) 
   {
      clientSocket = socket; // store Socket for client
      
      // build message to be sent
      messageToSend = userName + MESSAGE_SEPARATOR + message;
   } // end MessageSender constructor
   
   // send message and end
   public void run() 
   {
      try // send message and flush formatter
      {     
         Formatter output =
            new Formatter( clientSocket.getOutputStream() );
         output.format( "%s\n", messageToSend ); // send message
         output.flush(); // flush output
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method run
} // end class MessageSender
