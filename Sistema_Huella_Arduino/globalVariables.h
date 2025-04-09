#ifndef GLOBALVARIABLES_H
#define GLOBALVARIABLES_H

#include <ESP8266WiFi.h>
#include <ESPAsyncWebServer.h>

//Declaro las variables definidas en el archivo .ino
//------------------------------------------------------------
//Variables a utilizar en la función readStateButton(...)
// extern AsyncWebSocket ws;
extern bool manualMode;

//Variables a utilizar en la función controlFromApp(...)
extern bool flag;
extern bool status;

//Variables a utilizar en la función showConfiguration(...)
extern String ssid;
extern String password;

/*Constantes para pines de salida y entrada
Led WiFi: wifiLed
Pin de salida: outPin
botón: button*/
extern const uint8_t button;

//Variable a utilizar en la función showModeSensorMove(....)
// extern bool loadState;

//Variables a utilizar en el archivo ServerFunctions.cpp
//Variables que reciben del JSON
// extern bool state;
// extern bool flag;
// extern bool scheduleMode;
// extern bool manualMode;
// extern bool movementMode;
//------------------------------------------------------------

//Variable para activar el reinicio del microcontrolador.
extern bool flagEventConfig;

/*Variable que almacena las direcciones de los datos
  almacenadas en la memoria EEPROM
*/
extern uint8_t addressData;

#endif