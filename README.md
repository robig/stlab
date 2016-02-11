StLab - graphical interface for VOX ToneLab ST
==============================================

Requirements:
=============

* a VOX ToneLab ST unit
* an installed VOX ToneLab ST USB-to-MIDI driver (see installation below)
* Java Runtime Environment (JRE) 1.6 or later installed on your computer (get it from java.com)

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

    startup.checkforupdates=(true/false)
Automatic check for updates at sourceforge can be disabled by setting to false.
Note that disabling this won't speed up the application start because update check is running in background.
	
    knobs.mouse.sensitivity=INTEGER
Mouse sensitivity when turning knobs can be set with this option. (Default: 150)
	
    knobs.mousewheel.sensitivity=DOUBLE
Mouse wheel sensitivity. (Default: 1.0)

Release notes:
==============

see changelog.txt


