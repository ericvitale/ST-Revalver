# ST-Revalver

## Summary
Manage your valves with SmartThings like a boss. Revalver allows you to group your valves and control them as a single group or to create smaller groups or control individual valves with different automations. 

Automations Include:

**Water Sensor** - Most obvious application. 

Example 1: When a water sensor detects water, close the valve.

**Contact Sensor** - For each action of a valve (open & close) choose to do either of these based on the current event of a contact sensor, timer, or ignore. 

Example 1: When a contact sensor opens, close the valve. When the contact sensor closes, open the value.
Example 2: When a contact sensor opens, open the valve. When the contact sensor closes, close the value.
Example 3: When a contact sensor opens, close the valve. When the contact sensor closes, do nothing.
Example 4: When a contact sensor opens, close the valve. When the contact sensor closes, open the valve in 5 minutes.

**Schedule** - Open and close the valve at specific times daily.

Example 1: Open the valve at 8:30 AM daily, close the valve at 9:30 PM daily.
Example 2: Close the valve at 10:00 PM daily, never open (only manual).

**Toggle** - Open and close the valve continiously on an interval.

Example 1: Toggle the valve every 5 minutes.
Example 2: Toggle the valve every 8 hours.
Example 3: Toggle the valve every 1 day.

**Switch** - For each action of a valve (open & close) choose to do either of these based on the current event of a switch, timer, or ignore. 

Example 1: When a switch is turned on, close the valve. When the switch is turned off, open the value.
Example 2: When a switch is turned on, open the valve. When the switch is turned off, close the value.
Example 3: When a switch is turned on, close the valve. When the switch is turned off, do nothing.
Example 4: When a switch is turned on, close the valve. When the switch is turned off, open the valve in 5 minutes.

## Additional Features
Lights - Turn specified lights on when a valve closes.
Push Notifications - Get notified when a valve is told to open or close and when it actually does.
Testing - Simulation mode allows you to substitute a light for a valve for testing. Valve closed, light on, valve open, light off.

## Installation via GitHub Integration
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My SmartApps" section in the navigation bar.
3. Click on "Settings".
4. Click "Add New Repository".
5. Enter "ericvitale" as the namespace.
6. Enter "ST-Revalver" as the repository.
7. Hit "Save".
8. Select "Update from Repo" and select "ST-Revalver".
9. Select "revalver.groovy".
10. Check "Publish" and hit "Execute".

## Manual Installation (if that is your thing)
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My SmartApps" section in the navigation bar.
3. Click on the "+ New SmartApp" button on the right.
4 . Select the Tab "From Code" , Copy the "revalver.groovy" source code from GitHub and paste it into the IDE editor window.
5. Click the blue "Create" button at the bottom of the page. An IDE editor window containing device handler template should now open.
6. Click the blue "Save" button above the editor window.
7. Click the "Publish" button next to it and select "For Me". You have now self-published your Device Handler.
8. See the "Preferences" & "How to get your API Token" sections below on how to configure.
