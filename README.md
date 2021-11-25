# No Thanks Dev Day

The purpose of this Dev Day is to create a web socket implementation of the card game 'No Thanks'.
The server is a STOMP web socket server using sock js, so clients made with java, node,
or javascript are recommended.

Step 1 should be to read through some of the server code and understand what is going on.
Then the next step is to write a client and establish a connection with the server. The end goal
is to have a fully working client/server application that can get through an entire game.

If you accomplish making a working game in the day, you can try:
 - Making a nice UI for the game client
 - Creating a bot to play the game
 - Host the server and play with others
 
If you have never worked with web sockets, STOMP, or sock js before, see the Resources section
of this README for some helpful resources on those topics.

### Rules
Each turn, players have two options:
play one of their chips to avoid picking up the current face-up card
pick up the face-up card (along with any chips that have already been played on that card) 
and turn over the next card. 

However, the choices aren't so easy as players compete to have the lowest score at 
the end of the game. The deck of cards is numbered from 3 to 35, with each card counting 
for a number of points equal to its face value. Runs of two or more cards only count as 
the lowest value in the run - but nine cards are removed from the deck before starting, 
so be careful looking for connectors. Each chip is worth -1 point, but they can be even more 
valuable by allowing you to avoid drawing that unwanted card.

~ Source: boardgamegeeks.com

To watch a more hands-on explanation of the rules, see the following video:
https://www.youtube.com/watch?v=Lw2ua5B0TTM

### Endpoints
- /create
    - Create a new lobby. If the lobby has been created successfully, the user will receive
    a message on the `user/queue/lobby` destination. If there are problems creating
    the lobby, the user will receive a message of notification type `ERROR` at the same destination.

- /join
    - Join an existing lobby
    - After successfully joining, you will get a `JOINED_LOBBY` message on the `user/queue/lobby`
    destination. The payload of that message will be the gameId. It is important to start listening on the `/topic/game/{gameId}` 
    topic at this point to receive future socket messages. The gameId == joinCode
    - When another player joins, all players in the lobby will receive a `PLAYER_JOINED` message
    at `/user/queue/lobby` with the player's name as the payload.
    - If there are any problems joining the lobby, the user will receive a message
    with the notification type `ERROR`.

- /start
    - Starts the game if in a lobby. If the game has been started successfully, a message 
    with NotificationType `GAME_STARTED` will be sent to `/topic/game/{gameId}`. The user
    should have connected to this destination after joining the lobby.
    - If there are any problems starting the game, the user will receive a message
    on `/user/queue/lobby` of type `ERROR`
    - NOTE: only the lobby host is allowed to start the game

- /game/action
    - Performs an action during a game. If any error occurs during an action, a
    GameMessageDto with type `ERROR` will be sent to `user/queue/game/errors`.
    - See the Actions section below for more details of the actions available to 
    perform
    - See the Responses section below for more details on the responses you can get 
    when sending an action.

### Actions:

- "NO_THANKS": Put one of your chips on the card and start the next player's turn. 
If you have no chips remaining, you will get a response back with the notification type of 'ERROR'.
- "TAKE_CARD": Take the current card and all of the chips that are on it.

#### Responses
Lobby messages:
- NotificationType: `"LOBBY_CREATED"` Payload: The join code for the lobby
- NotificationType: `"JOINED_LOBBY"` Payload: 
- NotificationType: `"PLAYER_JOINED"` Payload: The name of the player who joined 
- NotificationType: `"PLAYER_LEFT"` Payload: The name of the player who left
- NotificationType: `"GAME_STARTED"` Payload: null
- NotificationType: `"ERROR"` Payload: The validation/error message

Action responses:
- NotificationType: `"DRAW_CARD"` Payload: The number of the card that was drawn
- NotificationType: `"NO_THANKS"` Payload: The gameId/join code
- NotificationType: `"TAKE_CARD"` Payload: The player name who took the card
- NotificationType: `"GAME_OVER"` Payload: Name of the player with the most points
- NotificationType: `"PLAYER_TURN"` Payload: Name of the player whose turn it is
- NotificationType: `"ERROR"` Payload: The validation/error message

#### Resources
[Official spring basic tutorial](https://spring.io/guides/gs/messaging-stomp-websocket/)

[Baeldung basic tutorial](https://www.baeldung.com/websockets-spring)

[Helpful 4-part video tutorial](https://www.youtube.com/watch?v=XY5CUuE6VOk)
