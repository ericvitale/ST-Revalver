/**
 *  Revalver
 *
 *  Version 1.0.0 - 08/17/16
 *   -- Initial Build
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  You can find this smart app @ https://github.com/ericvitale/ST-Revalver
 *  You can find my other device handlers & SmartApps @ https://github.com/ericvitale
 *
 */
 
definition(
    name: "${appName()}",
    namespace: "ericvitale",
    author: "Eric Vitale",
    description: "Manage your valves with SmartThings like a boss.",
    category: "",
    iconUrl: "https://s3.amazonaws.com/ev-public/st-images/revalver-icon.png", 
    iconX2Url: "https://s3.amazonaws.com/ev-public/st-images/revalver-icon-2x.png", 
    iconX3Url: "https://s3.amazonaws.com/ev-public/st-images/revalver-icon-3x.png",
    singleInstance: true)

preferences {
    page(name: "startPage")
    page(name: "parentPage")
    page(name: "childStartPage")
}

def startPage() {
    if (parent) {
        childStartPage()
    } else {
        parentPage()
    }
}

def parentPage() {
	return dynamicPage(name: "parentPage", title: "", nextPage: "", install: false, uninstall: true) {
        section("Create a new child app.") {
            app(name: "childApps", appName: appName(), namespace: "ericvitale", title: "New Valve Automation", multiple: true)
        }
    }
}
 
def childStartPage() {
	return dynamicPage(name: "childStartPage", title: "", install: true, uninstall: true) {
        
        section("Trigger Type") {
        	input "triggerType", "enum", title: "Trigger Type", required: true, multiple: false, options: ["Contact", "Schedule", "Switch", "Water Sensor"], submitOnChange: true
        }
        
        if(triggerType == "Water Sensor") {
            section("Water Sensor") {
                input "waterSensors", "capability.waterSensor", title: "Which?", required: false, multiple: true
            }
        } else if(triggerType == "Switch") {
            section("Trigger") {
                input "switches", "capability.switch", title: "Switches", multiple: true, required: false, submitOnChange: true
                if(switches != null) {
                    input "closeEvent", "enum", title: "Close Valve When?", required: true, multiple: false, options: ["On", "Off", "Never"], defaultValue: "On"
                    input "openEvent", "enum", title: "Open Valve When?", required: true, multiple: false, options: ["On", "Off", "Never"], defaultValue: "Never"
                }
            }
        } else if(triggerType == "Schedule") {
        	//ToDo
        } else if(triggerType == "Contact") {
        	section("Contact Sensor Subscriptions") {
            	input "contacts", "capability.contactSensor", title: "Which?", required: false, multiple: true, submitOnChange: true
                if(contacts != null) {
            		input "closeEvent", "enum", title: "Close Valve When?", required: true, multiple: false, options: ["Open", "Closed", "Never"], defaultValue: "Open"
                	input "openEvent", "enum", title: "Open Valve When?", required: true, multiple: false, options: ["Open", "Closed", "Never"], defaultValue: "Never"
                }
            }
        }
        
        section("Valves") {
        	input "valves", "capability.valve", title: "Valves?", required: false, multiple: true
        }
        
        section("Lights") {
            input "useLights", "bool", title: "Turn on Lights?", required: true, defaultValue: false, submitOnChange: true
            if(useLights) {
				input "lights", "capability.switch", title: "Which?", required: false, multiple: true
            }
        }
        
        section("Electronic Notifications") {
        	input "push", "bool", title: "Send Push Notifications?", required: true, defaultValue: true
        }
        
        section("Test This Automation") {
        	input "testAutomation", "bool", title: "Test This Automation?", required: true, defaultValue: false, submitOnChange: true
            if(testAutomation) {
            	input "testLights", "capability.switch", title: "Which?", required: false, multiple: true
            }
        }
        
        section("Setting") {
        	label(title: "Assign a name", required: false)
            input "active", "bool", title: "Rules Active?", required: true, defaultValue: true
            input "logging", "enum", title: "Log Level", required: true, defaultValue: "INFO", options: ["TRACE", "DEBUG", "INFO", "WARN", "ERROR"]
        }
	}
}

