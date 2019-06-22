#include "SwitchPack.h"
#include <EEPROM.h>

#define hit_lim_addr 0
#define day_offset 1
#define sync_year_addr 2
#define sync_month_addr 3
#define sync_day_addr 4
#define hit_start_addr 5
#define btnPin 13
#define ledPin 12
#define btStatePin 11
#define REACTION_TIME 500

DoubleClick key(btnPin, PULLUP, REACTION_TIME);
byte btnPresses = 0;

void blinkLed(){
    digitalWrite(ledPin, HIGH);
    delay(50);
    digitalWrite(ledPin, LOW);
}

void syncData(){
    char x;
    Serial.print(EEPROM.read(sync_year_addr));
    Serial1.print(EEPROM.read(sync_year_addr));
    Serial1.print(",");
    Serial.print(EEPROM.read(sync_month_addr));
    Serial1.print(EEPROM.read(sync_month_addr));
    Serial1.print(",");
    Serial.println(EEPROM.read(sync_day_addr));
    Serial1.print(EEPROM.read(sync_day_addr));
    Serial1.print("/");
    for(int i = EEPROM.read(hit_start_addr); i < EEPROM.read(hit_start_addr) + EEPROM.read(day_offset); i++){
        Serial1.print(EEPROM.read(hit_start_addr + i));
        Serial1.print(",");
    }
    x = (char)Serial1.read();
    Serial.println(x);
    EEPROM.write(hit_lim_addr, x);
    x = Serial1.read();
    Serial.println(x);
    EEPROM.write(sync_year_addr, x);
    x = Serial1.read();
    Serial.println(x);
    EEPROM.write(sync_month_addr, x);
    x = Serial1.read();
    Serial.println(x);
    EEPROM.write(sync_day_addr, x);
    Serial.println(EEPROM.read(hit_lim_addr));
    Serial.println(EEPROM.read(sync_year_addr));
    Serial.println(EEPROM.read(sync_month_addr));
    Serial.println(EEPROM.read(sync_day_addr));
}

void setup(){
    Serial.begin(9600);
    Serial1.begin(9600);
    key.begin();
    key.setMaxClicks(3);
    pinMode(ledPin, OUTPUT);
    EEPROM.write(hit_lim_addr, 100);
    EEPROM.write(sync_year_addr, 19);
    EEPROM.write(sync_month_addr, 6);
    EEPROM.write(sync_day_addr, 22);
    EEPROM.write(day_offset, 5);
    for(int i = 0; i < 5; i++){
        EEPROM.write(hit_start_addr + i, i * 10);
    }
}

void loop(){
    //One Day has passed, increment day counter
    if((millis() / 100) % 86400 == 0){
        EEPROM.write(day_offset, EEPROM.read(day_offset) + 1);
    }
    btnPresses = key.clickCount();
    
    if(btnPresses == 1){
        Serial.println(btnPresses);
        if(1000 < EEPROM.read(hit_lim_addr)){
            blinkLed();
            //EEPROM.write(cur_hit_addr, EEPROM.read(cur_hit_addr) + 1);
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
            Serial.println("Waiting to connect");
        }
        char x = Serial1.read();
        Serial.print("Got: ");
        Serial.println(x);
        if(x == 'S'){
            Serial.println("Sending");
            syncData();
        }
        Serial.println("Paired");
    }
}