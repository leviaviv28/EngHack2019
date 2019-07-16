## VapeSafe - EngHack2019 Overall Prize Winner

This project was completed during EngHack2019 at UWaterloo by Levi Aviv, Jenil Vekaria, and Jarrod Servilla 
from June 21st 8:00 PM to June 23rd 8:00 AM. 

## The idea

VapeSafe is an Arduino and Android project that keeps track of daily usage and limits the user once the daily vape usage 
limit has been reached. The recommended daily limit is calculated using information regarding the user's height, weight, 
and daily smoking habits. Usage statistics are also available to be viewed in the app to keep track of progress. 
Data is communicated between the two components with Bluetooth.

## How we built it

The project has two parts:

1. Arduino component 

We worked through various stages to implement our Bluetooth connection model: communication between components, sending and receiving data functionality, storing data on the Arduino chip, and on-launch syncing between components.

2. VapeSafe Android app

We designed a mockup of how the activities would look, the flow of the app, and how data would be sent/received/stored. Then, we worked on syncing it to the Arduino, calculating the recommended daily limit, and communicating between the two. 

## Challenges we ran into

The main issues we had during the project were establishing a connection between the two components and the transferring of data. At first, the connection was being refused. Sending and receiving data on the Arduino was the most difficult stage since we would often either receive incorrect garbage data or fail to establish a connection at all. 

## Accomplishments that we're proud of

We are proud to have fully implemented our project idea as we envisioned it within the timeframe of the hackathon and despite the challenges that we have faced.

In addition, we surprisingly were able to finish the day before despite the fact that we were very close to quitting just that afternoon.

## What we learned

We learnt how to design and implement efficient data transfer models over Bluetooth and building an app from the ground up.

## What's next

VapeSafe has a lot of potential to be expanded upon, such as improving upon the recommended limit by using usage statistics from users to maximize their chances to quit nicotine. In addition, we could implement an occasional "phantom puff" where we have some hits contain little to no nicotine to wean users off nicotine.

Team Members: Levi Aviv, Jenil Vekaria, Jarrod Servilla

## Devpost
https://devpost.com/software/vapesafe/
