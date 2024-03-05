#include <FirebaseESP8266.h>
#include <MAX30100_PulseOximeter.h>
#include <ESP8266WiFi.h>

#define WIFI_SSID "UiTiOt-E3.1"
#define WIFI_PASSWORD "UiTiOtAP"

#define FIREBASE_HOST "pulse-measurement-402ec-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "b38a6302138951aa04f026be720336c0eb273982"

#define REPORTING_PERIOD_MS 1000

uint32_t tsLastReport = 0;
int heartRate = 0.0;
int spO2 = 0;

FirebaseData firebaseData;
PulseOximeter pox;

void onBeatDetected()
{
  Serial.println("Beat Detected...");

  if (millis() - tsLastReport > REPORTING_PERIOD_MS)
  {
    Serial.print("Nhịp tim: ");
    Serial.println(round(pox.getHeartRate()));
    Serial.print("SpO2: ");
    Serial.print(pox.getSpO2());
    Serial.println("%");
    tsLastReport = millis();
  }

  heartRate = round(pox.getHeartRate());
  spO2 = pox.getSpO2();

  if (heartRate > 40){
      if (spO2 >= 20 && spO2 <= 100){
        pox.shutdown();
        sendDataToFirebase();
        pox.resume();
      }
  }
}

void connectWifi()
{
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("Kết nối...");
  Serial.print("Kết nối thành công. IP Address:");
  Serial.println(WiFi.localIP());
}

void connectFirebase()
{
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void setup()
{
  Serial.begin(9600);

  connectWifi();
  connectFirebase();

  Serial.print("Initializing pulse oximeter..");

    // Initialize sensor
    if (!pox.begin()) {
        Serial.println("FAILED");
        for(;;);
    } else {
        Serial.println("SUCCESS");
    }

	// Configure sensor to use 7.6mA for LED drive
	pox.setIRLedCurrent(MAX30100_LED_CURR_7_6MA);

  // Register a callback routine
  Serial.println("it run here");
}

void sendDataToFirebase()
{
  if (Firebase.setInt(firebaseData, "Heart", heartRate))
  {
    Serial.println("Gửi dữ liệu Heart thành công");
  }
  else
  {
    Serial.println("Lỗi khi gửi dữ liệu Heart");
  }

  if (Firebase.setInt(firebaseData, "SpO2", spO2))
  {
    Serial.println("Gửi dữ liệu SpO2 thành công");
  }
  else
  {
    Serial.println("Lỗi khi gửi dữ liệu SpO2");
  }
}

void loop()
{
  pox.setOnBeatDetectedCallback(onBeatDetected);
  pox.update();
}