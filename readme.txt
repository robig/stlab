            StLab - graphical interface for the Tonelab ST
            =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
            
Configuration options:
======================

midi.implementation=(default|Mac|PC)
	You can switch the Midi implementation. By default MacOX uses another implementation that PC (Windows & Linux)
	PC will also work in MacOS X 10.6 or higher.

Release notes:
==============

== StLab 0.1-SNAPSHOT
Upcoming Release 0.1 is only a PREVIEW version and will only contian the LIVE view!
Program list and preset saving is planned for 1.0 (which comes after 0.1 if no bugfix releases are needed ;) )

=== Included features:
* Live view window with Knobs and buttons to control your VOX Tonelab ST device directly from your screen like you're changing the properties on the device itself.
* When changing presets (on the device or by pressing the buttons in the live view or using left and right cursor keys) the Live view displays the options of the current preset - the Tonelab device does not.
* Set all device parametes from Amp (including STD,SPL,CST) to Reverb. (excluding the expression pedal)
* Set Mod/Delay speed parameter by tapping.  

=== TODO for version 0.1:
There are still some things to do before I will release the first version:
* logging.properties from jar
* BUGFIX: get correct delay speed from device

=== Expected
* between 18-21st Aug 2010

== Planned version 1.0:
* GUI: shadowed option Knobs when not in option mode?
* Model: decode & encode missing program data (Expression Pedal?)
* Implement incoming save command
* Expression Pedal
* Poll for device availability and program change!
* Preset list (request from and save to device) edit by using LIVE view
* Save & Load (single) presets to/from file

== Planned version X:
* import Preset lists in VOX' format
* export single presets to a webserver ;)
* print preset list (for going abroad) ;)
