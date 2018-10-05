package com.messenger.sockets.client;
// PacketReceiver listens for DatagramPackets containing
// messages from a ChatMessengerServer.

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import com.messenger.MessageListener;
import static com.messenger.sockets.SocketMessengerConstants.*;

public class PacketReceiver implements Runnable 
{
   private MessageListener messageListener; // receives messages
   private MulticastSocket multicastSocket; // receive broadcast messages
   private InetAddress multicastGroup; // InetAddress of multicast group
   private boolean keepListening = true; // terminates PacketReceiver
   
   public PacketReceiver( MessageListener listener ) 
   {
      messageListener = listener; // set MessageListener
      
      try // connect MulticastSocket to multicast address and port 
      {
         // create new MulticastSocket
         multicastSocket = new MulticastSocket(
            MULTICAST_LISTENING_PORT );
         
         // use InetAddress to get multicast group
         multicastGroup = InetAddress.getByName( MULTICAST_ADDRESS );
         
         // join multicast group to receive messages
         multicastSocket.joinGroup( multicastGroup ); 
         
         // set 5 second timeout when waiting for new packets
         multicastSocket.setSoTimeout( 5000 );
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end PacketReceiver constructor
   
   // listen for messages from multicast group 
   public void run() 
   {          
      // listen for messages until stopped
      while ( keepListening ) 
      {
         // create buffer for incoming message
         byte[] buffer = new byte[ MESSAGE_SIZE ];

         // create DatagramPacket for incoming message
         DatagramPacket packet = new DatagramPacket( buffer, 
            MESSAGE_SIZE );

         try // receive new DatagramPacket (blocking call)
         {            
            multicastSocket.receive( packet ); 
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

         // put message data in a String
         String message = new String( packet.getData() );

         message = message.trim(); // trim whitespace from message

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
      } // end while

      try 
      {
         multicastSocket.leaveGroup( multicastGroup ); // leave group
         multicastSocket.close(); // close MulticastSocket
      } // end try
      catch ( IOException ioException )
      { 
         ioException.printStackTrace();
      } // end catch
   } // end method run
   
   // stop listening for new messages
   public void stopListening() 
   {
      keepListening = false;
   } // end method stopListening
}
