package org.jsresources.midi;

/*
 *	SendSysex.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;


/**	<titleabbrev>SendSysex</titleabbrev>
	<title>Deliver a system exclusive message to a MIDI device</title>

	<formalpara><title>Purpose</title>
	<para>Delivers a single system exclusive message. This is commonly
	useful with a MIDI port as the target device, though any available
	Java Sound MidiDevice could be chosen, even the software
	synthesizer.</para>
	</formalpara>

	<formalpara><title>Usage</title>
	<para>
	<cmdsynopsis><command>java SendSysex</command>
	<arg choice="optional"><replaceable class="parameter">devicename</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">hexstring</replaceable></arg>
	</cmdsynopsis>
	</para></formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><replaceable class="parameter">devicename</replaceable></term>
	<listitem><para>output to named device. If not given, Java Sound's default device is used</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">hexstring</replaceable></term>
	<listitem><para>the content of the message to send in hexadecimal notation</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>
	<para>Not well-tested.</para>
	</formalpara>

	<formalpara><title>Source code</title>
	<para>
	<ulink url="SendSysex.java.html">SendSysex.java</ulink>,
	<ulink url="MidiCommon.java.html">MidiCommon.java</ulink>
	</para>
	</formalpara>

*/
public class SendSysEx
{
	/**	Flag for debugging messages.
	 	If true, some messages are dumped to the console
	 	during operation.
	*/
	private static boolean		DEBUG = true;



	public static void main(String[] args)
		throws MidiUnavailableException, InvalidMidiDataException
	{
		String	strMessage = null;
//		int	nArgumentIndexOffset = 0;
		String	strDeviceName = null;
		if (args.length == 2)
		{
			strDeviceName = args[0];
			strMessage = args[1];
		}
		else if (args.length == 1)
		{
			strMessage = args[0];
		}
		else
		{
			printUsageAndExit();
		}
		
		int	nLengthInBytes = strMessage.length() / 2;
		byte[]	abMessage = new byte[nLengthInBytes];
		for (int i = 0; i < nLengthInBytes; i++)
		{
			abMessage[i] = (byte) Integer.parseInt(strMessage.substring(i * 2, i * 2 + 2), 16);
		}

		MidiDevice	outputDevice = null;
		Receiver	receiver = null;
		if (strDeviceName != null)
		{
			MidiDevice.Info	info = MidiCommon.getMidiDeviceInfo(strDeviceName, true);
			if (info == null)
			{
				out("no device info found for name " + strDeviceName);
				System.exit(1);
			}
			outputDevice = MidiSystem.getMidiDevice(info);
			outputDevice.open();
			if (outputDevice == null)
			{
				out("wasn't able to retrieve MidiDevice");
				System.exit(1);
			}
			receiver = outputDevice.getReceiver();
		}
		else
		{
			/*	We retrieve a Receiver for the default
				MidiDevice.
			*/
			receiver = MidiSystem.getReceiver();
		}
		if (receiver == null)
		{
			out("wasn't able to retrieve Receiver");
			System.exit(1);
		}

		/*	Here, we prepare the MIDI messages to send.
		*/
		SysexMessage	sysexMessage = new SysexMessage();
		sysexMessage.setMessage(abMessage, abMessage.length);

		/*
		 *	Deliver the message
		 */
		receiver.send(sysexMessage, -1);

		/*
		 *	Clean up.
		 */
		receiver.close();
		if (outputDevice != null)
		{
			outputDevice.close();
		}
		System.exit(0);
	}



	private static void printUsageAndExit()
	{
		out("SendSysex: usage:");
		out("  java SendSysex [<device name>] <hexstring>");
		out("    <device name>   output to named device. If not given, Java Sound's default device is used.");
		out("    <hexstring>     the content of the message to send in hexadecimal notation");
		out("  example: java SendSysex  F0112233F7");
		System.exit(1);
	}



	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}



	private static void out(Throwable t)
	{
		t.printStackTrace();
	}
}



/*** SendSysex.java ***/
