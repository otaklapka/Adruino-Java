#include <Servo.h>

Servo s1; Servo s2; Servo s3; Servo s4;


void setup() {

  pinMode(1,OUTPUT);

  s1.attach(14); //analog pin 0
  s2.attach(15);
  s3.attach(16);
  s4.attach(17);
  
  //servo1.setMaximumPulse(2000);
  //servo1.setMinimumPulse(700);
  
  Serial.begin(9600);
}

void loop() {
  String readString;
  while (Serial.available()) {
    char c = Serial.read();  //gets one byte from serial buffer
    readString += c; //makes the string readString
    delay(2);  //slow looping to allow buffer to fill with next character
  }
  
  if (readString.length() >0) {
    if(readString.indexOf('a') >0) { 
      s1.write(readString.toInt());
    }
    else if(readString.indexOf('b') >0) { 
      s2.write(readString.toInt());
    }
    else if(readString.indexOf('c') >0) { 
      s3.write(readString.toInt());
    }
    else if(readString.indexOf('d') >0) { 
      s4.write(readString.toInt());
    }
 delay(15);
 }
} 
