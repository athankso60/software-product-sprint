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

    //ignoresPeopleNotAttending
    

    //add attendees to attendees array
    // String[] attendees = (String[])request.getAttendees().toArray();
    List<String> attendees = new ArrayList<String>();
    attendees.addAll(request.getAttendees());

    //find feasible times for primary attendee
    // String primaryAttendee = attendees.get(0);
    // System.out.println("Primary Attendee: "+primaryAttendee);

    //Times that don't work for all attendees
    HashSet attendeeSchedule = new HashSet<TimeRange>();


    //find when primary attendee is busy in order to tell when they're not
    // for(Event event : events){
    //     if(isAnAttendee(primaryAttendee,event)){//checks if primary attendant goes to this event
    //         addToSchedule(primaryAttendeeSchedule, event);//adds event primary attendee schedule
    //     }
    // }

    //find feasible durations for primary attendee
    // HashSet feasibleTimesForPrimaryAttendee = new HashSet<TimeRange>();

    // for(int i = TimeRange.START_OF_DAY ; i < TimeRange.END_OF_DAY ; i+=(int) (request.getDuration())){
    //     TimeRange currentMeetingTime = TimeRange.fromStartDuration(i,(int) (request.getDuration()));
    //     //Test
        
    //     if(!primaryAttendeeSchedule.contains(currentMeetingTime)&&!checkIfOverlap(currentMeetingTime,primaryAttendeeSchedule)&&!containing(currentMeetingTime,primaryAttendeeSchedule))
    //                     feasibleTimesForPrimaryAttendee.add(currentMeetingTime);
    // }

    // if(numberOfAttendees ==1){
    //         List<TimeRange> list = new ArrayList<TimeRange>();
    //         Iterator<TimeRange> myIt = feasibleTimesForPrimaryAttendee.iterator(); 
    //         while (myIt.hasNext()){
    //             list.add(myIt.next());
                
    //         }

    //         Collections.sort(list, TimeRange.ORDER_BY_END);

    //         return list;
    // }
 
     

    //find times attendees are busy
    for(int i = 0 ; i < numberOfAttendees ; i++){
        String currentAttendee = attendees.get(i);
        // System.out.println("Attendee "+i+ "  "+currentAttendee);

        //check if current attendee times works for primary attendee
        // HashSet currentAttendeeSchedule = new HashSet<TimeRange>();

        for(Event event : events){
            if(isAnAttendee(currentAttendee,event)){//checks if current attendant goes to this event
                addToSchedule(attendeeSchedule, event);//adds event to current attendee schedule
            }
        }
    }

    if(attendeeSchedule.size()==0){
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

        


        List<TimeRange> busySchedule = new ArrayList<TimeRange>();
            Iterator<TimeRange> myIt = attendeeSchedule.iterator(); 
            while (myIt.hasNext()){
                busySchedule.add(myIt.next());
                
            }

        //sort times that don't work
        Collections.sort(busySchedule, TimeRange.ORDER_BY_END);


        //print busySchedule
        for(int j= 0; j<busySchedule.size() ; j++){
            System.out.println(busySchedule.get(j));
        }

        //test
        for(int k=0 ; k < busySchedule.size(); k++){
            
            //System.out.println("Timerange: "+busySchedule.get(k));
            TimeRange currentTimeRange = busySchedule.get(k);
            int start = currentTimeRange.start();
            int end = currentTimeRange.end();
            int duration = currentTimeRange.duration();

            if(k == 0 && busySchedule.size()!=0){

                TimeRange possibleTime = TimeRange.fromStartEnd(dayStart,start,false);
                
                if(possibleTime.duration() >= meetingDuration){
                    possibleTimes.add(possibleTime);
                    
                }
                
            }

            if(k == busySchedule.size()-1 && busySchedule.size()!=0){
                
                TimeRange possibleTime = TimeRange.fromStartEnd(end,dayEnd,true);

                if(k!=0){
                    TimeRange anotherPossibleTime = TimeRange.fromStartEnd(end,dayEnd,true);
                    if(anotherPossibleTime.contains(possibleTime)||anotherPossibleTime.overlaps(possibleTime)){
                        if(anotherPossibleTime.duration() >= meetingDuration){
                            possibleTimes.add(possibleTime);
                            
                        }

                    }
                    
                }else{
                    if(possibleTime.duration() >= meetingDuration){
                        possibleTimes.add(possibleTime); 
                    }
                }

               
                
                

                


                
            }

            if(k+1<busySchedule.size()){
                
                TimeRange possibleTime = TimeRange.fromStartEnd(end,busySchedule.get(k+1).start(),false);
                
                if(possibleTime.duration() >= meetingDuration){
                    possibleTimes.add(possibleTime);
                    
                }
                

            }
            






        }
        System.out.println("done");






        

        
    

    
    
    //edge case
     return possibleTimes;

    //return Arrays.asList();
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
