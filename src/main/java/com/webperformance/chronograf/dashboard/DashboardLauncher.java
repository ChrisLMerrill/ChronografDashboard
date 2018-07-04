package com.webperformance.chronograf.dashboard;

import io.airlift.airline.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DashboardLauncher
	{
	public static void main(String[] args)
		{
		List<Class<? extends Runnable>> implementors = new ArrayList<>();
		implementors.add(BackupCommand.class);
		implementors.add(RestoreCommand.class);
		Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("chronograf-dashboard")
			.withDescription("Chronograf Dashboard backup and restore")
			.withDefaultCommand(Help.class)
			.withCommands(Help.class)
			.withCommands(implementors);
		Cli<Runnable> parser = builder.build();

		final Runnable command;
		try
            {
			command = parser.parse(args);
            }
        catch (Exception e)
            {
            parser.parse(new String[0]).run();
            return;
            }
        try
            {
            command.run();
            }
        catch (Exception e)
            {
			System.out.println(String.format("Command failed due to a %s.\n%s", e.getClass().getSimpleName(), e.getMessage()));
			e.printStackTrace(System.err);
			}
		}
	}
