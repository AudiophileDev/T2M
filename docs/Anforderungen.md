# Anforderungen

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
