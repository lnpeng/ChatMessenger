package com.messenger.sockets.server;
// MulticastSender broadcasts a chat message using a multicast datagram.

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.messenger.sockets.SocketMessengerConstants.*;

public class MulticastSender implements Runnable
{   
   private byte[] messageBytes; // message data
   
   public MulticastSender( byte[] bytes ) 
   { 
      messageBytes = bytes; // create the message
   } // end MulticastSender constructor

   // deliver message to MULTICAST_ADDRESS over DatagramSocket
   public void run() 
   {
      try // deliver message
      {         
         // create DatagramSocket for sending message
         DatagramSocket socket = 
            new DatagramSocket( MULTICAST_SENDING_PORT );
         
         // use InetAddress reserved for multicast group
         InetAddress group = InetAddress.getByName( MULTICAST_ADDRESS );
         
         // create DatagramPacket containing message
         DatagramPacket packet = new DatagramPacket( messageBytes,
            messageBytes.length, group, MULTICAST_LISTENING_PORT );
         
         socket.send( packet ); // send packet to multicast group
         socket.close(); // close socket
      } // end try
      catch ( IOException ioException ) 
      { 
         ioException.printStackTrace();
      } // end catch
   } // end method run
}
