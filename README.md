<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->


<h3 align="center">Chest_Game</h3>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#event-chain">Event Chain</a></li>
      </ul>
    </li>
    <li>
      <a href="#implementation">Implementation</a></li>
       <ul>
        <li><a href="#used-libraries-and-technologies">Used Libraries And Technologies</a></li>
      </ul>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

  This project is a backend part for a 'chest' game. Chest game is a game for 2-4 people. At the start of the game everyone gets 4 cards from the deck. Then first player asks anyone he wants for a card according to these rules : 
first, he asks the value of the card(king, ace, 8 etc.), then he asks the amount of cards(1,2,3) and finally the suits of these cards. If he got it right, the person asked gives him these cards and the player can ask anyone else again, if not, he picks the card from the deck and the turn goes to the next player. The goal is to make as many chests as possible, the chest is 4 cards with equal card value, the game ends, when all the deck is empty. 

### Event chain
The user signs in via http request, so he can retrieve information about his previous games, like number of games won, or history of previous games (TODO). For the http handshake user has to add user_name and password headers, so the system can authenticate him and bound userPrincipal to simpSessionId. Then user can create a room, or join the existing room. When room is full( 2-4 people, depends on number, that owner set when created a room) the game starts and the system sends event to every user in the room. Then the game goes as its written in 'About the project' paragraph.     

## Implementation
The project was built on java, using spring framework. The idea was to make a browser game, so i used websockets for that. The app uses MySql to store User objects and Redis to store Chat objects with Players in it. 
I wanted to make a scalable app, so i made Abstract classes like AbstractUser, AbstractChat, ChatEvent. These classes use generics to specify class fields and methods. There are several abstract repositories and Services. So, you can reduce the amount of boilerplate code if you want, for example, add a new chat to write text messages, not only GameChat. Also i shortened the code by adding ChatEvents. The idea is to make some a few subscription destinations and different events, that extends from ChatEvent, so you will always get a meaningful message of happened events in one topic. On the first steps, project contained Spring Security to associate users with sessions. But there was several problems with SockJs and i decided to replace it with small custom security implementation, because i didnt need an advanced protection. Also, app uses DTO pattern, so i included map-struct to my project to transform DAO to DTO objects

### Used Libraries And Technologies

Spring boot
Redis, MySql
RabbitMq
Hibernate 
Spring Data
JPA
Spring-messaging, Stomp
Spring-testing, JUnit5
MapStruct
Lombok
Spring MVC
