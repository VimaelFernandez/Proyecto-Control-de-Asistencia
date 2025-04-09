#include <Arduino.h>
#include <EEPROM.h>
#include "Functions.h"

//Muestra de valores almacenados en la EEPROM
void showValuesFromEEPROM() {
  Serial.println("");
  Serial.println("Lectura de valores almacenados en la EEPROM:");
  Serial.println("mDNS almacenado: " + readStringFromEEPROM(EEPROM.read(127)));
  Serial.println("SSID almacenado: " + readStringFromEEPROM(EEPROM.read(126)));
  Serial.println("Password almacenado: " + readStringFromEEPROM(EEPROM.read(125)));
}

// Muestra la configuración del microcontrolador
void showConfiguration(String mDnsStr, String &ssid, String &password, int &onTimeConfigured, int &offTimeConfigured) {
  Serial.println("");
  Serial.println("mDns almacenado: " + mDnsStr);
  Serial.println("SSID almacenado: " + ssid);
  Serial.println("Password almacenado: " + password);
}

// //Muestra el modo Interruptor Manual
// void ShowModeManual(bool stateLamp) {
//   Serial.println();
//   Serial.println("----------------------------------------------");
//   String state;
//   state = stateLamp == HIGH ? "Encendido" : "Apagado";
//   Serial.println("Modo Manual Activo : Bombillo " + state);
// }

//Muestra el modo Interruptor Telemático
// void ShowModeTelematic(String body) {
//   Serial.println();
//   Serial.println("----------------------------------------------");
//   Serial.println("Modo Interruptor Telemático Activo");
//   //Muestra el cuerpo del JSON recibido del API 
//   Serial.println("Cuerpo del objeto recibido del API: ");
//   Serial.println(body);
// }

//Muestra el modo Interruptor por sensor de movimiento
// void showModeSensorMove(bool &loadState) {
//   Serial.println();
//   Serial.println("----------------------------------------------");
//   String state;
//   state = loadState == true ? "Encendido" : "Apagado";
//   Serial.println("Modo Interruptor Por Sensor de Movimiento Activo: Bombillo " + state);
// }

