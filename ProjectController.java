/** Controller for Tasky Projects Page. */
global class ProjectController{

    @RemoteAction
    global static Tasky_Collaborator__c[] getCollaborators() {
        Tasky_Collaborator__c[] collaborators = [SELECT Name, Project__c, Project__r.Name, Project__r.Description__c FROM Tasky_Collaborator__c WHERE User__c =: UserInfo.getUserId()];
        return collaborators;
    }
}