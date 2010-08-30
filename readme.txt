            StLab - graphical interface for the ToneLab ST
            =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

Requirements:
=============

* a VOX ToneLab ST unit
* an installed VOX ToneLab ST USB-to-MIDI driver (see installation below)
* Java Runtime Environment (JRE) 1.6 installed on your computer (or get it from java.com)

Installation:
=============

1. Install ToneLab ST USB-to-MIDI driver from voxamps.com
2. Extract the StLab archive to your hard disk
3. Connect the ToneLab ST unit to your computer using an USB-AB cable and power on the device
4. Running StLab:
   * on PC (Windows) double-click the stlab-VERSION.jar
   * on MacOS drag the StLab.app to your Applications folder and double click it there

Configuration options:
======================
You can create a config.properties file in your StLab folder to set/override the following properties:

midi.implementation=(default|Mac|PC)
	You can switch the Midi implementation. By default MacOX uses another implementation that PC (Windows & Linux)
	PC will also work in MacOS X 10.6 or higher.

Release notes:
==============

Download from sourceforge: http://sf.net/projects/stlab

== StLab 0.1-SNAPSHOT
* Current Release 0.1 is only a PREVIEW version and does only contian the LIVE view!
* Live view window with Knobs and buttons to control your VOX Tonelab ST device directly from your screen like you're changing the properties on the device itself.
* When changing presets (on the device or by pressing the buttons in the live view or using left and right cursor keys) the Live view displays the options of the current preset - the Tonelab device does not.
* Set all device parametes of your currently activated preset from Amp (including STD,SPL,CST) to Reverb. (excluding the expression pedal and tapping speed!)

== Planned StLab 0.2
* fixed not transfering cabinet value to the device
* implemented decoding mod/delay speed

== Planned version 1.0:
* Double click on display to enter preset number to switch to
* GUI: shadowed option Knobs when not in option mode?
* Model: decode & encode missing program data (Expression Pedal?)
* Implement incoming save command
* Expression Pedal
* Poll for device availability and program change!
* Preset list (request from and save to device) edit by using LIVE view
* Save & Load (single) presets to/from file
* display if current preset has been changed and got not written to file or device

== Planned version X:
* import Preset lists in VOX' format
* export single presets to a webserver ;)
* print preset list (for going abroad) ;)
