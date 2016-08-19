# ST-Revalver

## Summary
Manage your valves with SmartThings like a boss. Revalver allows you to control your valves in muliple ways. Example automations:

Water Sensor - Most obvious application. When a water sensor detects water, close the valve.

**Contact Sensor** - For each action of a valve (open & close) choose to do either of these based on the current event of a contact sensor, timer, or ignore. 

Example 1: When a contact sensor opens, close the valve. When the contact sensor closes, open the value.
Example 2: When a contact sensor opens, open the valve. When the contact sensor closes, close the value.
Example 3: When a contact sensor opens, close the valve. When the contact sensor closes, do nothing.
Example 4: When a contact sensor opens, close the valve. When the contact sensor closes, open the valve in 5 minutes.


## This device handler supports
On / Off
Setting Color
Setting Color Temperature
Setting Brightness
1 to 10 groups as a single device
0 to 3 scenes for the group (unfortunatly if you don't use these you cannot remove them from UI via settings)

## Installation via GitHub Integration
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My Device Types" section in the navigation bar.
3. Click on "Settings".
4. Click "Add New Repository".
5. Enter "ericvitale" as the namespace.
6. Enter "ST-LIFX-Group-of-Groups" as the repository.
7. Hit "Save".
8. Select "Update from Repo" and select "ST-LIFX-Group-of-Groups".
9. Select "lifx-group-of-groups.groovy".
10. Check "Publish" and hit "Execute".
11. See the "Preferences" & "How to get your API Token" sections below on how to configure.

## Manual Installation (if that is your thing)
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My Device Types" section in the navigation bar.
3. On your Device Types page, click on the "+ New Device Type" button on the right.
4 . On the "New Device Type" page, Select the Tab "From Code" , Copy the "lifx-group-of-groups.groovy" source code from GitHub and paste it into the IDE editor window.
5. Click the blue "Create" button at the bottom of the page. An IDE editor window containing device handler template should now open.
6. Click the blue "Save" button above the editor window.
7. Click the "Publish" button next to it and select "For Me". You have now self-published your Device Handler.
8. See the "Preferences" & "How to get your API Token" sections below on how to configure.

## Preferences
1. API Token - [Required] You have to get this from LIFX. It is a long character string so text it to yourself and copy and paste it in.
2. Groups 1 to 10 - [Required, 1st] You can choose from up to 10 groups, the first group is required. Enter the group name, case sensitive. No need to enter a group id.
3. Scenes 1 to 3 - Select a brightness level and a color (kelvin temperatures should be entered like this: "kelvin:2750" with no quotes.
3. Log Level - Enter: TRACE, DEBUG, INFO, WARN, ERROR

## Acknowledgements
Insperation to create this device handler came from AdamV (https://github.com/adampv/smartthings/blob/master/LIFXGroupversion.groovy). Adam also credits Nicolas Cerveaux.

## How to get your API Token
Navigate to https://cloud.lifx.com, sign in and then go to the settings section and select generate new token.

## Known Issues
1. I can't get the UI of the device to update when the state changes without hitting referesh.

## Design Decisions
1. I decided to hard code up to 10 groups versus having a single group preference that was comma delimited. The comma delimited version would allow for any number of groups. I opted for the fixed number as I think it is a better user experience for the less technical people out there.
2. Still trying to decide what I am going to do in the UI when the individual groups or bulbs are not in sync. For example you manually turn on a single bulb or goup using the LIFX app. Right now I show "Onish" if some of the lights are on and some are off.
