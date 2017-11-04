# ![image alt text](image_0.png)

Pflichtenheft

Text2Music

12.09.2017

<table>
  <tr>
    <td>Projektleiter</td>
    <td>Leon Erath</td>
  </tr>
  <tr>
    <td>letzte Änderung</td>
    <td>12.09.2017</td>
  </tr>
  <tr>
    <td>E-Mail</td>
    <td>text2music@googlegroups.com</td>
  </tr>
</table>


Inhaltsverzeichnis

[[TOC]]

# 1 Einleitung

Das klassische Printmedium als Nachrichtenquelle wird im Zeitalter der Digitalisierung immer weniger genutzt. Daher greifen Nachrichtendienstleister vermehrt auf Webseiten zurück um Nachrichten schnell und übersichtlich darzustellen. Ein neues, innovatives Feature für eine solche Nachrichtenwebseite  wäre eine kurze Preview des Artikels in Form von Musik. Dabei wird nicht irgendeine zufällige, bereits existente Musik gespielt, sondern mit Hilfe einer Software anhand des Textes und dessen Inhalt ein individueller Musikausschnitt generiert und ausgegeben.

<table>
  <tr>
    <td>Projekttitel</td>
    <td>Text2Music</td>
  </tr>
  <tr>
    <td>Kunde des Projekts</td>
    <td>SPIEGEL ONLINE</td>
  </tr>
  <tr>
    <td>Preis</td>
    <td>34.800,00 €</td>
  </tr>
  <tr>
    <td>Lieferumfang</td>
    <td>Software "T2M",  Dokumentation der Software</td>
  </tr>
  <tr>
    <td>Lieferdatum</td>
    <td>09.11.2017</td>
  </tr>
  <tr>
    <td>Mitglieder</td>
    <td>Leon Erath
Daniel Scholz
Simon Niedermayr
Tobi Wymer
Marcello Eiermann
Ivan Marchuk
(Carolin Lindow)
</td>
  </tr>
</table>


# 2 Ziele 

## 2.1 Musskriterien

Wir wollen die Software T2M entwickeln, die Nachrichtenartikel in mindestens 15 sekunden langen Musikdateien (mp3) interpretiert. Dabei wird die Stimmung der Musik den Inhalt des Textes wiedergeben. Die Musik wird individuell zum Text generiert und ist rein instrumental. Die Software wird als einfach einzubindes Kommandozeilenwerkzeug ausgeliefert. Zu Testzwecken wird eine GUI implementiert. 

## 2.2 Wunschkriterien

Es ist wünschenswert, dass die Lautstärke variiert werden kann. Desweiteren soll die Verwendung von mehrer Instrumenten möglich sein. 

## 2.3 Abgrenzungskriterien

* Es wird kein Gesang implementiert. 

* Es werden nur deutsche Texte zur Musikgenerierung benutzt.

# 3 Aufwandsschätzung

Im Folgenden wird die Aufwandsschätzung in Form einer tabellarischen Übersicht dargestellt.

<table>
  <tr>
    <td></td>
    <td>Stunden</td>
    <td>Kosten</td>
  </tr>
  <tr>
    <td>Systementwurf</td>
    <td>50</td>
    <td>6.000,00 €</td>
  </tr>
  <tr>
    <td>Entwicklung + Dokumentation der Software</td>
    <td>265</td>
    <td>31.800,00 €</td>
  </tr>
  <tr>
    <td>Testen</td>
    <td>25</td>
    <td>3.000,00 €</td>
  </tr>
  <tr>
    <td>Insgesamt</td>
    <td>340</td>
    <td>40.800,00 €</td>
  </tr>
  <tr>
    <td></td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>Stundenlohn pro Mitarbeiter</td>
    <td></td>
    <td>120,00€</td>
  </tr>
  <tr>
    <td>Mitarbeiter</td>
    <td></td>
    <td>6</td>
  </tr>
</table>


# 4 Einsatzbereich

## 4.1 Anwendungsbereiche

