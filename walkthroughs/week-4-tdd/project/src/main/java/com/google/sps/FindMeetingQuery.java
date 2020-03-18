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


    //Times that don't work for all attendees
    HashSet attendeeSchedule = new HashSet<TimeRange>();


 
     

    //find times attendees are busy
    for(int i = 0 ; i < numberOfAttendees ; i++){
        String currentAttendee = attendees.get(i);

        for(Event event : events){
            if(isAnAttendee(currentAttendee,event)){//checks if current attendant goes to this event
                addToSchedule(attendeeSchedule, event);//adds event to current attendee schedule
            }
        }
    }

    
    //ignores people not attending
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

        trimContainedNodes(busySchedule);
        System.out.println("After trimming containing nodes");
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
                System.out.println("loop 0");
                TimeRange possibleTime = TimeRange.fromStartEnd(dayStart,start,false);
                
                
            
                //if(k+1<=busySchedule.size()){
                //     if(busySchedule.get(k+1).start() < start){
                //         TimeRange anotherPossibleTime = TimeRange.fromStartEnd(dayStart,busySchedule.get(k+1).start(),false);

                //         if(anotherPossibleTime.duration() >= meetingDuration){
                //         possibleTimes.add(anotherPossibleTime);
                //         System.out.println("added 1");
                        
                //     }
                // }
                // }else{
                        if(possibleTime.duration() >= meetingDuration){
                        possibleTimes.add(possibleTime);
                        System.out.println("added");
                        
                    }
                //}
                
            }

            if(k == busySchedule.size()-1 && busySchedule.size()!=0){
                System.out.println("loop 1");
                TimeRange possibleTime = TimeRange.fromStartEnd(end,dayEnd,true);

                if(k!=0){
                    int possibleStartTime = Math.max(end,possibleTime.end());
                    TimeRange anotherPossibleTime = TimeRange.fromStartEnd(end,dayEnd,true);
                    System.out.println("Another possible time: "+anotherPossibleTime);
                    if(anotherPossibleTime.overlaps(possibleTime)){
                        if(anotherPossibleTime.duration() >= meetingDuration){
                            possibleTimes.add(anotherPossibleTime);
                            System.out.println("added case 1");
                            
                        }

                    }else if(anotherPossibleTime.contains(possibleTime)){
                        System.out.println("contains");
                    }
                    
                }else{
                    if(possibleTime.duration() >= meetingDuration){
                        possibleTimes.add(possibleTime); 
                        System.out.println("added case 2");
                    }
                }

               
                
                

                


                
            }

            if(k+1<busySchedule.size()){
                System.out.println("loop 2");
                TimeRange possibleTime = TimeRange.fromStartEnd(end,busySchedule.get(k+1).start(),false);
                // if((k-1)!<0){
                //     TimeRange anotherPossibleTime = TimeRange.fromStartEnd(dayStart,busySchedule.get(k-1).end(),false);
                //     if(anotherePossibleTime.duration() >= meetingDuration){
                //         if(anotherPossibleTime.contains(possibleTime)){
                //             possibleTimes.add(anotherPossibleTime);
                //         }
                //     }
                // }else{
                    if(possibleTime.duration() >= meetingDuration){
                    possibleTimes.add(possibleTime);

                    System.out.println("added");
                    
                }
                // }

                
                

            }
            






        }
        


    
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

   public void trimContainedNodes(List<TimeRange>listThatMayContainNestedEvents){
       for(int i =0 ; i < listThatMayContainNestedEvents.size() ; i++){//iterating through the list and removing at the same time. (mark)
           for(int j =0 ; j< listThatMayContainNestedEvents.size(); j++){//skip check for same one
                if(i==j){
                    continue;
                }else if(listThatMayContainNestedEvents.get(i).contains(listThatMayContainNestedEvents.get(j))){
                   listThatMayContainNestedEvents.remove(j);
               }
           }
       }
   }

    

}
