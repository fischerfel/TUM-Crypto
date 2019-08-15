package com.belzabar.ocap.controllers;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.belzabar.ocap.mailer.MailMail;
import com.belzabar.ocap.mappers.AdminMapper;
import com.belzabar.ocap.models.AssessorCandidateEvaluation;
import com.belzabar.ocap.models.Candidates;
import com.belzabar.ocap.models.College;
import com.belzabar.ocap.models.EventandQuestionjoin;
import com.belzabar.ocap.models.Events;
import com.belzabar.ocap.models.Requests;
import com.belzabar.ocap.models.Questions;
import com.belzabar.ocap.models.Users;
import com.belzabar.ocap.models.EvaluationRequests;
import com.belzabar.ocap.resultmap.EvaluationRequestsMap;
import com.belzabar.ocap.resultmap.OngoingEventsMap;
import com.belzabar.ocap.resultmap.UserAssignMap;


import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * The following class controls all the url mappings related to Organizer
 * Interfaces
 */


@Controller
@RequestMapping("/api/admin")
public class AdminController {
    String password;

    public static String generateRandomString(int length) throws Exception {

        StringBuffer buffer = new StringBuffer();
        String characters = "";
        characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();

    }

    @Autowired
    private JavaMailSender mailSender;
    @Inject
    private AdminMapper eventMapper;



    /**
     * This method is used to return the list of colleges and problems when the
     * organizer is creating an event and is called immediately when the create
     * event interface opens
     * 
     * @return Map This returns a map with mappings to college list and problem
     *         list
     */

    @RequestMapping(value = "/event/fetchData", method = RequestMethod.GET)
    public @ResponseBody Map getShopInJSON1() {
        System.out.println("hi");
        List<College> l1 = this.eventMapper.getCollegeData();
        List<Questions> l2 = this.eventMapper.getEventQuestions();
        Map map = new HashMap();
        map.put("collegelist", l1);
        map.put("questionlist", l2);
        return map;
    }

    /**
     * This method is used to return the list of candidates based on the event
     * id and their evaluation details
     * 
     * @param int This is the event id received based on the event name clicked
     * @return List This returns a list of candidates related to that event and
     *         their evaluation details
     */

    @RequestMapping( value="/candidate_evaluation",method = RequestMethod.GET)
    public @ResponseBody List<AssessorCandidateEvaluation> getEval(@RequestParam(value="eid", required=false) Integer eid)
    {


        //return u;
        System.out.println(eid);
        System.out.println(this.eventMapper.getCandidate_evaluation(eid)+"ewfr");
        return this.eventMapper.getCandidate_evaluation(eid);
        //System.out.println("king2");




    //  return shop;

    }



    /**
     * This method is used to return the list of ongoing events whenever the
     * corresponding interface is opened
     * 
     * @return List This returns a list containing event name,college name,start
     *         time and end time of ongoing events
     */

    @RequestMapping(value = "/event/fetchOngoingEventsData", method = RequestMethod.GET)
    public @ResponseBody List<OngoingEventsMap> getShopInJSON2() {

        List<OngoingEventsMap> l1= this.eventMapper.getOngoingEventsData();String s="";
        List<OngoingEventsMap> l2= new Vector<OngoingEventsMap>();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for(int i=0;i<l1.size();i++){
            OngoingEventsMap ob=l1.get(i);
            s=df.format(ob.getStart_time());
            ob.setStart(s);
            s=df.format(ob.getEnd_time());
            ob.setEnd(s);
            l2.add(ob);
        }
        return l2;

    }

    /**
     * This method is used to return the list of scheduled events which have not
     * started yet whenever the corresponding interface is opened
     * 
     * @return List This returns a list containing event name,college name,start
     *         time and end time of scheduled events
     */

    @RequestMapping(value = "/event/fetchScheduledEventsData", method = RequestMethod.GET)
    public @ResponseBody List<OngoingEventsMap> getShopInJSON3() {
        List<OngoingEventsMap> l1= this.eventMapper.getScheduledEventsData();
        String s="";
        List<OngoingEventsMap> l2= new Vector<OngoingEventsMap>();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for(int i=0;i<l1.size();i++){
            OngoingEventsMap ob=l1.get(i);
            s=df.format(ob.getStart_time());
            ob.setStart(s);
            s=df.format(ob.getEnd_time());
            ob.setEnd(s);
            l2.add(ob);
        }
        return l2;

        //return this.eventMapper.getScheduledEventsData();
    }

