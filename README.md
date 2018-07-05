# ChronografDashboard

Backup and restore dashboards from Chronograf

# Installation

Java 8 or greater must be installed as well (with the JAVA_HOME variable set in the usual way).

## From binaries

Unzip and add the bin folder to your path.

## From source

After cloning the repository, run 

```
gradlew(.bat) installDist
```

Then add the build\install\chronograf-dashboard\bin subfolder to your path.

# Usage

The base Chronograf URL must be provided to all commands. If you browse to http://chronograf.com/ and see Chronograf, then that is the base URL. 

## Backup all dashboards

```
chronograf-dashboard backup <url> 
```

If successful, it will respond with: 

```
wrote all dashboards to dashboards.json
```

## Restore all dashboards

To restore the dashboards, you will need the _dashboards.json_ file in the current folder. 

```
chronograf-dashboard restore <url> 
```

If successful, it will respond with this, for each dashboard that was uploaded: 

```
uploaded dashboard <dashboard_name>
```

## Backup one dashboard

To backup one specific dashboard, you will also need the id of the dashboard. Browse to the dashboard and look at the URL. 
The URL will end with a number - that is the dashboard id.

```
chronograf-dashboard backup <url> -id <id> 
```

If successful, it will respond with:

```
wrote dashboard to <id>.json
```

After backing up, I recommend renaming the file to something meaningful, such as FavoriteDashboard.json, though this is not required.

## Restore one dashboard

To restore one specific dashboard, you will need the _.json_ file from the backup. In this case, the _id_ is base name of the file. E.g. if the
backup produced 12.json, then the _id_ is '12'. If you renamed it to _rocketship.json_, then the _id_ is 'rocketship'.  

```
chronograf-dashboard backup <url> -id <id> 
```

If successful, it will respond with:

```
uploaded dashboard to Chronograf from <id>.json
```
