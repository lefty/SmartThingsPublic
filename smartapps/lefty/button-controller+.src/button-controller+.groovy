/**
 *  Copyright 2015 SmartThings
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
 *	Button Controller
 *
 *	Author: SmartThings, modified by Jeffrey Hartmann with code from Bruce Ravenel
 *	Date: 2014-5-21
 */
definition(
    name: "Button Controller+",
    namespace: "lefty",
    author: "Jeffrey Hartmann",
    description: "Control devices with buttons like the Aeon Labs Minimote",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/MyApps/Cat-MyApps.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/MyApps/Cat-MyApps@2x.png"
)

preferences {
	page(name: "selectButton")
	for (def i=1; i<=8; i++) {
		page(name: "configureButton$i")
	}

	page(name: "timeIntervalInput", title: "Only during a certain time") {
		section {
			input "starting", "time", title: "Starting", required: false
			input "ending", "time", title: "Ending", required: false
		}
	}
}

def selectButton() {
	dynamicPage(name: "selectButton", title: "First, select your button device", nextPage: "configureButton1", uninstall: configured()) {
		section {
			input "buttonDevice", "capability.button", title: "Button", multiple: false, required: true
		}

		section(title: "More options", hidden: hideOptionsSection(), hideable: true) {

			def timeLabel = timeIntervalLabel()

			href "timeIntervalInput", title: "Only during a certain time", description: timeLabel ?: "Tap to set", state: timeLabel ? "complete" : null

			input "days", "enum", title: "Only on certain days of the week", multiple: true, required: false,
				options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]

			input "modes", "mode", title: "Only when mode is", multiple: true, required: false
		}
        
        section() {
        	label title: "Assign a name:", required: false
        }
	}
}

def createPage(pageNum) {
	if ((state.numButton == pageNum) || (pageNum == 8))
		state.installCondition = true
	dynamicPage(name: "configureButton$pageNum", title: "Set up button $pageNum here",
			nextPage: "configureButton${pageNum+1}", install: state.installCondition, uninstall: configured(), getButtonSections(pageNum))
}

def configureButton1() {
	state.numButton = buttonDevice.currentState("numberOfButtons")?.longValue ?: 4
	log.debug "state variable numButton: ${state.numButton}"
	state.installCondition = false
	createPage(1)
}
def configureButton2() {
	createPage(2)
}

def configureButton3() {
	createPage(3)
}

def configureButton4() {
	createPage(4)
}

def configureButton5() {
	createPage(5)
}

def configureButton6() {
	createPage(6)
}

def configureButton7() {
	createPage(7)
}

def configureButton8() {
	createPage(8)
}