private def appName() { return "${parent ? "Valve Automation" : "Revalver"}" }

private determineLogLevel(data) {
    switch (data?.toUpperCase()) {
        case "TRACE":
            return 0
            break
        case "DEBUG":
            return 1
            break
        case "INFO":
            return 2
            break
        case "WARN":
            return 3
            break
        case "ERROR":
        	return 4
            break
        default:
            return 1
    }
}

def log(data, type) {
    data = "Revalver -- ${data ?: ''}"
        
    if (determineLogLevel(type) >= determineLogLevel(settings?.logging ?: "INFO")) {
        switch (type?.toUpperCase()) {
            case "TRACE":
                log.trace "${data}"
                break
            case "DEBUG":
                log.debug "${data}"
                break
            case "INFO":
                log.info "${data}"
                break
            case "WARN":
                log.warn "${data}"
                break
            case "ERROR":
                log.error "${data}"
                break
            default:
                log.error "Revalver -- Invalid Log Setting of ${type}"
        }
    }
}

def installed() {
	log("Begin installed.", "DEBUG")
	initialization() 
    log("End installed.", "DEBUG")
}

def updated() {
	log("Begin updated().", "DEBUG")
	unsubscribe()
    unschedule()
	initialization()
    log("End updated().", "DEBUG")
}

def initialization() {
	log("Begin initialization().", "DEBUG")
    
    if(parent) {
    	if(triggerType == "Water Sensor") {
        	initWaterSensorChild()
        } else if(triggerType == "Switch") {
        	initSwitchChild()
        } else if(triggerType == "Schedule") {
        	initScheduleChild()
        } else if(triggerType == "Contact") {
        	initContactChild()
        }
    } else {
    	initParent() 
    }
    
    log("End initialization().", "DEBUG")
}

def initParent() {
	log("initParent()", "DEBUG")
}

def initWaterSensorChild() {
	log("Begin initWaterSensorChild().", "DEBUG")
    log("active = ${active}.", "INFO")
    log("triggerType = ${triggerType}.", "INFO")
    
    unsubscribe()
    
    if(waterSensors != null) {
    	waterSensors.each { it->
        	log("Water Sensor ${it.label} selected.", "INFO")
        }
    	subscribe(waterSensors, "water.wet", waterHandler)
        log("Subscribed to water sensor events.", "INFO")
    }
    
    initValves()

    log("End initWaterSensorChild().", "DEBUG")
}

def initSwitchChild() {
	log("Begin initSwitchChild().", "DEBUG")
    log("active = ${active}.", "INFO")

    unsubscribe()
    
    if(closeEvent == openEvent) {
    	log("You cannot set the close event and the open event to the same value. Defaulting to close when switch is on and never open valve.", "WARN")
        closeEvent = "On"
        openEvent = "Never"
    }
    
    if(switches != null) {
        switches.each { it->
        	log("Switch ${it.label} selected.", "INFO")
        }
    	subscribe(switches, "switch", switchHandler)
        log("Subscribed to switch events.", "INFO")
    }
    
    initValves()
    
    log("End initSwitchChild().", "DEBUG")
}

def initScheduleChild() {
	log("Begin initScheduleChild().", "DEBUG")
    log("active = ${active}.", "INFO")
    unsubscribe()
    
    initValves()
    
    log("End initScheduleChild().", "DEBUG")
}

def initContactChild() {
	log("Begin initContactChild().", "DEBUG")
    log("active = ${active}.", "INFO")
    unsubscribe()
    
    if(closeEvent == openEvent) {
    	log("You cannot set the close event and the open event to the same value. Defaulting to close when contact is opened and never open valve.", "WARN")
        closeEvent = "Open"
        openEvent = "Never"
    }
    
    if(contacts != null) {
    	contacts.each { it->
        	log("Contact Sensor ${it.label} selected.", "INFO")
        }
    	subscribe(contacts, "contact", contactHandler)
        log("Subscribed to contact events.", "INFO")
    }
    
    initValves()
    
    log("End initContactChild().", "DEBUG")
}

