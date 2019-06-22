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

byte btnPresses = 0;
bool requestingData = false;
void setup(){
    Serial.begin(9600);
    Serial1.begin(9600);
}

void loop(){
    String data = "";
    while(Serial1.available() > 0){
       data += Serial1.read();
    }


    //REQUEST FOR SYNC OPERATION
    if(data.equals("83")){
      ////DEBUGGING///
      Serial.print("Sending Data: ");
      Serial.println("19,6,10/5,10,20,30,40");
      ////DEBUGGING///

      //SENDING THE DATA TO THE PHONE
      Serial1.print("19,6,10/5,10,20,30,40,50,60");

      requestingData = true;
    }else if(data.length() > 0){
      //DISPLAY THE DATA
      Serial.print("Receiving Data: ");
      data.replace("44",",");
      Serial.println(data);
      requestingData = false;
    }

    delay(1000);
}
