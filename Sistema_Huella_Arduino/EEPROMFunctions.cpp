#include <Arduino.h>
#include <EEPROM.h>
#include "Functions.h"
#include "globalVariables.h"

/*Variable que almacena las direcciones de los datos
almacenados en la memoria EEPROM
*/
uint8_t currentAddress = 0;

/*Variable que almacena las direcciones de los datos
almacenadas en la memoria EEPROM
*/
uint8_t addressData = 127;

/*Función para guardar el tiempo en variables del tipo int
en la memoria EEPROM
La función recibe el tiempo exacto (horas y minutos) para
realizar una conversión a minutos y guardar los datos en la
memoria EEPROM*/
void saveMinutes(int hour, int minutes) {
  int minutesToSave = (hour * 60) + minutes;
  Serial.println("Hora recibida: " + String(hour) + "; Minutos recibidos: " + String(minutes));
  Serial.println("Dirección: " + String(currentAddress) + "; Minutos totales: " + String(minutesToSave));
  Serial.println("Guardar dirección en la dirección: " + String(addressData));
  EEPROM.write(addressData, currentAddress);
  addressData--;
  EEPROM.put(currentAddress, minutesToSave);
  EEPROM.commit();
  currentAddress += 3;
}

/*Función para escribir en la EEPROM cadenas de texto
  y guardar las direcciones de las mismas 
*/
void writeStringToEEPROM(String str) {
  Serial.println("Dirección: " + String(currentAddress) + "; Dato (String): " + str + "; Tamaño: " + str.length());
  Serial.println("Guardar dirección en la dirección: " + String(addressData));
  EEPROM.write(addressData, (currentAddress + 1));
  addressData--;
  EEPROM.write(currentAddress, str.length());
  Serial.print("Escribiendo en EEPROM: ");
  currentAddress++;
  for (int i = 0; i < str.length(); i++) {
    EEPROM.write(currentAddress + i, str[i]);
    Serial.print(str[i]);
  }
  Serial.println("");
  EEPROM.write(currentAddress + str.length(), '\0');
  EEPROM.commit();
  currentAddress += str.length() + 2;
}

//Lee los datos almacenados en la EEPROM
void readDataEEPROM(String &mDnsStr, String &ssid, String &password) {
  mDnsStr = readStringFromEEPROM(EEPROM.read(addressData));
  addressData--;
  ssid = readStringFromEEPROM(EEPROM.read(addressData));
  addressData--;
  password = readStringFromEEPROM(EEPROM.read(addressData));
}

//Función para leer cadenas de texto de la EEPROM
String readStringFromEEPROM(int addr) {
  char data[20];  //Maximo tamaño del dato almacenado
  int len = 0;
  unsigned char k;
  k = EEPROM.read(addr);
  if (k == 255) {
    return "";
  }
  while (k != '\0' && len < 99) {  //Leer hasta el terminador del string
    data[len] = k;
    len++;
    k = EEPROM.read(addr + len);
  }
  data[len] = '\0';  //Terminar la cadena con nulo
  return String(data);
}
