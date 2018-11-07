const int btnRight = 2;
const int sensor = 3;
const int btnLeft = 4;
const int ledGreen = 5;
const int ledYellow = 6;
const int ledRed = 7;
const long TIME_INTERVAL = 5000; //Timer before locking

int val = 0;				//Order validator counter
int state = 0;				//State for choosing switch case
int btnLeftState = 0;		//Current state of left button
int btnLeftLastState = 0;	//Last state of left button
int btnRightState = 0;		//Current state of right button
int btnRightLastState = 0;	//Last state of right button
int btnPushCounter = 0; 	//Count how many times buttons have been pressed

long timeOfMotion = 0;		//Time of registeres motion in milliseconds
long timeOfUnlock = 0;		//Time of unlocking in milliseconds
long currentTime = 0;		//Current time on milliseconds

void setup() {
  pinMode(btnRight, INPUT);
  pinMode(sensor, INPUT);
  pinMode(btnLeft, INPUT);
  pinMode(ledGreen, OUTPUT);
  pinMode(ledYellow, OUTPUT);
  pinMode(ledRed, OUTPUT);
  Serial.begin(9600);
} 

void loop() {
	static int btnPushCounter = 0;
	bool stateSensor = digitalRead(sensor)==HIGH;
	btnLeftState = digitalRead(btnLeft);
	btnRightState = digitalRead (btnRight);

	switch(state) {
	case 1: 
		detectMotion(stateSensor);
		break;
	case 2: 
		currentTime = millis();
		if (currentTime - timeOfMotion < TIME_INTERVAL) {
			userInput();
		} else {
			locked();
		}
		break;
	case 3:
		currentTime = millis();
		if (currentTime - timeOfUnlock < TIME_INTERVAL) {
			unlocked();
		} else {
			locked();
		}
		break;
	default:
		locked();
		break;    
	}
}//loop
	
//Default
void locked() {
    digitalWrite(ledYellow, LOW); 
    digitalWrite(ledGreen, LOW); 
    digitalWrite(ledRed, HIGH); 
    state = 1;
	val = 0;
	btnPushCounter = 0;
	btnLeftState = 0;
	btnLeftLastState = 0;
	btnRightState = 0;
	btnRightLastState = 0;
	timeOfMotion = 0;
	timeOfUnlock = 0;
	currentTime = 0;
    Serial.println("Now in locked state");
}

//Case 1
void detectMotion(bool stateSensor) {
  if (stateSensor) {
      Serial.println("Motion detected");
      digitalWrite(ledRed, LOW);
	  digitalWrite(ledYellow, HIGH);
      state = 2;
	  timeOfMotion = millis();
  }
}
 
//Case 2
void userInput() {
		//Right button was just pressed or released
	if (btnRightState != btnRightLastState) {
		//Right button was just pressed
		if (btnRightState == HIGH) {
			Serial.println("Right button pushed");
			btnPushCounter++;
			digitalWrite(ledYellow, LOW);
			delay(60);
			digitalWrite(ledYellow, HIGH);
			
			if (val == 0) {
				val = 1; 
			}
		}
		btnRightLastState = btnRightState;
	}
	
	if (btnLeftState != btnLeftLastState) {
		if (btnLeftState == HIGH) {
			Serial.println("Left button pushed");
			btnPushCounter++;
			digitalWrite(ledYellow, LOW);
			delay(60);
			digitalWrite(ledYellow, HIGH);
			
			if (val ==1) {
				val = 2;
			}
		}
		
		btnLeftLastState = btnLeftState;
	}
	
	if (val == 2 && btnPushCounter == 2) {
		state = 3;
		timeOfUnlock = millis();
	} else if (val != 2 && btnPushCounter >= 2) {
		locked();
	}

}

//Case 3
void unlocked() {
    digitalWrite(ledRed, LOW);
    digitalWrite(ledYellow, LOW);
    digitalWrite(ledGreen, HIGH);
	Serial.println("Now in unlocked state");	
	//delay(TIME_INTERVAL); 
	//locked(); //Alternative way to lock after a given time
}