def getButtonSections(buttonNumber) {
	return {
		section("Lights to Toggle") {
			input "lights_${buttonNumber}_pushed", "capability.switch", title: "Pushed", multiple: true, required: false
			input "lights_${buttonNumber}_held", "capability.switch", title: "Held", multiple: true, required: false
		}
		section("Dimmers to Toggle") {
			input "lightsDT_${buttonNumber}_pushed", "capability.switchLevel", title: "Pushed", multiple: true, required: false
			input "lightsDTVal_${buttonNumber}_pushed", "number", title: "Dim Level", required: false, description: "0 to 99"
			input "lightsDT_${buttonNumber}_held", "capability.switchLevel", title: "Held", multiple: true, required: false
			input "lightsDTVal_${buttonNumber}_held", "number", title: "Dim Level", required: false, description: "0 to 99"
		}
		section("Lights to Turn On") {
			input "lightOn_${buttonNumber}_pushed", "capability.switch", title: "Pushed", multiple: true, required: false
			input "lightOn_${buttonNumber}_held", "capability.switch", title: "Held", multiple: true, required: false
		}
		section("Lights to Turn Off") {
			input "lightOff_${buttonNumber}_pushed", "capability.switch", title: "Pushed", multiple: true, required: false
			input "lightOff_${buttonNumber}_held", "capability.switch", title: "Held", multiple: true, required: false
		}
		section("Dimmers to DimLevel 1") {
			input "lightDim_${buttonNumber}_pushed", "capability.switchLevel", title: "Pushed", multiple: true, required: false
			input "lightVal_${buttonNumber}_pushed", "number", title: "DimLevel 1", multiple: false, required: false, description: "0 to 99"
			input "lightDim_${buttonNumber}_held", "capability.switchLevel", title: "Held", multiple: true, required: false
			input "lightVal_${buttonNumber}_held", "number", title: "DimLevel 1", multiple: false, required: false, description: "0 to 99"
		}
		section("Dimmers to DimLevel 2") {
			input "lightD2m_${buttonNumber}_pushed", "capability.switchLevel", title: "Pushed", multiple: true, required: false
			input "lightV2l_${buttonNumber}_pushed", "number", title: "DimLevel 2", multiple: false, required: false, description: "0 to 99"
			input "lightD2m_${buttonNumber}_held", "capability.switchLevel", title: "Held", multiple: true, required: false
			input "lightV2l_${buttonNumber}_held", "number", title: "DimLevel 2", multiple: false, required: false, description: "0 to 99"
		}
		section("Fan to Adjust - Low, Medium, High, Off") {
			input "fanAdjust_${buttonNumber}_pushed", "capability.switchLevel", title: "Pushed", multiple: false, required: false
			input "fanAdjust_${buttonNumber}_held", "capability.switchLevel", title: "Held", multiple: false, required: false
		}
		section("Shade to Adjust - Up, Down, or Stop") {
			input "shadeAdjust_${buttonNumber}_pushed", "capability.doorControl", title: "Pushed", multiple: false, required: false
			input "shadeAdjust_${buttonNumber}_held", "capability.doorControl", title: "Held", multiple: false, required: false
		}
		section("Locks") {
			input "locks_${buttonNumber}_pushed", "capability.lock", title: "Pushed", multiple: true, required: false
			input "locks_${buttonNumber}_held", "capability.lock", title: "Held", multiple: true, required: false
		}
		section("Sonos") {
			input "sonos_${buttonNumber}_pushed", "capability.musicPlayer", title: "Pushed", multiple: true, required: false
			input "sonos_${buttonNumber}_held", "capability.musicPlayer", title: "Held", multiple: true, required: false
		}
		section("Modes") {
			input "mode_${buttonNumber}_pushed", "mode", title: "Pushed", required: false
			input "mode_${buttonNumber}_held", "mode", title: "Held", required: false
		}
		def phrases = location.helloHome?.getPhrases()*.label
		if (phrases) {
			section("Hello Home Actions") {
				log.trace phrases
				input "phrase_${buttonNumber}_pushed", "enum", title: "Pushed", required: false, options: phrases
				input "phrase_${buttonNumber}_held", "enum", title: "Held", required: false, options: phrases
			}
		}
        section("Sirens") {
            input "sirens_${buttonNumber}_pushed","capability.alarm" ,title: "Pushed", multiple: true, required: false
            input "sirens_${buttonNumber}_held", "capability.alarm", title: "Held", multiple: true, required: false
        }

		section("Custom Message") {
			input "textMessage_${buttonNumber}_pushed", "text", title: "Pushed", required: false
			input "textMessage_${buttonNumber}_held", "text", title: "Held", required: false
		}

        section("Push Notifications") {
            input "notifications_${buttonNumber}_pushed","bool" ,title: "Pushed", required: false, defaultValue: false
            input "notifications_${buttonNumber}_held", "bool", title: "Held", required: false, defaultValue: false
        }

        section("Sms Notifications") {
            input "phone_${buttonNumber}_pushed","phone" ,title: "Pushed", required: false
            input "phone_${buttonNumber}_held", "phone", title: "Held", required: false
        }
	}
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(buttonDevice, "button", buttonEvent)
}