    /**
     * This method is used to return the list of users for assignment as problem
     * setter and reviewer as soon as the problem interface is opened.
     * 
     * @return List This returns a list containing user id and user name of the
     *         users
     */

    @RequestMapping(value = "/event/fetchUserData", method = RequestMethod.GET)
    public @ResponseBody List<Users> getShopInJSON4() {
        return this.eventMapper.getUserData();
    }


    /**
     * This method is used to return a list of candidates based on the event id
     * and a list containing user details to assign sets of candidates to users
     * for evaluation of code
     * 
     * @param int This is the event id received based on the event name clicked
     * @return Map This returns a mapping to candidate email list and user
     *         details list.
     */

    @RequestMapping(value="event/fetchAssessCandidateData", method = RequestMethod.GET)
    public @ResponseBody Map getShopInJSON5(@RequestParam(value="event_id") int event_id)
    {
        List<Integer> l1=this.eventMapper.getAssessCandidateId(event_id);
        List<Candidates> l2= new Vector<Candidates>();
        for(int i=0;i<l1.size();i++)
        {
            Candidates ob=this.eventMapper.getAssessCandidateData(l1.get(i));
            l2.add(ob);
        }
        List<Users> l3=this.eventMapper.getUserData();
        Map map = new HashMap();
        map.put("candidateemail",l2);
        map.put("userlist", l3);
        return map;
    }


    /**
     * This method is used to return the list of completed events along with
     * their status depending on whether they have been assigned assessors yet.
     * 
     * @return List This returns a list containing event name,college name,start
     *         time, end time and status of completed events
     */

    @RequestMapping(value = "/event/fetchCompletedEventsData", method = RequestMethod.GET)
    public @ResponseBody List<OngoingEventsMap> getShopInJSON6() {
        List<OngoingEventsMap> l1 = this.eventMapper.getCompletedEventsData();
        List<EvaluationRequests> l2 = this.eventMapper
                .getEvaluationRequestData();
        Map<Integer, OngoingEventsMap> map = new HashMap();
        boolean unassgn;
        int uneval, sub;
        for (int i = 0; i < l1.size(); i++) {
            OngoingEventsMap ob1 = l1.get(i);
            uneval = 0;
            sub = 0;unassgn=false;
            for (int j = 0; j < l2.size(); j++) {
                EvaluationRequests ob2 = l2.get(j);
                if (ob1.getEvent_id() == ob2.getEvent_id()) {
                    unassgn = true;
                    if (ob2.getStatus().equals("NEW"))
                        uneval++;
                }
            }
            if (unassgn == false)
                ob1.setCurrent_status("Unassigned");
            else {
                if (uneval > 0)
                    ob1.setCurrent_status("Unevaluated");
                else
                    ob1.setCurrent_status("Evaluated");
            }

        }

        String s="";
        List<OngoingEventsMap> l= new Vector<OngoingEventsMap>();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for(int i=0;i<l1.size();i++){
            OngoingEventsMap ob=l1.get(i);
            s=df.format(ob.getStart_time());
            ob.setStart(s);
            s=df.format(ob.getEnd_time());
            ob.setEnd(s);
            l.add(ob);
        }
        return l;

    }

    /**
     * This method is used to create event on form submission
     * 
     * @param List
     *            This is a list containing event details.The number of entries
     *            depend on the number of problems selected.
     * @return String This returns a success or failure message depending on
     *         whether the event is created or not
     */

