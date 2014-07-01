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
        Tasky_Task__c[] tasks = [SELECT Name, Assignee__c, Deadline__c, Detail__c, Late__c, Status__c FROM Tasky_Task__c WHERE Project__c =: projectId];
        return tasks;
    }

    /** Create a project named PROJECTNAME with a DESCRIPTION. */
    @RemoteAction
    global static Tasky_Project__c createProject(String projectName, String description) {
        Tasky_Project__c proj = new Tasky_Project__c();
        proj.Name = projectName;
        proj.Description__c = description;
        insert proj;
        Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
        collaborator.Project__c = proj.Id;
        collaborator.User__c = UserInfo.getUserId();
        collaborator.Name = UserInfo.getName();
        insert collaborator;
        return proj;
    }

    /** Update the STATUS of a Task with Id TASKID. */
    @RemoteAction
    global static Tasky_Task__c updateTaskStatus(Id taskID, String status) {
        Tasky_Task__c task = [SELECT Status__c FROM Tasky_Task__c WHERE Id =: taskId];
        task.Status__c = status;
        update task;
        return task;
    }

    /** Upserts TASK. */
    @RemoteAction
    global static Tasky_Task__c upsertTask(Tasky_Task__c task) {
        upsert task;
        return task;
    }

    /** Adds a collaborator with Id USERID to a project with Id PROJECTID. */
    @RemoteAction
    global static Tasky_Collaborator__c addCollaborator(Id projectId, Id userId) {
        Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
        User[] users= [SELECT Name FROM User WHERE Id =: userId];
        collaborator.Name = users[0].Name;
        collaborator.User__c = userId;
        collaborator.Project__c = projectId;
        insert collaborator;
        return collaborator;
    }

    /** Returns all the users in the org. */
    @RemoteAction
    global static User[] getOrgUsers() {
        User[] users = [SELECT Name, Id FROM User];
        return users;
    }

    /** Removes Collaborator with Id COLLABORATOR from the project. */
    @RemoteAction
    global static Tasky_Collaborator__c removeCollaborator(Id collaboratorId) {
        Tasky_Collaborator__c[] collaborators = [SELECT Id FROM Tasky_Collaborator__c WHERE Id =: collaboratorId];
        delete collaborators[0];
        return collaborators[0];
    }

    /** Returns all the Collaborators working on project with Id PROJECTID. */
    @RemoteAction
    global static Tasky_Collaborator__c[] getProjectCollaborators(Id projectId) {
        Tasky_Collaborator__c[] collaborators = [SELECT Id, Name FROM Tasky_Collaborator__c WHERE Project__c =: projectId];
        return collaborators;
    }

    /** Assigns Collaborator with Id COLLABORATORID to TASK. */
    @RemoteAction
    global static Tasky_Task__c assignTask(Tasky_Task__c task, Id collaboratorId) {
        task.Assignee__c = collaboratorId;
        update task;
        return task;
    }

    /** Remove the assignee from TASK. */
    @RemoteAction
    global static Tasky_Task__c removeTaskCollaborator(Tasky_Task__c task) {
        task.Assignee__c = null;
        update task;
        return task;
    }
}