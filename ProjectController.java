/** Controller for Tasky Projects Page. */
global class ProjectController{

    /** Gets all Collaborators associated with the User. */
    @RemoteAction
    global static Tasky_Collaborator__c[] getUserCollaborators() {
        Tasky_Collaborator__c[] collaborators = [SELECT Name, Project__c, Project__r.Name, Project__r.Description__c FROM Tasky_Collaborator__c WHERE User__c =: UserInfo.getUserId()];
        return collaborators;
    }

    /** Gets all Tasks associated with the Project with Id PROJECTID. */
    @RemoteAction
    global static Tasky_Task__c[] getTasks(Id projectId) {
        Tasky_Task__c[] tasks = [SELECT Name, Assignee__c, Deadline__c, Detail__c FROM Tasky_Task__c WHERE Project__c =: projectId];
        return tasks;
    }
}