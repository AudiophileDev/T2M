# Ziele

### 1 Funktionalität  
Mit Hilfe die Software T2M werden die Nachrichtenartikel in mindestens 15 sekunden langen Musikdateien (mp3) interpretiert. Dabei wird die Stimmung des Textes adaptiert und musikalisch wiedergeben. 
Die Systemarchitektur soll nicht nur die Abhängigkeiten der wesentlichen Komponenten abbilden, sondern auch als grafische Darstellung des Zusammenhangs zwischen Textanalyse und Generierung der Musikkomponenten wie Rhythmus, Melodie usw. dienen. Diese erleichtert die spätere Implementierung, da die Programmierer sich nach der abgebildeten und festgelegten Architektur richten können.
### 2 Kompatibilität  
Die “T2M”-Software ist auf unterschiedlichen aktuellen Betriebssystemen verwendbar. Diese Kompatibilität der Software wird durch eine Implementierung in der Programmiersprache Java in der Version 8 entwickelt.
### 3 Wartbarkeit 
Durch die Aufteilung des Programmcodes in verschiedene Klassen, soll zunächst die Implementierung der Software klar auf einzelne Programmierer verteilt werden. Somit entsteht die Möglichkeit der besseren Wartbarkeit und der schnelleren, unabhängigen Implementierung des Tools. Hier ist der Code klar strukturiert und erleichtert deswegen die Modifikation der Funktionalität.
### 4 Performance  
Die Software generiert die Musik in kurzer Zeit, sodass auf einem Computer mit 2,4 GHz dual core CPU und 8GB RAM bei 1000 Textzeichen die Generierung der Musik in weniger als 5s erfolgt.