def configured() {
	return buttonDevice || buttonConfigured(1) || buttonConfigured(2) || buttonConfigured(3) || buttonConfigured(4)
}

def buttonConfigured(idx) {
	return settings["lights_$idx_pushed"] ||
    	settings["lightsDT_$idx_pushed"] ||
        settings["lightsDTVal_$idx_pushed"] ||
    	settings["lightOn_$idx_pushed"] ||
    	settings["lightOff_$idx_pushed"] ||
        settings["lightDim_$idx_pushed"] ||
        settings["lightVal_$idx_pushed"] ||
        settings["lightD2m_$idx_pushed"] ||
        settings["lightV2l_$idx_pushed"] ||
        settings["fanAdjust_$idx_pushed"] ||
        settings["shadeAdjust_$idx_pushed"] ||
		settings["locks_$idx_pushed"] ||
		settings["sonos_$idx_pushed"] ||
		settings["mode_$idx_pushed"] ||
        settings["notifications_$idx_pushed"] ||
        settings["sirens_$idx_pushed"] ||
        settings["notifications_$idx_pushed"]   ||
        settings["phone_$idx_pushed"]
}

def buttonEvent(evt){
	if(allOk) {
		def buttonNumber = evt.data // why doesn't jsonData work? always returning [:]
		def value = evt.value
		log.debug "buttonEvent: $evt.name = $evt.value ($evt.data)"
		log.debug "button: $buttonNumber, value: $value"

		def recentEvents = buttonDevice.eventsSince(new Date(now() - 3000)).findAll{it.value == evt.value && it.data == evt.data}
		log.debug "Found ${recentEvents.size()?:0} events in past 3 seconds"

		if(recentEvents.size <= 1){
			switch(buttonNumber) {
				case ~/.*1.*/:
					executeHandlers(1, value)
					break
				case ~/.*2.*/:
					executeHandlers(2, value)
					break
				case ~/.*3.*/:
					executeHandlers(3, value)
					break
				case ~/.*4.*/:
					executeHandlers(4, value)
					break
			}
		} else {
			log.debug "Found recent button press events for $buttonNumber with value $value"
		}
	}
}

def executeHandlers(buttonNumber, value) {
	log.debug "executeHandlers: $buttonNumber - $value"

	def lights = find('lights', buttonNumber, value)
	if (lights != null) toggle(lights)

	def lightsDT = find('lightsDT', buttonNumber, value)
	def dimTVal = find('lightsDTVal', buttonNumber, value)
	if (lightsDT != null) dimToggle(lightsDT, dimTVal)

	def lights1 = find('lightOn', buttonNumber, value)
	if (lights1 != null) turnOn(lights1)

	def lights2 = find('lightOff', buttonNumber, value)
	if (lights2 != null) turnOff(lights2)

	def lights3 = find('lightDim', buttonNumber, value)
	def dimval3 = find('lightVal', buttonNumber, value)
	if (lights3 != null) turnDim(lights3,dimval3)

	def lights4 = find('lightD2m', buttonNumber, value)
	def dimval4 = find('lightV2l', buttonNumber, value)
	if (lights4 != null) turnDim(lights4,dimval4)

	def fan = find('fanAdjust', buttonNumber, value)
	if (fan != null) adjustFan(fan)
    
	def shade = find('shadesAdjust', buttonNumber, value)
	if (shade != null) adjustShade(shade)

	def locks = find('locks', buttonNumber, value)
	if (locks != null) toggle(locks)

	def sonos = find('sonos', buttonNumber, value)
	if (sonos != null) toggle(sonos)

	def mode = find('mode', buttonNumber, value)
	if (mode != null) changeMode(mode)

	def phrase = find('phrase', buttonNumber, value)
	if (phrase != null) location.helloHome.execute(phrase)

	def textMessage = findMsg('textMessage', buttonNumber, value)

	def notifications = find('notifications', buttonNumber, value)
	if (notifications?.toBoolean()) sendPush(textMessage ?: "Button $buttonNumber was pressed" )

	def phone = find('phone', buttonNumber, value)
	if (phone != null) sendSms(phone, textMessage ?:"Button $buttonNumber was pressed")

    def sirens = find('sirens', buttonNumber, value)
    if (sirens != null) toggle(sirens)
}

