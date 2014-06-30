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
        Tasky_Task__c[] tasks = [SELECT Name, Assignee__c, Deadline__c, Detail__c, Late__c FROM Tasky_Task__c WHERE Project__c =: projectId];
        return tasks;
    }

    @RemoteAction
    global static Tasky_Project__c createProject(String projectName, String description) {
        Tasky_Project__c proj = new Tasky_Project__c();
        proj.Name = projectName;
        proj.Description__c = description;
        insert proj;
        return proj;
    }

    @RemoteAction
    global static Tasky_Task__c updateTaskStatus(Id taskID, String status) {
        Tasky_Task__c task = [SELECT Status__c FROM Tasky_Task__c WHERE Id =: taskId];
        task.Status__c = status;
        update task;
        return task;
    }

    @RemoteAction
    global static Tasky_Task__c createTask(Tasky_Task__c task) {
        insert task;
        return task;
    }
}