const int LED = 14;
void setup() {
   pinMode(LED,OUTPUT);
   Serial.begin(9600);
}

void loop() {
  String readString;
  while (Serial.available()) {
    char c = Serial.read();  // current char from serial
    readString += c; // add to string
    delay(2);  //slow looping to allow buffer to fill with next character
  }

  if (readString.length() >0) {
    if(readString == "turnOn"){
    digitalWrite(LED, 1);
    }
    else if(readString == "turnOff"){
      digitalWrite(LED, 0);
    }
  }
}
