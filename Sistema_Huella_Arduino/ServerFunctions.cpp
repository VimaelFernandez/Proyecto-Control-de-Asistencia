#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESPAsyncWebServer.h>
#include <EEPROM.h>
#include <ArduinoJson.h>
#include "globalVariables.h"
#include "Functions.h"
#include "WiFiConfig.h"
#include "SensorPage.h"

//Define htmlPage como un dato escrito en la memoria flash
extern char wifiConfig[] PROGMEM;

//Define Sensor como un dato escrito en la memoria flash
extern char sensorPage[] PROGMEM;

//Instancia de servidor web para comunicación HTTP. Puerto 80
AsyncWebServer server(80);

/*Configura el servidor en modo AP para la configuración del dispositivo.
Establece las rutas que se utilizarán en el servidor asíncrono*/
void serverAppConfig() {

  //Ruta para interfaz gráfica (HTML, CSS, JS)
  server.on("/", HTTP_GET, [](AsyncWebServerRequest* request) {
    request->send_P(200, "text/html", wifiConfig);
  });


  /*Ruta para guardar los datos de configuración del dispositivo:
  Nombre (mDns), IP, SSID y Horario de funcionamiento*/
  server.on("/save", HTTP_POST, [](AsyncWebServerRequest* request) {
    String message = "Configuración guardada correctamente!";

    //Guarda la IP, SSID y Password en la EEPROM
    Serial.println("Guardar Ip, SSID y Password");
    String characters[3] = { "devicename", "ssid", "password" };
    for (int i = 0; i < 3; i++) {
      if ((request->hasParam(characters[i], true))) {
        writeStringToEEPROM(request->getParam(characters[i], true)->value());
      } else {
        Serial.println("Hubo un error");
      }
    }

    //Lectura de valores almacenados en la EEPROM
    showValuesFromEEPROM();
    flagEventConfig = true;

    //Enviar la respuesta a consulta.
    request->send(200, "text/plain", message);
  });

  //Levanta el servidor
  server.begin();
}

/*Configura el servidor en modo STA para la configuración del dispositivo.
Establece las rutas que se utilizarán en el servidor asíncrono*/
void serverStaConfig() {

  /*Configura el servidor en modo AP para la configuración del dispositivo.
  Establece las rutas que se utilizarán en el servidor asíncrono*/
  server.on("/", HTTP_GET, [](AsyncWebServerRequest* request) {
    request->send_P(200, "text/html", sensorPage);
  });

  /*Ruta para recibir los datos enviados de la página Web.
  Cambia el estado del dispositivo mediante página web.
  Devuelve una estructura que hace referencia al JSON recibido.*/
  // server.on(
  //   "/api/lamp-telematic", HTTP_POST, [](AsyncWebServerRequest* request) {},
  //   nullptr, [](AsyncWebServerRequest* request, uint8_t* data, size_t len, size_t index, size_t total) {
  //     //Crea el objeto JSON que recibe la información
  //     StaticJsonDocument<256> doc;
  //     //Castea datos recibidos a String. (char* to String)
  //     String body = String((char*)data);

  //     //Deserializa JSON
  //     DeserializationError error = deserializeJson(doc, body);

  //     //Captura un error en la deserialización
  //     if (error) {
  //       request->send(400, "application/json", "{\"error\": \"Error en el formato JSON\"}");
  //       return;
  //     }
  //     //Procede si no hay error en la deserialización
  //     else {
  //       //Obten las propiedades 'status', 'flag' y 'value'
  //       if ((doc.containsKey("status")) && (doc.containsKey("flag"))
  //           && (doc.containsKey("scheduleMode")) && (doc.containsKey("movementMode"))
  //           && (doc.containsKey("manualMode"))) {

  //         //Muestra el modo Interruptor Telemático
  //         // ShowModeTelematic(body);

  //         //Extrae los valores en variables locales
  //         bool stateL = doc["status"];
  //         bool flagL = doc["flag"];
  //         bool scheduleModeL = doc["scheduleMode"];
  //         bool movementModeL = doc["movementMode"];
  //         bool manualL = doc["manualMode"];

  //         //Pasar datos del evento al Loop
  //         flag = flagL;
  //         state = stateL;
  //         scheduleMode = scheduleModeL;
  //         movementMode = movementModeL;
  //         manualMode = manualL;

  //         //Limpia el objeto doc
  //         doc.clear();

  //         //Añade las propiedades 'state'
  //         doc["status"] = stateL;

  //         //Serializa la respuesta al API
  //         String response;
  //         serializeJson(doc, response);
  //         // printMemoryUsage();
  //         //Envía la respuesta
  //         request->send(200, "application/json", response);
  //       } else {
  //         Serial.println("There was a problem with status, flag o checkbox");
  //         request->send(400, "application/json", "{\"error\": \"Status or flag not found\"}");
  //       }
  //     }
  //   });

  // server.on(
  //   "/api/schedule-switch-mov-sensor", HTTP_POST, [](AsyncWebServerRequest* request) {},
  //   nullptr, [](AsyncWebServerRequest* request, uint8_t* data, size_t len, size_t index, size_t total) {
  //     //Crea el objeto JSON que recibe la información
  //     StaticJsonDocument<128> doc;
  //     //Castea datos recibidos a String. (char* to String)
  //     String body = String((char*)data);

  //     //Deserializa JSON
  //     DeserializationError error = deserializeJson(doc, body);

  //     //Captura un error en la deserialización
  //     if (error) {
  //       request->send(400, "application/json", "{\"error\": \"Error en el formato JSON\"}");
  //       return;
  //     } else {
  //       if ((doc.containsKey("scheduleMode")) && (doc.containsKey("movementMode"))
  //           && (doc.containsKey("manualMode"))) {
  //         //Extrae los valores en variables locales
  //         bool scheduleModeL = doc["scheduleMode"];
  //         bool movementModeL = doc["movementMode"];
  //         bool manualL = doc["manualMode"];

  //         scheduleMode = scheduleModeL;
  //         movementMode = movementModeL;
  //         manualMode = manualL;

  //         //Limpia el objeto doc
  //         doc.clear();

  //         //Añade las propiedades 'status'
  //         doc["scheduleMode"] = scheduleMode;
  //         doc["movementModeL"] = movementModeL;
  //         doc["manualMode"] = manualL;

  //         //Serializa la respuesta al API
  //         String response;
  //         serializeJson(doc, response);
  //         // printMemoryUsage();
  //         //Envía la respuesta
  //         request->send(200, "application/json", response);
  //       } else {
  //         Serial.println("There was a problem with status, flag o checkbox");
  //         request->send(400, "application/json", "{\"error\": \"Status or flag not found\"}");
  //       }
  //     }
  //   });

  //Ruta para extraer datos del dispositivo (nombre)
  // server.on("/data", HTTP_GET, [](AsyncWebServerRequest* request) {
  //   //Crea el objeto JSON que recibe la información
  //   String response = "{ \"name\": \"Lampara exterior\", ";
  //   uint8_t statusLoad;
  //   statusLoad = (digitalRead(outPin) == LOW) ? 1 : 0;
  //   response += "\"status\": \"" + String(statusLoad) + "\" }";
  //   request->send(200, "application/json", response);
  // });

  //Levanta el servidor
  server.begin();
}
