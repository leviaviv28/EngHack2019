#include <EEPROM.h>

#define btnPin 13
#define ledPin 12

int firstPress = 999;
int secondPress = 999;
int thirdPress = 999;

byte btnState = LOW;

void setup(){
    Serial.begin(9600);
    pinMode(btnPin, INPUT);
    pinMode(ledPin, OUTPUT);
}

void loop(){
    btnState = (digitalRead(btnPin) == 0)? HIGH : LOW;
    digitalWrite(ledPin, btnState);
    int timeout = 0;
    while(timeout > 500){
        
    }
    firstPress = 999;
    secondPress = 999;
    thirdPress = 999;
}