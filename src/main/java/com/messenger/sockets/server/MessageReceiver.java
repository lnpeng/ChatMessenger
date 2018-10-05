package com.messenger.sockets.server;
// MessageReceiver is a Runnable that listens for messages from a 
// particular client and delivers messages to a MessageListener.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import com.messenger.MessageListener;
import static com.messenger.sockets.SocketMessengerConstants.*;

public class MessageReceiver implements Runnable
{
   private BufferedReader input; // input stream
   private MessageListener messageListener; // message listener
   private boolean keepListening = true; // when false, ends runnable
   
   // MessageReceiver constructor
   public MessageReceiver(MessageListener listener, Socket clientSocket )
   {
      // set listener to which new messages should be sent
      messageListener = listener;
      
      try 
      {
         // set timeout for reading from client
         clientSocket.setSoTimeout( 5000 ); // five seconds
         
         // create BufferedReader for reading incoming messages
         input = new BufferedReader( new InputStreamReader( 
            clientSocket.getInputStream() ) );
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end MessageReceiver constructor
   
   // listen for new messages and deliver them to MessageListener
   public void run() 
   {    
      String message; // String for incoming messages
      
      // listen for messages until stopped
      while ( keepListening ) 
      {   
         try 
         {            
            message = input.readLine(); // read message from client
         } // end try
         catch ( SocketTimeoutException socketTimeoutException ) 
         {
            continue; // continue to next iteration to keep listening
         } // end catch
         catch ( IOException ioException ) 
         {
            ioException.printStackTrace();            
            break;
         } // end catch

         // ensure non-null message
         if ( message != null ) 
         {
            // tokenize message to retrieve user name and message body
            StringTokenizer tokenizer = new StringTokenizer( 
               message, MESSAGE_SEPARATOR );

            // ignore messages that do not contain a user
            // name and message body
            if ( tokenizer.countTokens() == 2 ) 
            {
               // send message to MessageListener
               messageListener.messageReceived( 
                  tokenizer.nextToken(), // user name
                  tokenizer.nextToken() ); // message body
            } // end if
            else
            {
               // if disconnect message received, stop listening
               if ( message.equalsIgnoreCase(
                  MESSAGE_SEPARATOR + DISCONNECT_STRING ) ) 
                  stopListening();
            } // end else
         } // end if
      } // end while  
      
      try
      {         
         input.close(); // close BufferedReader (also closes Socket)
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();     
      } // end catch 
   } // end method run
   
   // stop listening for incoming messages
   public void stopListening() 
   {
      keepListening = false;
   } // end method stopListening
}