def initValves() {
	log("Begin initValves", "DEBUG")
    
    if(valves != null) {
    	valves.each {it->
        	log("Valve selected: ${it.label}.", "INFO")
        }
        
        subscribe(valves, "contact", valveHandler)
        log("Subscribed to valve events.", "INFO")
    } else {
    	log("No valves selected.", "INFO")
    }
    
    log("End initValves", "DEBUG")
}

def switchHandler(evt) {
	log("Manual switch (${evt.device.label}) -- ${evt.value}.", "INFO")
    
    if(evt.value == closeEvent.toLowerCase()) {
    	if(testAutomation) {
        	log("Testing Automation: turning test lights on, simulating a valve closing.", "INFO")
        	testLightsOn()
        } else {
	        closeValves()
        }
        
        if(useLights) {
        	lightsOn()
        }
    } else if(evt.value == openEvent.toLowerCase()) {
    	if(testAutomation) {
        	log("Testing Automation: turning test lights off, simulating a valve opening.", "INFO")
        	testLightsOff()
        } else {
	        openValves()
        }
    }
}

def waterHandler(evt) {
	log("Water detected by (${evt.device.label}) -- ${evt.value}.", "INFO")
    log("Event = ${evt.descriptionText}.", "DEBUG")
    if(testAutomation) {
        log("Testing Automation: turning test lights on.", "INFO")
        testLightsOn()
    } else {
		closeValves()
    }
    
	if(useLights) {
    	lightsOn()
    }
}

def contactHandler(evt) {
	log("Contact event by (${evt.device.label}) -- ${evt.value}.", "INFO")
    
    if(evt.value == closeEvent.toLowerCase()) {
    	if(testAutomation) {
        	log("Testing Automation: turning test lights on, simulating a valve closing.", "INFO")
        	testLightsOn()
        } else {
	        closeValves()
        }
        
        if(useLights) {
        	lightsOn()
        }
    } else if(evt.value == openEvent.toLowerCase()) {
    	if(testAutomation) {
        	log("Testing Automation: turning test lights off, simulating a valve opening.", "INFO")
        	testLightsOff()
        } else {
	        openValves()
        }
    }
}

def valveHandler(evt) {
	log("Valve event by (${evt.device.label}).", "INFO")
    log("${evt.descriptionText}.", "INFO")
    if(push) {
    	state.phrase = "${evt.descriptionText}"
    	asyncSendPushNotification()
    } 
}

def closeValves() {
	log("Closing valves.", "INFO")
    valves.each { it->
        it.close()
        log("Closing ${it.label} now...", "INFO")
        if(push) {
        	state.phrase = "${it.label} is closing"
        	asyncSendPushNotification()
        }
    }
}

def openValves() {
	log("Opening valves.", "INFO")
    valves.each { it->
        it.open()
        log("Opening ${it.label} now...", "INFO")
        if(push) {
        	state.phrase = "${it.label} is opening"
        	asyncSendPushNotification()
        }
    }
}

def asyncSendPushNotification() {
	sendPush(state.phrase)
}

def lightsOn() {
	if(useLights) {
    	if(lights != null) {
        	lights.each { it->
            	it.on()
            }
        }
    }
}

def lightsOff() {
	if(useLights) {
    	if(lights != null) {
        	lights.each { it->
            	it.off()
            }
        }
    }
}

def testLightsOff() {
    if(testAutomation) {
    	if(testLights != null) {
        	testLights.each { it->
            	it.off()
            }
        }
        if(push) {
        	state.phrase = "Testing Automation Valve Opening"
        	asyncSendPushNotification()
        }
    }
}

def testLightsOn() {
    if(testAutomation) {
    	if(testLights != null) {
        	testLights.each { it->
            	it.on()
            }
        }
        if(push) {
        	state.phrase = "Testing Automation Valve Closing"
        	asyncSendPushNotification()
        }
    }
}