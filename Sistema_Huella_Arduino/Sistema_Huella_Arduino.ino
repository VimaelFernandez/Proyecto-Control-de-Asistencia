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

// Configurar SoftwareSerial para la comunicación con el sensor
SoftwareSerial mySerial(2, 3); // RX, TX
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
  Serial.begin(115200);
  //Configura 'button' como entrada
  pinMode(button, INPUT);
  //Inicializo el serial que conecta con el sensor
  mySerial.begin(57600); // Velocidad recomendada para el sensor
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
  // put your main code here, to run repeatedly:
}