def find(type, buttonNumber, value) {
	def preferenceName = type + "_" + buttonNumber + "_" + value
	def pref = settings[preferenceName]
	if(pref != null) {
		log.debug "Found: $pref for $preferenceName"
	}

	return pref
}

def findMsg(type, buttonNumber, value) {
	def preferenceName = type + "_" + buttonNumber + "_" + value
	def pref = settings[preferenceName]
	if(pref != null) {
		log.debug "Found: $pref for $preferenceName"
	}

	return pref
}

def turnOn(devices) {
	log.debug "turnOn: $devices = ${devices*.currentSwitch}"

	devices.on()
}

def turnOff(devices) {
	log.debug "turnOff: $devices = ${devices*.currentSwitch}"

	devices.off()
}

def turnDim(devices, level) {
	log.debug "turnDim: $devices = ${devices*.currentSwitch}"

	devices.setLevel(level)
}

def adjustFan(device) {
	log.debug "adjust: $device = ${device.currentLevel}"
    
	def currentLevel = device.currentLevel

	if(device.currentSwitch == 'off') device.setLevel(15)
	else if (currentLevel < 34) device.setLevel(50)
  	else if (currentLevel < 67) device.setLevel(90)
	else device.off()
}

def adjustShade(device) {
	log.debug "shades: $device = ${device.currentMotor} state.lastUP = $state.lastshadesUp"

	if(device.currentMotor in ["up","down"]) {
    	state.lastshadesUp = device.currentMotor == "up"
    	device.stop()
    } else {
    	state.lastshadesUp ? device.down() : device.up()
//    	if(state.lastshadesUp) device.down()
//        else device.up()
        state.lastshadesUp = !state.lastshadesUp
    }
}

def toggle(devices) {
	log.debug "toggle: $devices = ${devices*.currentValue('switch')}"

	if (devices*.currentValue('switch').contains('on')) {
		devices.off()
	}
	else if (devices*.currentValue('switch').contains('off')) {
		devices.on()
	}
	else if (devices*.currentValue('lock').contains('locked')) {
		devices.unlock()
	}
	else if (devices*.currentValue('lock').contains('unlocked')) {
		devices.lock()
	}
	else if (devices*.currentValue('alarm').contains('off')) {
        devices.siren()
    }
	else {
		devices.on()
	}
}

def dimToggle(devices, dimLevel) {
	log.debug "dimToggle: $devices = ${devices*.currentValue('switch')}"

	if (devices*.currentValue('switch').contains('on')) devices.off()
	else devices.setLevel(dimLevel)
}

def changeMode(mode) {
	log.debug "changeMode: $mode, location.mode = $location.mode, location.modes = $location.modes"

	if (location.mode != mode && location.modes?.find { it.name == mode }) {
		setLocationMode(mode)
	}
}

// execution filter methods
private getAllOk() {
	modeOk && daysOk && timeOk
}

private getModeOk() {
	def result = !modes || modes.contains(location.mode)
	log.trace "modeOk = $result"
	result
}

private getDaysOk() {
	def result = true
	if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	log.trace "daysOk = $result"
	result
}

private getTimeOk() {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting, location.timeZone).time
		def stop = timeToday(ending, location.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	log.trace "timeOk = $result"
	result
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private hideOptionsSection() {
	(starting || ending || days || modes) ? false : true
}

private timeIntervalLabel() {
	(starting && ending) ? hhmm(starting) + "-" + hhmm(ending, "h:mm a z") : ""
}