<table>
  <tr>
    <td>Name</td>
    <td>Musik steuern</td>
  </tr>
  <tr>
    <td>Kennung</td>
    <td>U-1</td>
  </tr>
  <tr>
    <td>Priorität</td>
    <td>Hoch</td>
  </tr>
  <tr>
    <td>Beschreibung</td>
    <td>Der Anwendungsbereich der Software begrenzt sich auf Webseiten mit Nachrichtenartikeln. Hierbei kann es sich um Nachrichtenseiten, Blogs, Kurzgeschichten etc. handeln. </td>
  </tr>
  <tr>
    <td>Typischer Ablauf</td>
    <td>Der User geht auf die Webseite des Kunden. Wenn dem User ein Artikel interessiert, spielt er die Musik ab, die den Artikel interpretiert. Falls er möchte kann er die Musik pausieren oder stoppen. 
Der User des Testwerkzeuges kann darüber hinaus Instrumente einstellen. Er kann selber Musik generieren lassen, indem er Text in einem Textfeld eingibt.</td>
  </tr>
  <tr>
    <td>Vorbedingungen</td>
    <td>T2M ist eingebunden.</td>
  </tr>
  <tr>
    <td>Autor</td>
    <td>Leon Erath</td>
  </tr>
  <tr>
    <td>Version</td>
    <td>1.4</td>
  </tr>
  <tr>
    <td>Datum</td>
    <td>14.09.2017</td>
  </tr>
</table>


#### ![image alt text](image_1.png)

## 4.2 Zielgruppen

Die T2M Software soll Lesern der Textartikel die Möglichkeit bieten sich einen Eindruck des Texts über Musik zu verschaffen. Somit ist die Zielgruppe die Leserschaft der jeweiligen Website.    

# 5 Umgebung 

## 5.1 Hardware

* Internetfähiges Gerät auf Seite des Users

* Server zum Generieren der Musik

## 5.2 Software

* Java JRE 8 auf dem Betriebssystem installiert

* Browser (Chrome, Firefox, Safari) zum ausführen der Web-Demo

# 6 Funktionen

<table>
  <tr>
    <td>ID</td>
    <td>Beschreibung</td>
    <td>Aufwand in Stunden</td>
  </tr>
  <tr>
    <td>F-X</td>
    <td>Die gelieferte Software liest eine Textdatei (.txt) ein und  gibt es eine mindestens 15 sekündige Musikdatei (.mp3) aus. </td>
    <td>-</td>
  </tr>
  <tr>
    <td>F-X</td>
    <td>Die generierte Musikdatei soll dabei die Stimmung des eingelesenen Textes zusammenfasfsen. Dabei muss der eingelesene Text mindestens 50 Wörter beinhalten.</td>
    <td>-</td>
  </tr>
  <tr>
    <td>F-X</td>
    <td>Damit die Stimmung der Musik der Stimmung des Textes entspricht wird das Tempo, der Rhythmus, die Lautstärke, die Tonart, die Instrumentation und die Harmonien wie nachfolgend erläutert individuell angepasst.</td>
    <td>-</td>
  </tr>
  <tr>
    <td>F-TXT-10</td>
    <td>Bestimmung des Tempos 
Die durchschnittlichen Wortlängen pro Satz eines Textes beeinflussen maßgeblich die Lesegeschwindigkeit des Lesers. Zudem vermittelt sie die Charakteristik des Textes, wobei eine kurze Wortlänge einen eher hitzigen, kurzatmigen Text und eine lange Wortlänge eher einen langsam zu lesenden, trägen Text widerspiegelt. Dies wird in der Musik durch das Tempo ausgedrückt. Eine kürzere  Wortlänge wird durch ein höheres Tempo, eine längere durch eine langsameres Tempo ausgedrückt. Das Tempo wird einmalig berechnet und bleibt während der gesamten Spieldauer gleich.
Priorität: Hoch</td>
    <td>20</td>
  </tr>
  <tr>
    <td>F-RHY-20</td>
    <td>Festlegung des Rhythmus
Der Rhythmus der Musik wird anhand verschiedener Parameter, die aus dem Text ermittelt werden, ermittelt. Die einzelnen Parameter werden in den nachfolgenden Punkten für die Generierung des Rhythmus in Melodie- und Rhythmusgruppe genauer erläutert.  Dadurch wird abwechslungsreiche Musik  zu unterschiedlichen Texten sichergestellt. 
Priorität: Hoch</td>
    <td>-</td>
  </tr>
  <tr>
    <td>F-RHY-20.1</td>
    <td>Rhyhtmusgruppe
