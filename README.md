# Decentralized Chat System

## Description

Decentralzed Chat System is a peer-to-peer communication application developed in Java.

The goal of this project is to create a chat application where users can find each other and communicate over a local network without using a central server.


## Projet is still under developing


## Features

### 1. Peer Discovery

When a user starts the connection:

    * The application sends a UDP broadcast message to announce that the user has joined the network.

    * Other users receive this message and add this new IP in their contact list.

    * Each user sends back a response with their username.

    * The initial user contact list is automatically created and updated.


### 2. Username Management

    * Each user has a username.

    * Users can change their username while the application is running

    * If the new username is not in our contact list, the username is sent to other users and they will update their contact list.


### 3. Connection Management

The application can detect both normal and unexpected disconnections.

#### Normal disconnection: 

    * When a user leaves the application, a UDP message is sent to the other users. When they receive this message their remove the user from their contact list.

#### Unexpected disconnection: 

    * Each user regularly sends a UDP message to show that they are still conected.

    * Each user checks the last time since they receive a message from every contact.

    * If it's been too long since the last update message the contact is considered disconnect and he is deleted from the contact list.


## Terminal Commands 

The application provides a terminal interface to interact with the chat system.

### Available commands

|Command | Description |
|---|---|
|`help`| Displays the list of available commands. |
|`test_connection`| Sends a UDP broadcast to test the connection process. |
|`test_update`| Sends a UDP message to test the update broadcast. |
|`change_username <name> `| Changes the user's username. |
| `list_active`| Displays the list of active users. |
|`exit`| Closes the application. |


## Authors

Developed by : Jacob Ørnegård, Armand Essetcheou, Thomas Schaeffer

