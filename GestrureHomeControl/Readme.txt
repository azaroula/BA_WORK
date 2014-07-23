Version 1.0

°REST kommunikation bereitgestelt
°Interaktion mit Kommponenten ralisiert durch Ray Tracing.
°Anheben der Linken Hand in Richtung linke Wand und rechte Hand in Richtung rechte Wand bewirkt die ansteuerrung des
 REST servers, der einen entsprechenden switch Befehl an den Sockets sendet. (Diese Konfiguration ist momentan noch Fest). 

Version 2.0

°User wir jetzt mit hilfe von OpenNIs Fast Tracking automatisch kalibriert und getrackt
°Neue Initialisierungspose: Hand winken wird mit Hilfe der midleware NITE erkannt und als Trigger für die Komponenten
  interaktion genutzt.
°In der Datei config->config.xml können alle Spezifikationen konfiguriert werden(z.b welches Socket zur welcher Wand 
  switchen soll.)
°Multi-user interaction: Es können mehrere benutzer ohne gegenseitige beeinflussung mit den Komponenten im Raum interagieren.
°Datengenerierung erfolgt nur wenn ein User vor der Kinect steht, ansonsten bleibt das Bild stehen und wartet bis ein User erkannt
 wird.

Version 2.1
°Einige bugfixes bezüglich der Multiuser interaktion beseitigt.
°Die Anwendung ist jetzt mit hilfe von Observerpattern modular erweiterbar. Näheres und ausführliche beschreibung zur Nutzung  
 der Observerklassen wird bald im Wiki veröffentlicht. 
°Wandabschnitte können jetzt im configfile definiert werden, so dass auf einer Wand auch mehrere Schnittpunkte angesprochen
 werden können(zb. Obere linke Seite der rechten Wand).    