package com.osten.halp.launcher;

/**
 * Created by server on 2013-12-18.
 */
public class CmdLauncher
{

	public static void main( String[] args ){

		HalpRunner runner = new HalpRunner();
		runner.parseArgs( args );
		runner.start();
	}
}
