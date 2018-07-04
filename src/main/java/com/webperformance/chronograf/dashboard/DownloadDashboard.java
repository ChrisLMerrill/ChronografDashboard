package com.webperformance.chronograf.dashboard;

import javax.ws.rs.client.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DownloadDashboard
	{
	public static void main(String[] args)
		{
		String hostname = args[0];

		if (args.length > 1)
			downloadOne(hostname, args[1]);
		else
			downloadAll(hostname);
		}

	private static void downloadOne(String hostname, String dash_id)
		{
		Client client = ClientBuilder.newClient();
		final WebTarget target = client.target("http://" + hostname).path("chronograf").path("v1").path("dashboards").path(dash_id);
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

	private static void downloadAll(String hostname)
		{
		Client client = ClientBuilder.newClient();
		final WebTarget target = client.target("http://" + hostname).path("chronograf").path("v1").path("dashboards");
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