Der Grundrhythmus der Musik wird hauptsächlich von den Instrumentengruppen Percussion, Piano und Bass vorgegeben. Für die Rhythmusgruppe gibt es verschiedene, vorgefertigte Bausteine, die mit einem Algorithmus anhand der durchschnittlichen Wortlänge eines Satzes ausgewählt werden.
Priorität: Hoch</td>
    <td>30</td>
  </tr>
  <tr>
    <td>F-RHY-20.2</td>
    <td>Melodiegruppe
Der Rhythmus, den die Melodie spielt, wird durch einen Algorithmus erstellt, der von den einzelnen Buchstaben des Textes ermittelt.  
Priorität: Hoch</td>
    <td>15</td>
  </tr>
  <tr>
    <td>F-MEL-30</td>
    <td>Die Melodie folgt harmonischen Schemata, um ein wohlklingendes, individuelles Musikerlebnis zu gewährleisten. Dafür können wie anschließend erläutert verschiedene Instrumente ausgewählt werden und die Tonhöhe der Melodie wird neben dem Rhythmus individuell errechnet. </td>
    <td></td>
  </tr>
  <tr>
    <td>F-MEL-30.1</td>
    <td>Die Tonhöhe wird durch tonart-eigene (siehe F-TYP-50.1)   Töne durch ein Mapping aus den Buchstaben des Alphabets festgelegt. Um eine schön klingende Melodie zu erzeugen, wird der Tonumfang auf zwei Oktaven begrenzt
Priorität: Hoch</td>
    <td>20</td>
  </tr>
  <tr>
    <td>F-MEL-30.2</td>
    <td>Die Melodie wird von verschiedenen Ensembles gespielt. Dabei kann vom Administrator der Webseite zwischen Piano, Streichensembles, Bläserensembles und Gitarrenklängen ausgewählt werden.
Priorität: Hoch</td>
    <td>10</td>
  </tr>
  <tr>
    <td>F-VOL-40</td>
    <td>Generierung der Lautstärke
Der Charakter eines Textes lässt sich weiterhin durch die relative Häufigkeit der verschiedenen Satzmodi genauer bestimmen.  Dies wird musikalisch durch verschiedene Lautstärke interpretiert. Ein Text der überdurchschnittlich viele Fragesätze enthält wird eher leise und geheimnisvoll bleiben. Viele Ausrufesätze bewirken einen kontinuierlichen Zuwachs der Lautstärke mit einem sehr lauten Abschluss. Enthält der Text keine Besonderheiten hinsichtlich der Satzmodi wird sich die Lautstärke nicht besonders verändern und immer im mittellauten Bereich befinden.
Priorität: Hoch</td>
    <td>10</td>
  </tr>
  <tr>
    <td>F-TYP-50.1</td>
    <td>Generierung der Tonart
Die Stimmung eines Textes wird aber am stärksten durch die verwendeten Adjektive spezifiziert. Um dies in der Musik möglichst genau abzubilden wird der Text mit einer vorgefertigten Datenbank abgeglichen und nach solchen stimmungsbehafteten Adjektiven gesucht. Hier wird nach fröhlichen bzw. traurigen Wörtern unterschieden. Diese liefern dann die Spezifizierung den Modus (Dur, Moll) der Grundtonart.
Priorität: Hoch</td>
    <td>30</td>
  </tr>
  <tr>
    <td>
F-TYP-50.2</td>
    <td>Bei besonders düsteren Texten werden auch verschiedene andere Skalen neben Dur und Moll verwendet. Dadurch kann die Stimmung noch genauer spezifiziert werden.
Priorität: Niedrig</td>
    <td>30</td>
  </tr>
  <tr>
    <td>
F-HAR-60</td>
    <td>Generierung der Harmonien
Eine sehr festliche Stimmung eines Textes wird neben Dur-Akkorden durch Harmonien in der Melodieführung unterstützt. Diese basieren auch auf der Wörter-  und Stimmungsdatenbank, die schon zur Ermittlung des grundsätzlichen Modus genutzt werden.
Priorität: Niedrig</td>
    <td>45</td>
  </tr>
  <tr>
    <td>
F-EFF-70</td>
    <td>Soundeffekte
Texte enthalten oft sehr metaphorische Ausdrücke, die dem Text noch einen sehr speziellen Charakter verleiht. Dies wird in der Musik durch perkussive Effekte und Bläsereinsätze realisiert. 
Priorität: Mittel</td>
    <td>30</td>
  </tr>
  <tr>
    <td>F-UI-80</td>
    <td>User Interface
