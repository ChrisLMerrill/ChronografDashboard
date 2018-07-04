package com.webperformance.chronograf.dashboard;

import io.airlift.airline.*;
import org.json.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "restore", description = "Backup one or more dashboards")
public class RestoreCommand implements Runnable
	{
	@Arguments(description = "The url of the Chronograf app", required = true)
	@SuppressWarnings("WeakerAccess")
	public String url;

	@Option(name = "-id", description = "The id of the Chronograf dashboard file to restore (restore all from dashboards.json if not specified)")
	@SuppressWarnings("WeakerAccess")
	public String id;

	@Override
	public void run()
		{
		if (id == null)
			restoreAll(url);
		else
			restoreOne(url, id);
		}

	private static void restoreOne(String url, String dash_id)
		{
		final String filename = dash_id + ".json";
		try
			{
			dashboard = new Scanner(new File(filename)).useDelimiter("\\Z").next();
			}
		catch (IOException e)
			{
			e.printStackTrace();
			return;
			}

		Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url).path("chronograf").path("v1").path("dashboards");
		final Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(dashboard));
		if (response.getStatus() == 201)
			System.out.println("uploaded dashboard to Chronograf from " + filename);
		else
			System.out.println(String.format("Chronograf said: %d %s", response.getStatus(), response.getStatusInfo().getReasonPhrase()));
		}

	private static void restoreAll(String url)
		{
		try
			{
			final String filename = "dashboards.json";
			if (! new File(filename).exists())
				{
				System.out.println(filename + " not found.");
				return;
				}

			String dashboards_json = new Scanner(new File(filename)).useDelimiter("\\Z").next();
			JSONObject json = (JSONObject) new JSONTokener(dashboards_json).nextValue();
			JSONArray dashboards = json.getJSONArray("dashboards");

			Client client = ClientBuilder.newClient();
			final WebTarget target = client.target(url).path("chronograf").path("v1").path("dashboards");

			for (int i = 0; i < dashboards.length(); i++)
				{
				JSONObject dashboard = (JSONObject) dashboards.get(i);
				final String name = dashboard.getString("name");
				final Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(dashboard.toString()));
				if (response.getStatus() == 201)
					System.out.println("uploaded dashboard " + name);
				else
					{
					System.out.println(String.format("Chronograf said: %d %s when uploading %s", response.getStatus(), response.getStatusInfo().getReasonPhrase(), name));
					return;
					}
				}
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		}

	private static String dashboard;
	}
