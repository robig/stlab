package net.robig.stlab;
/*
 *	MidiNote.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2006 by Matthias Pfisterer
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

import java.math.BigInteger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import org.jsresources.midi.MidiCommon;


// TODO: an optional delay parameter that is added to getMicrosecondPosition to be used as timestamp for the event delivery.

/**	<titleabbrev>MidiNote</titleabbrev>
	<title>Playing a note on a MIDI device</title>

	<formalpara><title>Purpose</title>
	<para>Plays a single note on a MIDI device. The MIDI device can
	be a software synthesizer, an internal hardware synthesizer or
	any device connected to the MIDI OUT port.</para>
	</formalpara>

	<formalpara><title>Usage</title>
	<para>
	<cmdsynopsis><command>java MidiNote</command>
	<arg choice="opt"><replaceable class="parameter">devicename</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">keynumber</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">velocity</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">duration</replaceable></arg>
	</cmdsynopsis>
	</para></formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><replaceable class="parameter">devicename</replaceable></term>
	<listitem><para>the name of the device to send the MIDI messages to</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">keynumber</replaceable></term>
	<listitem><para>the MIDI key number</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">velocity</replaceable></term>
	<listitem><para>the velocity</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">duration</replaceable></term>
	<listitem><para>the duration in milliseconds</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>
	<para>Not well-tested.</para>
	</formalpara>

	<formalpara><title>Source code</title>
	<para>
	<ulink url="MidiNote.java.html">MidiNote.java</ulink>,
	<ulink url="MidiCommon.java.html">MidiCommon.java</ulink>
	</para>
	</formalpara>

*/
public class MidiNote
{

	public static byte[] hex2byte(String hex){
		String strMessage=hex.replaceAll(" ", "").toUpperCase();
		int	nLengthInBytes = strMessage.length() / 2;
		byte[]	abMessage = new byte[nLengthInBytes];
		for (int i = 0; i < nLengthInBytes; i++)
		{
			abMessage[i] = (byte) Integer.parseInt(strMessage.substring(i * 2, i * 2 + 2), 16);
		}
		return abMessage;
	}
	public static byte[] ohex2byte(String hex) {
		return new BigInteger(
				hex.replaceAll(" ", "").toLowerCase(),
				16).toByteArray();
	}
	
	public static String toHexString(byte bytes[])
	{
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i)
		{
			retString.append(
				Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return retString.toString();
	}
	
	/**	Flag for debugging messages.
	 	If true, some messages are dumped to the console
	 	during operation.
	*/
	private static boolean		DEBUG = true;



	public static void main(String[] args)
	{
		MidiCommon.listDevices(true, true);
		if(args.length<1)
			printUsageAndExit();
		
		String	strDeviceName = args[0];
				
		MidiDevice	outputDevice = null;
		Receiver	receiver = null;
		if (strDeviceName != null)
		{
			MidiDevice.Info	info = MidiCommon.getMidiDeviceInfo(strDeviceName, true);
			//info=MidiCommon.getMidiDeviceInfo(8);
			if (info == null)
			{
				out("no device info found for name " + strDeviceName);
				System.exit(1);
			}
			try
			{
				outputDevice = MidiSystem.getMidiDevice(info);
				if (DEBUG) out("Using MidiDevice:"+info.getName());
				outputDevice.open();
			}
			catch (MidiUnavailableException e)
			{
				if (DEBUG) out(e);
			}
			if (outputDevice == null)
			{
				out("wasn't able to retrieve MidiDevice");
				System.exit(1);
			}
			try
			{
				receiver = outputDevice.getReceiver();
			}
			catch (MidiUnavailableException e)
			{
				if (DEBUG) out(e);
			}
		}
		else
		{
			/*	We retrieve a Receiver for the default
				MidiDevice.
			*/
			try
			{
				receiver = MidiSystem.getReceiver();
			}
			catch (MidiUnavailableException e)
			{
				if (DEBUG) { out(e); }
			}
		}
		if (receiver == null)
		{
			out("wasn't able to retrieve Receiver");
			System.exit(1);
		}

		if (DEBUG) out("Receiver: " + receiver);
		/*	Here, we prepare the MIDI messages to send.
			Obviously, one is for turning the key on and
			one for turning it off.
		*/
		SysexMessage onMessage = new SysexMessage();
		//ShortMessage message2 = new ShortMessage();
		
		try
		{
			//soll: 00  F0 42 30 00 01 08 1C 20  00 F7 
			byte[] data=hex2byte("F0 42 30 00 01 08 1C 20 00 F7");
			data=hex2byte("F0112233F7");
			data=hex2byte("F0 42 30 00 01 08 40 00  77 06 32 1A 5A 53 2B 00 10  2A 48 37 20 09 01 11 00  09 18 17 78 00 01 00 00 20  00 64 00 F7");
			//hex2byte("0f00")
			String back = toHexString(data);
			int status = (data[0] & 0xFF);
			onMessage.setMessage(data,data.length);
			//message2.

			if (DEBUG)
			    {
			    out("Msg: " + onMessage.getStatus() + " " + toHexString(onMessage.getMessage()));
			   
			}
		}
		catch (InvalidMidiDataException e)
		{
			if (DEBUG) { out(e); System.exit(1);}
		}

		/*
		 *	Turn the note on
		 */
		if (DEBUG) out("sending message: ");
		receiver.send(onMessage, -1);
		if (DEBUG) out("...sent");

		/*
		 *	Wait for the specified amount of time
		 */
		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
			if (DEBUG) out(e);
		}

		/*
		 *	Clean up.
		 */
		receiver.close();
		if (outputDevice != null)
		{
			outputDevice.close();
		}
	}



	private static void printUsageAndExit()
	{
		out("MidiNote: usage:");
		out("  java MidiNote <device name>");
		out("    <device name>\toutput to named device");
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



/*** MidiNote.java ***/