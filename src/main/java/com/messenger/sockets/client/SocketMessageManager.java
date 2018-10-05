package com.messenger.sockets.client;
// SocketMessageManager communicates with a ChatMessengerServer using
// Sockets and MulticastSockets.

import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.messenger.MessageListener;
import com.messenger.MessageManager;

import static com.messenger.sockets.SocketMessengerConstants.*;

public class SocketMessageManager implements MessageManager
{
   private Socket clientSocket; // Socket for outgoing messages
   private String serverAddress; // ChatMessengerServer address
   private PacketReceiver receiver; // receives multicast messages
   private boolean connected = false; // connection status
   private ExecutorService serverExecutor; // executor for server
   
   public SocketMessageManager( String address )
   {
      serverAddress = address; // store server address
      serverExecutor = Executors.newCachedThreadPool();
   } // end SocketMessageManager constructor
   
   // connect to server and send messages to given MessageListener
   public void connect( MessageListener listener ) 
   {
      if ( connected )
         return; // if already connected, return immediately

      try // open Socket connection to ChatMessengerServer
      {
         clientSocket = new Socket( 
            InetAddress.getByName( serverAddress ), SERVER_PORT );

         // create Runnable for receiving incoming messages
         receiver = new PacketReceiver( listener );
         serverExecutor.execute( receiver ); // execute Runnable
         connected = true; // update connected flag
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method connect
   
   // disconnect from server and unregister given MessageListener
   public void disconnect( MessageListener listener ) 
   {
      if ( !connected )
         return; // if not connected, return immediately
      
      try // stop listener and disconnect from server
      {     
         // notify server that client is disconnecting
         Runnable disconnecter = new MessageSender( clientSocket, "",
            DISCONNECT_STRING );         
         Future disconnecting = serverExecutor.submit( disconnecter );         
         disconnecting.get(); // wait for disconnect message to be sent
         receiver.stopListening(); // stop receiver
         clientSocket.close(); // close outgoing Socket
      } // end try
      catch ( ExecutionException exception ) 
      {
         exception.printStackTrace();
      } // end catch
      catch ( InterruptedException exception ) 
      {
         exception.printStackTrace();
      } // end catch
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
     
      connected = false; // update connected flag
   } // end method disconnect
   
   // send message to server
   public void sendMessage( String from, String message ) 
   {
      if ( !connected )
         return; // if not connected, return immediately
      
      // create and start new MessageSender to deliver message
      serverExecutor.execute( 
         new MessageSender( clientSocket, from, message) );
   } // end method sendMessage
}
