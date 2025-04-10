#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESPAsyncWebServer.h>
#include <ESP8266mDNS.h>
#include <Adafruit_Fingerprint.h>
#include <SoftwareSerial.h>
#include <EEPROM.h>
#include "globalVariables.h"
#include "Functions.h"

//Botón para escoger el modo de operación
const uint8_t button = 4;

//Led para saber estado WiFi del microcontrolador
const uint8_t wifiLed = 2;

// Configurar SoftwareSerial para la comunicación con el sensor
SoftwareSerial mySerial(3, 1);  // RX, TX
Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);

//Variable para activar el reinicio del microcontrolador.
bool flagEventConfig = false;

//Variable para limitar la impresión en serial si no se ha configurado el micro
bool flagLimSerial = false;

//Variable para imprimir una sola vez el estado de conexión a red WiFi.
bool printOnceTime = true;

/*Variable para escoger modo de configuración
  Funcionamiento en modo AP -> configMode = true;
  Funcionamiento en modo STA -> configMode = false;
*/
bool configMode;

//Variables para credenciales WiFi
String ssid;
String password;

//Variable para guardar el nombre mDns
String mDnsStr;

//Variable para determinar si la EEPROM está vacío
bool eepromVoid;

void setup() {
  //Inicializa el serial
  Serial.begin(57600);
  //Configura 'button' como entrada
  pinMode(button, INPUT);
  //Configura 'wifiLed'como salida
  pinMode(wifiLed, OUTPUT);
  //Inicializo el serial que conecta con el sensor
  // mySerial.begin(57600);  // Velocidad recomendada para el sensor
  //Inicializa la EEPROM para 128 bytes
  EEPROM.begin(128);
  Serial.println("");

  //Escoge el modo de funcionamiento AP o STA
  if (digitalRead(button) == LOW) {
    Serial.println("Modo de funcionamiento: AP");
    // Funcionamiento en modo AP (Servidor)
    configMode = true;
    //Configuración del AP
    const char* apSSID = "ESP8266-AP";
    const char* apPassword = "12345678";
    //Inicializa en modo AP
    WiFi.disconnect();
    WiFi.mode(WIFI_AP);
    WiFi.softAP(apSSID, apPassword);
    Serial.println("");
    Serial.println("AP iniciado");

    // Establece el mDNS para conectarse al servidor
    if (!MDNS.begin("device-iot")) {
      Serial.println("Ocurrió un error con el mDNS");
      delay(3000);
      ESP.restart();
    }
    Serial.println("Iniciado mDNS");

    /*Configura el servidor en modo AP para la configuración del dispositivo.
    Establece las rutas que se utilizarán en el servidor asíncrono*/
    serverAppConfig();
  } else {
    //Funcionamiento en modo STA (conexión a internet)
    configMode = false;

    //Lee la información guardada en la EEPROM (mDns, ssid, password)
    readDataEEPROM(mDnsStr, ssid, password);
    const char* mDns = mDnsStr.c_str();

    if (mDnsStr.isEmpty() || ssid.isEmpty() || password.isEmpty()) {
      eepromVoid = true;
      Serial.println("EEPROM vacía");
    } else {
      eepromVoid = false;
      // Muestra la configuración del microcontrolador
      showConfiguration(mDnsStr, ssid, password);
      //Configura el modo de funcionamiento en estación (STA)
      WiFi.disconnect();
      WiFi.mode(WIFI_STA);
      // Habilitar reconexión automática
      WiFi.setAutoReconnect(true);
      //Inicializa la conexión con las credenciales
      WiFi.begin(ssid, password);
      //Ejecuta bucle durante 60 segundos hasta establecer conexión
      Serial.println("");
      Serial.print("Conectando a red WiFi");
      Serial.println(WL_CONNECTED == WiFi.waitForConnectResult(60000) ? "Dispositivo conectado" : "Error al establecer conexión");
      //Reinicia dispositivo si no se establece conexión
      if (WiFi.status() != WL_CONNECTED) {
        Serial.println("Reiniciando dispositivo...");
        ESP.restart();
      }
      //Imprimir IP
      Serial.print("Ip asignado: ");
      Serial.println(WiFi.localIP());
      //Imprimir MAC
      String macAddress;
      macAddress = WiFi.macAddress();
      Serial.print("MAC: ");
      Serial.println(macAddress);

      //Inicializa mDns
      if (!MDNS.begin(mDns)) {
        Serial.println("Ocurrió un error con el mDNS");
        delay(3000);
        ESP.restart();
      } else {
        Serial.println("mDNS iniciado correctamente");
      }
      //Configura la lógica para el funcionamiento en Sta
      serverStaConfig();
    }
  }
}

void loop() {
  /*Variables y constantes para el control de retardos no bloqueantes*/
  static unsigned long lastEventTimeChangeStatusLed = millis();
  static const unsigned int EVENT_CHANGE_STATUS_LED = 4000;  //4 segundos

  static unsigned long lastEventTimeNoConfig = millis();
  static const unsigned int EVENT_CHANGE_TIME_NO_CONFIG = 2000;  //4 segundos

  static unsigned long lastEventTimeChangeStatusLedFirstConfig = millis();
  static const unsigned int EVENT_CHANGE_STATUS_LED_FIRST_CONFIG = 1000;  //1 Segundo


  if ((configMode == false) && (eepromVoid == false)) {
    //Bloque si la conexión a WiFi no es exitosa.
    if (WiFi.status() != WL_CONNECTED) {
      if (printOnceTime == true) {
        Serial.println("Desconectado de red WiFi. Intentando reconectar...");
        printOnceTime = false;
      }

      /*Evalúa la condición del LED cada 2 segundos. Cambia el estado
          en el tiempo establecido.Indica que no tiene conexión a internet*/
      if (millis() - lastEventTimeChangeStatusLed > EVENT_CHANGE_STATUS_LED) {
        //Cambia al estado contrario del leído
        digitalRead(wifiLed) ? digitalWrite(wifiLed, LOW) : digitalWrite(wifiLed, HIGH);
        //Activa el retraso de 1 segundo
        lastEventTimeChangeStatusLed = millis();
      }
    }
  }
  //Bloque si no se ha configurado ningún dato en el microcontrolador
  else {

    //Ejecuta esta acción con retardos de 2 segundos (delay)
    if (millis() - lastEventTimeNoConfig > EVENT_CHANGE_TIME_NO_CONFIG) {
      //Actualiza el servicio MDNS
      MDNS.update();
      //Condicional para imprimir una sola vez en el monitor serial
      if (flagLimSerial == false) {
        Serial.println("Falta configurar SSID, PASSWORD, IP del dispositivo");
        flagLimSerial = true;
      }
      //Condicional para reinicio automático al guardar configuracion
      if (flagEventConfig == true) {
        Serial.println("Reiniciando microcontrolador");
        digitalWrite(wifiLed, LOW);
        delay(10000);
        ESP.restart();
      }
      //Activa el retraso  de 2 segudos
      lastEventTimeNoConfig = millis();
    }

    //Retardo de 1 segundo para encender y apagar el LED. Indica que falta configuración WiFi
    if (millis() - lastEventTimeChangeStatusLedFirstConfig > EVENT_CHANGE_STATUS_LED_FIRST_CONFIG) {
      //Cambia al estado contrario del leído
      digitalRead(wifiLed) ? digitalWrite(wifiLed, LOW) : digitalWrite(wifiLed, HIGH);
      //Activa el retraso de 1 segundo
      lastEventTimeChangeStatusLedFirstConfig = millis();
    }
  }
}