Die T2M Musikdatein können über einen Media Controller auf einer Webseite abgespielt werden. Diese Media Controller werden auf jedem Nachrichtenartikel einzeln eingebunden. 
Um die Funktionalität unseres Controllers zu testen wird 
eine Testwebseite bereitgestellt.
Priorität: Hoch</td>
    <td>25</td>
  </tr>
  <tr>
    <td></td>
    <td></td>
    <td>265</td>
  </tr>
</table>


# 7 Daten

<table>
  <tr>
    <td>Input</td>
    <td>.txt-Datei, Textfeld</td>
  </tr>
  <tr>
    <td>Output</td>
    <td>.mp3-Musikdatei</td>
  </tr>
</table>


# 8 Leistungsmerkmale

<table>
  <tr>
    <td>ID</td>
    <td>Anforderung</td>
    <td>Beschreibung</td>
  </tr>
  <tr>
    <td>NF-10</td>
    <td>Performance</td>
    <td>Die Generierung der Musik geschieht auf einem Computer mit 2,4 GHz dual core CPU und 8GB RAM bei 1000 Textzeichen in weniger als 5s. </td>
  </tr>
  <tr>
    <td>NF-20</td>
    <td>Einfache Bedienung</td>
    <td>Es ist eine einfache Bedienung durch die windows typische Bedienung durch Tastatur und Maus gewährleistet. Die UI Elemente (Button, Textfelder etc.) entsprechen der auch auf anderen Webseiten bekannten Elemente wie Youtube oder Amazon.</td>
  </tr>
  <tr>
    <td>NF-30</td>
    <td>Einfache Installation</td>
    <td>Durch die Auslieferung Software als .Jar Datei kann die Software durch einen Konsolenbefehl ausgeführt werden. </td>
  </tr>
  <tr>
    <td>NF-40</td>
    <td>Kompatibilität
</td>
    <td>Die Software wird in der Programmiersprache Java programmiert. Sie ist somit kompatibel auf allen Java-Kompatiblen Betriebssystemen.</td>
  </tr>
  <tr>
    <td>NF-50</td>
    <td>Wartbarkeit
</td>
    <td>Durch keine Einbindung von Drittanbieter-Bibliotheken müssen lediglich Kenntnisse über die Programmiersprache Java vorhanden sein, um die Software zu warten. Des Weiteren ist durch die im Quellcode vorhanden Kommentaren und die  mitgelieferte Dokumentation eine einfache Wartbarkeit sichergestellt. </td>
  </tr>
  <tr>
    <td>NF-60</td>
    <td>Dokumentation</td>
    <td>Es wird eine Anwendungsdokumentation angefertigt.</td>
  </tr>
</table>


# 9 Benutzeroberfläche

<table>
  <tr>
    <td>Name</td>
    <td>Testwebseite</td>
  </tr>
  <tr>
    <td>Kennung</td>
    <td>UI-1</td>
  </tr>
  <tr>
    <td>Priorität</td>
    <td>Mittel</td>
  </tr>
  <tr>
    <td>Beschreibung</td>
    <td>Es wird eine Webseite entwickelt um eine Testumgebung für das T2M Programm zu erschaffen.
Die Webseite hat die Form eine Nachrichtenseite. Sie bietet Nutzern die Möglichkeit eine Liste an Nachrichtenartikeln in einer Übersicht einzusehen. Ein Nachrichtenartikel kann angeklickt werden um den Nachrichtenartikel in einer separaten Seite zu eröffnen. Diese Seite bietet die Möglichkeit ein den Artikel in voller Länge zu lesen oder sich das von T2M generierte Musikstück anzuhören.
Die Webseite wird Mobile-Optimiert sein.</td>
  </tr>
  <tr>
    <td>Typischer Ablauf</td>
    <td>Der User besucht die Webseite und wird mit einer Liste von Nachrichtenartikeln begrüßt.

Hier sieht er die jeweiligen überschriften und jeweils die ersten 150 Zeichen des Artikels. 
Der Nutzer kann nun entweder den Titel des Artikels anklicken und ihn separat zu öffnen, oder direkt einen "Play Button" an der rechten Seite des Artikelkopfes klicken um das von T2M generierte Musikstück zu hören.

Sollte der User den Artikel separat geöffnet haben wird ein Player angeboten um die T2M Musik zu hören.

