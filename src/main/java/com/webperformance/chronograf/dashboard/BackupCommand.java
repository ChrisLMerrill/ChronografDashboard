package com.webperformance.chronograf.dashboard;

import io.airlift.airline.*;

import javax.ws.rs.client.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "backup", description = "Backup one or more dashboards")
public class BackupCommand implements Runnable
	{
	@Arguments(description = "The url of the Chronograf app", required = true)
	@SuppressWarnings("WeakerAccess")
	public String url;

	@Option(name = "-id", description = "The id of the Chronograf dashboard to backup (backup all if not specified)")
	@SuppressWarnings("WeakerAccess")
	public String id;

	@Override
	public void run()
		{
		if (id == null)
			backupAll(url);
		else
			backupOne(url, id);
		}

	private static void backupOne(String url, String dash_id)
		{
		Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url).path("chronograf").path("v1").path("dashboards").path(dash_id);
		String response = target.request().get(String.class);

		final String filename = dash_id + ".json";
		try (FileOutputStream outstream = new FileOutputStream(filename))
			{
			outstream.write(response.getBytes());
			System.out.println("wrote dashboard to " + filename);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		}

	private static void backupAll(String url)
		{
		Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url).path("chronograf").path("v1").path("dashboards");
		String response = target.request().get(String.class);

		final String filename = "dashboards.json";
		try (FileOutputStream outstream = new FileOutputStream(filename))
			{
			outstream.write(response.getBytes());
			System.out.println("wrote all dashboards to " + filename);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		}

	}
