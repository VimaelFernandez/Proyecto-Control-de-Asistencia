#ifndef FUNCTIONS_H
#define FUNCTIONS_H

#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESPAsyncWebServer.h>
#include "globalVariables.h"

void showValuesFromEEPROM();

void showConfiguration(String mDnsStr, String &ssid, String &password, int &onTimeConfigured, int &offTimeConfigured);

void showModeSchedule(String apiTime);

void ShowModeManual(bool stateLamp);

void ShowModeTelematic(String body);

void showModeSensorMove(bool &loadState);

String readStringFromEEPROM(int addr);

int readIntFromEEPROM(int address);

void readDataEEPROM(String &mDnsStr, String &ssid, String &password,
                    int &onTimeConfigured, int &offTimeConfigured);

void writeStringToEEPROM(String str);

void saveMinutes(int hour, int minutes);

void serverAppConfig();

void serverStaConfig();

void evalueLoad(int &startMinutes, int &finishMinutes, const uint8_t &outPin, const uint8_t &wifiLed);

String doQuery(String url);

int convertHoursToMinutes(String &stringHours);

unsigned readStateButton(unsigned lastTime, AsyncWebSocket &ws, const uint8_t &button, const uint8_t &outPin, bool &manualMode);

bool controlFromApp(bool &flag, bool &status, const uint8_t &outPin, const uint8_t &wifiLed);

#endif