Es wird zusätzlich ein “Playground” geboten wo den Nutzern die Möglichkeit geboten wird einen eigenen Text einzugeben und durch T2M Musik generieren zu lassen.

</td>
  </tr>
  <tr>
    <td>Vorbedingungen</td>
    <td>Das System ist installiert und gestartet.</td>
  </tr>
  <tr>
    <td>Autor</td>
    <td>Tobias Wymer</td>
  </tr>
  <tr>
    <td>Version</td>
    <td>1.0</td>
  </tr>
  <tr>
    <td>Datum</td>
    <td>12.09.2017</td>
  </tr>
</table>


		

# 10 Qualitätsziele

<table>
  <tr>
    <td>ID</td>
    <td>Zusammenfassend</td>
    <td>Beschreibung</td>
  </tr>
  <tr>
    <td>QM-10</td>
    <td>Unterstützte Browser</td>
    <td>Optimal auf Chrome, funktionsfähig auf allen HTML5 Browsern</td>
  </tr>
  <tr>
    <td>QM-20</td>
    <td>technische Qualität</td>
    <td>Es wird später einfach auch weitere Funktionalität auf der Code-Basis aufzubauen. </td>
  </tr>
  <tr>
    <td>QM-30</td>
    <td>Produktqualität</td>
    <td>Die Produktqualität wird durch mehrere Tests sichergestellt.</td>
  </tr>
</table>


# 11 Testszenarien

Um eine gleichbleibende Testumgebung sicher zu stellen wird eine "Mockup" Webseite erstellt, welche die Möglichkeit bietet eigene Artikel hoch zu laden und mit dem T2M Tool auswerten zu lassen. Diese bietet uns dann die Möglichkeit folgende Testszenarien auf Erfüllung zu überprüfen:

<table>
  <tr>
    <td>ID</td>
    <td>Testfall</td>
    <td>Priorität</td>
  </tr>
  <tr>
    <td>TF-10</td>
    <td>Der selbe Text generiert die selbe Musik</td>
    <td>Hoch</td>
  </tr>
  <tr>
    <td>TF-20</td>
    <td>Der Stop-button stoppt die Musik</td>
    <td>Niedrig</td>
  </tr>
  <tr>
    <td>TF-30</td>
    <td>Der Start-button startet die Musikwiedergabe</td>
    <td>Hoch</td>
  </tr>
  <tr>
    <td>TF-40</td>
    <td>Der Pause-button pausiert die Musikwiedergabe</td>
    <td>Niedrig</td>
  </tr>
  <tr>
    <td>TF-50</td>
    <td>Bei einem Artikel ohne Text wird keine Musik wiedergegeben</td>
    <td>Hoch</td>
  </tr>
  <tr>
    <td>TF-60</td>
    <td>Bei eingabe eines nicht deutschen Textes wird keine Musik ausgegeben</td>
    <td>Medium</td>
  </tr>
  <tr>
    <td>TF-70</td>
    <td>Bei verschiedenen Texten werden verschieden klingende Musik erzeugt.</td>
    <td>Hoch</td>
  </tr>
</table>


# 12 Entwicklungsumgebung

<table>
  <tr>
    <td>Programmiersprache</td>
    <td>Java 8 </td>
  </tr>
  <tr>
    <td>IntelliJ IDEA 2017 </td>
    <td>IntelliJ IDEA ist eine integrierte Entwicklungsumgebung (IDE) des Softwareunternehmens JetBrains für die Programmiersprache Java.</td>
  </tr>
  <tr>
    <td>Versionskontrolle</td>
    <td>Git</td>
  </tr>
  <tr>
    <td>Projektmanagement</td>
    <td>github.com/AudiophileDev 
Kanban Boards
Meilensteinübersicht
Dokumentation 
Quellcode
Asana - T2M 
Aufgabenverteilung
Zeitmanagement
Teamkalendar
Teamchat
Statusübersicht
Instagantt 
Zeitmanagement zusammen mit Asana</td>
  </tr>
</table>


# 13 Ergänzungen

<table>
  <tr>
    <td>Maximale Größe des ausführbaren Programms</td>
    <td>Größe des ausführbaren Programms wird nicht mehr als  10MB</td>
  </tr>
</table>


# 14 Glossar

<table>
  <tr>
    <td>GUI</td>
    <td>Graphische Benutzeroberfläche</td>
  </tr>
</table>


