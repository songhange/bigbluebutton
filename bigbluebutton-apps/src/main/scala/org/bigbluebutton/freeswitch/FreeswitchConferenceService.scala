package org.bigbluebutton.freeswitch

import org.bigbluebutton.webconference.voice.IVoiceConferenceService
import org.bigbluebutton.core.api._
import org.bigbluebutton.webconference.voice.FreeswitchConferenceEventListener

class FreeswitchConferenceService(fsproxy: FreeswitchManagerProxy, 
                             fsListener: FreeswitchConferenceEventListener) 
                             extends IVoiceConferenceService
                             with OutMessageListener2 {

  fsListener.setVoiceConferenceService(this)
  
  var bbbInGW: IBigBlueButtonInGW = _
  var fsActor: FreeswitchConferenceActor = _
  
  def setIBigBlueButtonInGW(inGW: IBigBlueButtonInGW) {
      bbbInGW = inGW
      fsActor = new FreeswitchConferenceActor(fsproxy, bbbInGW)
      fsActor.start
  }
  
  def handleMessage(msg: IOutMessage) {
	  msg match {
	    case msg: MeetingCreated => 
	                  handleMeetingCreated(msg)
	    case msg: UserJoined => 
	                  handleUserJoined(msg)
	    case msg: UserLeft => 
	                  handleUserLeft(msg)
	    case msg: MuteVoiceUser =>
	                  handleMuteVoiceUser(msg)
	    case msg: EjectVoiceUser =>
	                  handleEjectVoiceUser(msg)
	    case _ => // do nothing
	  }
  }
  
  private def handleMuteVoiceUser(msg: MuteVoiceUser) {
    fsActor ! msg
  }
  
  private def handleEjectVoiceUser(msg: EjectVoiceUser) {
    fsActor ! msg
  }
  
  private def handleUserLeft(msg: UserLeft) {
    fsActor ! msg
  }
  
  private def handleUserJoined(msg: UserJoined) {
    fsActor ! msg
  }
  
  private def handleMeetingCreated(msg: MeetingCreated) {
    fsActor ! msg
  }
  
  
  def voiceUserJoined(userId: String, webUserId: String, conference: String, 
			          callerIdNum: String, callerIdName: String,
			          muted: java.lang.Boolean, talking: java.lang.Boolean) {
    println("******** FreeswitchConferenceService received voiceUserJoined vui=[" + userId + "] wui=[" + webUserId + "]")
    val vuj = new FsVoiceUserJoined(userId, webUserId, 
                             conference, callerIdNum, 
                             callerIdName, muted, 
                             talking)
    fsActor ! vuj
  }
  
  def voiceUserLeft(conference: String, userId: String) {
    val vul = new FsVoiceUserLeft(userId, conference)
    fsActor ! vul
  }
  
  def voiceUserLocked(conference: String, userId: String, locked: java.lang.Boolean) {
    val vul = new FsVoiceUserLocked(userId, conference, locked)
    fsActor ! vul    
  }
  
  def voiceUserMuted(conference: String, userId: String, muted: java.lang.Boolean) {
    println("******** FreeswitchConferenceService received voiceUserMuted vui=[" + userId + "] muted=[" + muted + "]")
    val vum = new FsVoiceUserMuted(userId, conference, muted)
    fsActor ! vum   
  }
  
  def voiceUserTalking(conference: String, userId: String, talking: java.lang.Boolean) {
    println("******** FreeswitchConferenceService received voiceUserTalking vui=[" + userId + "] muted=[" + talking + "]")
     val vut = new FsVoiceUserTalking(userId, conference, talking)
    fsActor ! vut   
  }
}