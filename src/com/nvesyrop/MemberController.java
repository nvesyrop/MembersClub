package com.nvesyrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MemberController {

	private List<Member> members;
	private MemberDbUtil memberDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public MemberController() throws Exception {
		members = new ArrayList<>();
		
		memberDbUtil = MemberDbUtil.getInstance(); 
	}
	
	public List<Member> getMembers() {
		return members;
	}

	public void loadMembers() {

		logger.info("Loading members");
		
		members.clear();

		try {
			
			// get all members from database
			members = memberDbUtil.getMembers();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading members", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
		
	public String addMember(Member theMember) {

		logger.info("Adding member: " + theMember);

		try {
			
			// add member to the database
			memberDbUtil.addMember(theMember);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding member", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "list-members?faces-redirect=true";
	}

	public String loadMember(int memberId) {
		
		logger.info("loading members: " + memberId);
		
		try {
			// get member from database
			Member theMember = memberDbUtil.getMember(memberId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("member", theMember);	
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading member id:" + memberId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-member-form.xhtml";
	}	
	
	public String updateMember(Member theMember) {

		logger.info("updating member: " + theMember);
		
		try {
			
			// update member in the database
			memberDbUtil.updateMember(theMember);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating member: " + theMember, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-member?faces-redirect=true";		
	}
	
	public String deleteMember(int memberId) {

		logger.info("Deleting member id: " + memberId);
		
		try {

			// delete the member from the database
			memberDbUtil.deleteMember(memberId);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error deleting member id: " + memberId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-member";	
	}	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