    @SuppressWarnings("deprecation")
    @RequestMapping(value="/event/create", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getShopInJSON(@RequestBody @Valid List<EventandQuestionjoin> ev,BindingResult result) throws Exception
    {
        String s="";
        if(result.hasErrors()){
        for (Object object : result.getAllErrors()) {
            if(object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                s=fieldError.getField()+":"+fieldError.getDefaultMessage()+"\br";

            }

        }
        return s;
        }
            this.eventMapper.setEvent(ev.get(0).getCollege_id(),ev.get(0).getCreated_by(),ev.get(0).getStart_time(),ev.get(0).getEnd_time(),ev.get(0).getEvent_name());
            int event_id=this.eventMapper.getEventId();
            int college_id=ev.get(0).getCollege_id();
            for(int i=0;i<ev.size();i++){
         this.eventMapper.setQuestionId(event_id,ev.get(i).getQuestion_id());
            }
         String ids=ev.get(0).getCandidate_ids();
         System.out.println(ids);
         String[] idArray =  ids.split(",");
         List<String> id=this.eventMapper.checkCandidate();
         int candidateId[]=new int[idArray.length];
         for(int i=0;i<idArray.length;i++)
         { 
            password =generateRandomString(10);




            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;

            }
            System.out.println(hashtext);





            /* if(this.eventMapper.checkCandidate(idArray[i])==false)*/
             MailMail leaveEmail=new MailMail();
             //set the mailSender to the MailMail class
             leaveEmail.setMailSender(mailSender);
             //boolean statusEmail=leaveEmail.sendMail(leaveapplyform);
            //MailMail mm = new MailMail();
             System.out.println(password);
             leaveEmail.sendMail("belzabar.coding@gmail.com",
                  idArray[i],
                   "Event Details", 
                   "Your password is " + password);
             System.out.println(idArray[i]);

             password=hashtext;

             if(id.contains(idArray[i]));
             else
             {
                /* MailMail leaveEmail=new MailMail();
                 //set the mailSender to the MailMail class
                 leaveEmail.setMailSender(mailSender);
                 //boolean statusEmail=leaveEmail.sendMail(leaveapplyform);
                //MailMail mm = new MailMail();
                 leaveEmail.sendMail("004bpr@gmail.com",
                      idArray[i],
                       "Testing123", 
                       "Testing only \n\n Hello Spring Email Sender");
                 System.out.println(idArray[i]);*/
                 this.eventMapper.insertCandidateId(idArray[i],college_id);
             }
             candidateId[i]=this.eventMapper.getEventCandidateId(idArray[i]);
             this.eventMapper.insertEventCandidateId(event_id,candidateId[i],password);

         }

         /*for(int i=0;i<idArray.length;i++)
         {
             candidateId[i]=this.eventMapper.getEventCandidateId(idArray[i]);
         }
         for(int i=0;i<idArray.length;i++)
         {
            System.out.println(candidateId[i]);

            this.eventMapper.insertEventCandidateId(event_id,candidateId[i],password);
         }*/
         return "Event Successfully Created!!";
    }

    /**
     * This method is used to assign problem setter and reviewer
     * 
     * @param Object
     *            This is a object containing problem setter id,reviewer id,
     *            number of problems and deadline
     * @return String This returns a success or failure message depending on
     *         whether the users are assigned or not.
     */

    @RequestMapping(value="/problem", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getShopInJSON(@RequestBody @Valid UserAssignMap ev,BindingResult result)
    {
         java.util.Date utilDate = new java.util.Date();
         System.out.println(utilDate.toString());
         //java.sql.Date utilDate = new Date(year, month, day)
         System.out.println(utilDate);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            String s="";
            if(result.hasErrors()){
                for (Object object : result.getAllErrors()) {
                    if(object instanceof FieldError) {
                        FieldError fieldError = (FieldError) object;

                       /* System.out.println(fieldError.getCode());*/
                        s=fieldError.getField()+":"+fieldError.getDefaultMessage()+"\br";

                    }
                }
                return s;
            }
            System.out.println(ev.getAssignment_name());
         this.eventMapper.setRequest(ev.getAssignment_name(), ev.getDescription(),ev.getCreated_by(),ev.getDeadline(),
         ev.getNumber_of_questions());
     int request_id=this.eventMapper.getRequestId();    
         this.eventMapper.setRole(request_id,ev.getSetter_id(),"SETTER");
         if(!(ev.isNoreviewer()))
         {this.eventMapper.setRole(request_id,ev.getReviewer_id(),"REVIEWER");}
         return "Successfully Assigned";
    }

    /**
     * This method is used to assign problem assessor
     * 
     * @param List
     *            This is a list containing candidate id(s) and the corresponding assessor id(s)
     *
     * @return String This returns a success or failure message depending on
     *         whether the assessors are assigned or not.
     */

    @RequestMapping(value="/event/AssignAssessor", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getShopInJSON(@RequestBody List<EvaluationRequestsMap> ev){
        for(int i=0;i<ev.size();i++){
            EvaluationRequestsMap ob=ev.get(i);
            int candidate_id=this.eventMapper.getEventCandidateId(ob.getCandidate_email());
            System.out.println(candidate_id);
            this.eventMapper.setAssessor(ob.getEvent_id(),candidate_id,ob.getAssigned_to());
        }
        return "Assessors Successfully Assigned!!";
      }
    }
