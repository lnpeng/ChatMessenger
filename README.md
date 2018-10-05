<h3>ChatMessenger</h3>

<ul>
  <li>Server class: ChatMessengerServer</li>

  <li>Client class: ChatMessenger</li>

  <li>Client sends connect request and messages to server via a uni-cast TCP connection</li>

  <li>The server sends received message to all clients in group via a multi-cast UDP connection</li>

  <li>Clients in group listen to a multi-cast address for new messages</li>
</ul>
