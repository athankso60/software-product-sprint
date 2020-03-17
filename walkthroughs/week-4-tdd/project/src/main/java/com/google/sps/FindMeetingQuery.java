// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*; 

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //throw new UnsupportedOperationException("TODO: Implement this method.");

    List<TimeRange> possibleTimes = new ArrayList<TimeRange>();

    //define variables
    int dayStart = TimeRange.START_OF_DAY;
    int dayEnd = TimeRange.END_OF_DAY;
    int numberOfAttendees = request.getAttendees().size();
    int numberOfEvents = events.size();
    long meetingDuration = request.getDuration();

    //optionsForNoAttendees
    if(request.getAttendees().isEmpty())
        return Arrays.asList(TimeRange.WHOLE_DAY);

    //noOptionsForTooLongOfARequest
    if(request.getDuration() > TimeRange.WHOLE_DAY.duration())
        return Arrays.asList();

    //add attendees to attendees array
    // String[] attendees = (String[])request.getAttendees().toArray();
    List<String> attendees = new ArrayList<String>();
    attendees.addAll(request.getAttendees());

    //find feasible times for primary attendee
    String primaryAttendee = attendees.get(0);
    // System.out.println("Primary Attendee: "+primaryAttendee);

    //primary attendee schedule
    HashSet primaryAttendeeSchedule = new HashSet<TimeRange>();


    //find when primary attendee is busy in order to tell when they're not
    for(Event event : events){
        if(isAnAttendee(primaryAttendee,event)){//checks if primary attendant goes to this event
            addToSchedule(primaryAttendeeSchedule, event);//adds event primary attendee schedule
        }
    }

    //find feasible durations for primary attendee
    HashSet feasibleTimesForPrimaryAttendee = new HashSet<TimeRange>();

    for(int i = TimeRange.START_OF_DAY ; i < TimeRange.END_OF_DAY ; i+=(int) (request.getDuration())){
        TimeRange currentMeetingTime = TimeRange.fromStartDuration(i,(int) (request.getDuration()));
        //Test
        // System.out.println("CurrentMeetingTime: ");
        // System.out.println("Start: "+currentMeetingTime.start() + "End: "+ currentMeetingTime.end());
        if(!primaryAttendeeSchedule.contains(currentMeetingTime)&&!checkIfOverlap(currentMeetingTime,primaryAttendeeSchedule)&&!containing(currentMeetingTime,primaryAttendeeSchedule))
                        feasibleTimesForPrimaryAttendee.add(currentMeetingTime);
    }

    if(numberOfAttendees ==1){
            List<TimeRange> list = new ArrayList<TimeRange>();
            Iterator<TimeRange> myIt = feasibleTimesForPrimaryAttendee.iterator(); 
            while (myIt.hasNext()){
                list.add(myIt.next());
                
            }

            Collections.sort(list, TimeRange.ORDER_BY_END);

            return list;
    }
 
     

    //check if primaryAttendee meeting times work for all other attendees.
    for(int i = 1 ; i < numberOfAttendees ; i++){
        String currentAttendee = attendees.get(i);
        // System.out.println("Attendee "+i+ "  "+currentAttendee);

        //check if current attendee times works for primary attendee
        HashSet currentAttendeeSchedule = new HashSet<TimeRange>();

        for(Event event : events){
            if(isAnAttendee(currentAttendee,event)){//checks if current attendant goes to this event
                addToSchedule(currentAttendeeSchedule, event);//adds event to current attendee schedule
            }
        }

        //find feasible times for current attendee
        //find feasible durations for primary attendee
        HashSet feasibleTimesForCurrentAttendee = new HashSet<TimeRange>();

        for(int j = TimeRange.START_OF_DAY ; j < TimeRange.END_OF_DAY ; j+=(int) (request.getDuration())){


            TimeRange currentMeetingTime = TimeRange.fromStartDuration(j,(int) (request.getDuration()));   
            if(!currentAttendeeSchedule.contains(currentMeetingTime)&&!checkIfOverlap(currentMeetingTime,currentAttendeeSchedule)&&!containing(currentMeetingTime,currentAttendeeSchedule))
                            feasibleTimesForCurrentAttendee.add(currentMeetingTime);


        }

        
        //for each loop.
        Iterator<TimeRange> it = feasibleTimesForCurrentAttendee.iterator(); 
        while (it.hasNext()){
            TimeRange currentTime = it.next();
            if(feasibleTimesForPrimaryAttendee.contains(currentTime)){
                possibleTimes.add(currentTime);
            }
            
        }

        //all possible times and remove conflicts
        
        

        
    }

    

    
    
    
    //edge case
    return possibleTimes;
  }

    //check if person is attendee of event
   public boolean isAnAttendee(String attendee, Event event){
       if (event.getAttendees().contains(attendee)){
            return true;
       }        
       else{
            return false;
       }
                
   }
    //adds event to an attendee schedule. 
   public void addToSchedule(HashSet<TimeRange> attendeeSchedule, Event event){
       attendeeSchedule.add(event.getWhen());
   }
    //check if there is an overlap between a meeting time and an attendee schedule
   public boolean checkIfOverlap(TimeRange currentMeetingTime,HashSet<TimeRange> attendeeSchedule){

     Iterator<TimeRange> i = attendeeSchedule.iterator(); 
        while (i.hasNext()){
            if(i.next().overlaps(currentMeetingTime)){
                return true;
            }
            
        }

        return false;
            
   }

    //check if one event contains the other
    public boolean containing(TimeRange meetingTime, HashSet<TimeRange> attendeeSchedule){
       
                
                        Iterator<TimeRange> i = attendeeSchedule.iterator(); 
                            while (i.hasNext()){
                                TimeRange currentRange = i.next();
                                if(currentRange.contains(meetingTime) ||meetingTime.contains(currentRange)){
                                    return true;
                                }      
                        }

                    return false;
            }

}
