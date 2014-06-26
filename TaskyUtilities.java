/** A Utility Class for Tasky. */
public class TaskyUtilities{
    
    public Tasky_Project__c createProject(String name, String description) {
        Tasky_Project__c proj = new Tasky_Project__c();
        if (name != null) {
            proj.Name = name;
        } else {
            proj.Name = '';
        }
        if (description != null) {
            proj.Description__c = description;
        } else {
            proj.Description__c = '';
        }
        insert proj;
        return proj;
    }

    public Tasky_Collaborator__c createCollaborator(Id projectId, Id userId) {
        Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
        collaborator.Project__c = projectId;
        collaborator.User__c = userId;
        insert collaborator;
        return collaborator;
    }
}