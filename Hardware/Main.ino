#include "SwitchPack.h"
#include <EEPROM.h>
#include <SoftwareSerial.h>

#define hit_lim_addr 0
#define cur_hit_addr 1
#define btnPin 13
#define ledPin 12
#define btStatePin 9
#define REACTION_TIME 500

DoubleClick key(btnPin, PULLUP, REACTION_TIME);
SoftwareSerial EEBlue(10, 11); // RX | TX
byte btnPresses = 0;

void blinkLed(){
    digitalWrite(ledPin, HIGH);
    delay(50);
    digitalWrite(ledPin, LOW);
}


void setup(){
    Serial.begin(9600);
    EEBlue.begin(9600);
    key.begin();
    key.setMaxClicks(3);
    pinMode(ledPin, OUTPUT);
    EEPROM.write(hit_lim_addr, 10);
    EEPROM.write(cur_hit_addr, 9);
}

void loop(){
    btnPresses = key.clickCount();
    if(btnPresses == 1){
        if(EEPROM.read(cur_hit_addr) < EEPROM.read(hit_lim_addr)){
            blinkLed();
            EEPROM.write(cur_hit_addr, EEPROM.read(cur_hit_addr) + 1);
            Serial.println("Hit allowed");
        } else {
            Serial.println("Hit not allowed");
        }
    } else if(btnPresses == 3){
        Serial.println("Pairing Mode");
        blinkLed();
        delay(100);
        blinkLed();
        delay(100);
        blinkLed();
        while(!digitalRead(btStatePin)){
            delay(50);
        }
        Serial.println("Paired");
    }
}