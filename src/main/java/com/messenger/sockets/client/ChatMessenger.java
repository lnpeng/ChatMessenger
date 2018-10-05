package com.messenger.sockets.client;
// ChatMessenger is a chat application that uses a ClientGUI
// and SocketMessageManager to communicate with ChatMessengerServer.

import com.messenger.MessageManager;
import com.messenger.ClientGUI;

public class ChatMessenger
{   
   public static void main( String args[] ) 
   {
      MessageManager messageManager; // declare MessageManager
      
      if ( args.length == 0 )
         // connect to localhost
         messageManager = new SocketMessageManager( "localhost" );
      else
         // connect using command-line arg
         messageManager = new SocketMessageManager( args[ 0 ] );  
      
      // create GUI for SocketMessageManager
      ClientGUI clientGUI = new ClientGUI( messageManager );
      clientGUI.setSize( 300, 400 ); // set window size
      clientGUI.setResizable( false ); // disable resizing
      clientGUI.setVisible( true ); // show window
   }
}
