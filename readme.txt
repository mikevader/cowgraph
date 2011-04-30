ZBeans Projects / CowGraph
==========================

ZBeans
------

ZBeans ist der Überbegriff für unsere Projekte welche mi der Netbeans Platform zu tun haben.
Z steht für eine Firma in Z wie Zürich bei welcher die Gründer dieses Projektes, zu Beginn dieses Opensource-Projektes gearbeitet haben.
Wir verwenden in unseren Projekten auch entsprechende Namespaces und Package-Names, welche immer mit "zbeans" beginnen.

ACHTUNG: Da wir (noch) keine entsprechende Domain haben, verwenden wir NICHT org.zbeans noch net.zbeans noch com.zbeans.


CowGraph
--------

CowGraph ist die erste Beispiel/Demo-Applikation die wir gemeinsam auf der Netbeans Platform entwickeln.
Die Applikation ermöglicht es sogenannte "CashCow"-Grafiken zu erstellen und versioniert abzulegen, so dass die Entwicklung der Grafiken über die Zeit verfolgt werden kann.


Naming Conventions
------------------

Package Names beginnen immer mit "zbeans", da es sich um Opensource handelt ist darauf zu achten, keinerlei Firmen-Namen in den Source-Codes zu verwenden.

Beispiele für Package Names: zbeans.cowgraph, zbeans.simple


Entwicklungs-Umgebung
---------------------

Wir verwenden folgende Entwicklungs-Umgebung:
- Netbeans 7.0 (aktuell RC1)
- Versionskontrolle mit GIT (vorwiegend über GIT-Bash-Konsole)

Die Entwicklungs-Umgebung ist wie folt zu installieren und einzurichten:

1. NetBeans installieren: 
	http://netbeans.org/downloads/index.html
	(die Java SE Version reicht bereits aus)

2. Account auf github erstellen
	https://github.com/

3. Git installieren und konfigurieren:
	Gemäss Anleitung: http://help.github.com/win-set-up-git/
	ACHTUNG: 
	Git-Bash speichert alle Einstellungen im Working Directory wo es sich sktuell befindet (Command: pwd), 
	Auch wird dort hin ausgecheckt wo die Konsole sich aktuell befindet: mit cd in entsprechendes Verzeichnis wechseln!
	Deshalb wird empfohlen, bevor Git gemäss Anleitung konfiguriert wird, 
	die Git-Bash-Konsole auf das gewünschte Working-Directory für ZBeans-Projekte zu konfigurieren 
	(einfach in der Windows-Verknüpgung->Properties->Start In)

4. Dann auf github.com nach unserem Projekt "zbeans/cashcow" suchen und dieses "forken" und auschecken:
	- Gemäss Anleitung unter http://help.github.com/fork-a-repo/
	- $ git clone git@github.com:<youusername>/cashcow.git

5. (Optional) Git Plugin für Netbeans installieren
	[Optional, weil es nicht wahnsinnig hilfreich zu sein scheint.]
	- http://nbgit.org/
	- nbm file von der Website runterladen
	- in NetBeans unter Tools-> Plugins->Downloaded installieren

6. In Netbeans eine ProjetcGroup einrichten:
	- Unter File/Project Group/New
	- Folder Of Projects: Das cashcow-Verzeichnis in welches Du die zbeans Projekte ausgecheckt hast
	- Es werden jeweils automatisch alle Projekte geöffnet, die dort ausgecheckt sind (kein Import/Refresh wie in Eclipse nötig)

Hier noch ein paar Links zu git Infoquellen:
- http://help.github.com/
- http://gitref.org/
- http://progit.org/book/

Bei Fragen zur Entwicklungs-Umgebung kontaktiert ihr Michael.


GIT Howto
---------

Alle folgenden Befehle sind im gewünschten Verzeichnis (allenfalls mit cd wechseln) auszuführen.

1. Updating from Git-Hub
	git pull upstream master

2. Committing (nur in mein Repository):
	git add .	
	git commit -m "Dein Commmit Text*  // nur lokal
	git push  // in mein repository

3. Commiting (von eigenem Repository in gemeinsames Repository):
	- auf github.com einloggen
	- im Repository pull request absetzen 
		(z.B. auf eigener Repository Seite auf Seite Commits)
	- danach wird man auf geshared Repository weitergeleitet dort 
		Request akzeptieren
		(ganz nach unten scrollen)

4. Mergen (lokal):
	git add .
	git commit -m "blabla*  // nur lokal
	git push // in mein remote repository (optional, sicher ist sicher)
	git pull upstream master
	Dann in netbeans alle aufgeführten conflicted files von Hand korrigieren
	git commit -a -m "merged blabla"


Kontakte / Lead-Entwickler
--------------------------

Michael Mühlebach
Rolf Bruderer

