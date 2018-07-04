package com.webperformance.chronograf.dashboard;

import org.json.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UploadDashboard
	{
	public static void main(String[] args) throws FileNotFoundException, JSONException
		{
		String hostname = args[0];
		if (args.length > 1)
			uploadOne(hostname, args[1]);
		else
			uploadAll(hostname);
		}

	private static void uploadOne(String hostname, String dash_id)
		{
		final String filename = dash_id + ".json";
		String dashboards;
		try
			{
			dashboards = new Scanner(new File(filename)).useDelimiter("\\Z").next();
			}
		catch (IOException e)
			{
			e.printStackTrace();
			return;
			}

		Client client = ClientBuilder.newClient();
		final WebTarget target = client.target("http://" + hostname).path("chronograf").path("v1").path("dashboards");
		final Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(dashboards));
		if (response.getStatus() != 200)
			System.out.println(String.format("Chronograf said: %d %s", response.getStatus(), response.getStatusInfo().getReasonPhrase()));
		else
			System.out.println("uploaded dashboard to Chronograf from " + filename);
		}

	private static void uploadAll(String hostname) throws FileNotFoundException, JSONException
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
		final WebTarget target = client.target("http://" + hostname).path("chronograf").path("v1").path("dashboards");

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
	}
