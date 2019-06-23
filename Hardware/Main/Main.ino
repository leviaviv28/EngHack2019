#include "SwitchPack.h"
#include <EEPROM.h>

#define btSerial Serial1
#define hit_lim_addr 0
#define day_offset 1
#define sync_year_addr 2
#define sync_month_addr 3
#define sync_day_addr 4
#define hit_start_addr 5
#define btnPin 13
#define ledPin 12
#define ledPin2 10
#define btStatePin 11
#define REACTION_TIME 500

DoubleClick key(btnPin, PULLUP, REACTION_TIME);
byte btnPresses = 0;
String btData = "";

void blinkLed(byte pin, int duration, int n){
    for(int i = 0; i < n; i++){
        digitalWrite(pin, HIGH);
        long start = millis();
        while(millis() - start < duration){}
        digitalWrite(pin, LOW);
    }
}

void syncData(){
    btData = "";
    while(btSerial.available() > 0){
        btData += btSerial.read();
    }
    EEPROM.write(hit_lim_addr, btData.toInt());
    Serial.print("New limit set to: ");
    Serial.println(btData);
    btData = "";
    btData += EEPROM.read(sync_year_addr);
    btData += ",";
    btData += EEPROM.read(sync_month_addr);
    btData += ",";
    btData += EEPROM.read(sync_day_addr);
    btData += "/";
    for(int i = 0; i < EEPROM.read(day_offset); i++){
        btData += EEPROM.read(hit_start_addr + i);
        btData += ",";
    }
    btData.trim();
    Serial.println();
    Serial.println(btData);
    btSerial.print(btData);
    btSerial.end();
}

void setup(){
    Serial.begin(9600);
    key.begin();
    key.setMaxClicks(3);
    pinMode(ledPin, OUTPUT);
    pinMode(ledPin2, OUTPUT);
    EEPROM.write(hit_lim_addr, 25);
    EEPROM.write(sync_year_addr, 19);
    EEPROM.write(sync_month_addr, 6);
    EEPROM.write(sync_day_addr, 22);
    EEPROM.write(day_offset, 8);

    //setting values for demo
    EEPROM.write(EEPROM.read(hit_start_addr + EEPROM.read(day_offset)), 24);
}

void loop(){
    //One Day has passed, increment day counter
    if(millis() % 8640000 == 0){
        //EEPROM.write(day_offset, EEPROM.read(day_offset) + 1);
        Serial.println("Day has passed");
    }

    btnPresses = key.clickCount();
    if(btnPresses == 1) {
        if(EEPROM.read(EEPROM.read(day_offset)) <= EEPROM.read(hit_lim_addr)){
            blinkLed(ledPin, 1000, 1);
            Serial.print("Vaporizer hits left: ");
            Serial.println(EEPROM.read(hit_lim_addr) - EEPROM.read(EEPROM.read(day_offset)));
            EEPROM.write(EEPROM.read(day_offset), EEPROM.read(EEPROM.read(day_offset)) + 1);
        } else {
            blinkLed(ledPin2, 1000, 1);
            Serial.println("Vaporizer hit not allowed");
        }
    } else if(btnPresses == 3) {
        Serial.println("Pairing Mode");
        blinkLed(ledPin, 3, 500);
        btSerial.begin(9600);
        while(!digitalRead(btStatePin)){
            blinkLed(ledPin, 100, 1);
        }
        Serial.println("Paired");
        Serial.println("Sending");
        delay(1000);
        syncData();
    }
}
