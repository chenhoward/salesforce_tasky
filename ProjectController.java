/** Controller for Tasky Projects Page. */
global with Sharing class ProjectController {

    /** Gets all the Projects the user can see. */
    @RemoteAction
    global static Tasky_Project__c[] getProjects() {
        Tasky_Project__c[] projects = [SELECT Name, Id, Due_Date__c, Description__c, CreatedById FROM Tasky_Project__c];
        return projects;
    }

    /** Gets all Tasks associated with the Project with Id PROJECTID. */
    @RemoteAction
    global static Tasky_Task__c[] getTasks(Id projectId) {
        Tasky_Task__c[] tasks = [SELECT Name, Assignee__c, Detail__c, Late__c, Status__c, Assignee__r.User__c, Due_Date__c FROM Tasky_Task__c WHERE Project__c =: projectId];
        return tasks;
    }

    /** Update the STATUS of a Task with Id TASKID and returns all the tasks. */
    @RemoteAction
    global static Tasky_Task__c[] updateTaskStatus(Id taskID, String status, Id projectId) {
        Tasky_Task__c[] tasks = getTasks(projectId);
        Tasky_Task__c task;
        for (Integer i = 0; i < tasks.size(); i++) {
            if (tasks[i].Id == taskId) {
                task = tasks[i];
            }
        }
        task.Status__c = status;
        if (Schema.SObjectType.Tasky_Task__c.isUpdateable()) {
            update task;
        }
        return tasks;
    }

    /** Adds a collaborator with Id USERID to a project with Id PROJECTID. */
    @RemoteAction
    global static List<SObject[]> addCollaborator(Id projectId, Id userId) {
        Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
        User[] users= [SELECT Name FROM User WHERE Id =: userId];
        collaborator.Name = users[0].Name;
        collaborator.User__c = userId;
        collaborator.Project__c = projectId;
        if (Schema.SObjectType.Tasky_Collaborator__c.isCreateable()) {
            insert collaborator;
        }
        return finalList(projectId);
    }

    private static List<SObject[]> finalList(Id projId) {
        List<SObject[]> finalList = new List<List<SObject>>();
        finalList.add(getOrgUsers(projId));
        finalList.add(getProjectCollaborators(projId));
        return finalList;
    }

    /** Returns all the users in the org. */
    @RemoteAction
    global static User[] getOrgUsers(Id projId) {
        Tasky_Collaborator__c[] collaborators = getProjectCollaborators(projId);
        User[] users = [SELECT Name, Id FROM User];
        User[] finalList = new List<User>();
        for (Integer i = 0; i < users.size(); i++) {
            if (!isUserACollaborator(users[i], collaborators)) {
                finalList.add(users[i]);
            }
        }
        return finalList;
    }

    /** Returns true if the COLLABORATORS contain the CHECKEDUSER. */
    private static Boolean isUserACollaborator(User checkedUser, Tasky_Collaborator__c[] collaborators) {
        for (Integer i = 0; i < collaborators.size(); i++) {
            if (checkedUser.Id == collaborators[i].User__c) {
                return true;
            }
        }
        return false;
    }

    /** Removes Collaborator with Id COLLABORATOR from the project. */
    @RemoteAction
    global static List<SObject[]> removeCollaborator(Id collaboratorId) {
        Tasky_Collaborator__c[] collaborators = [SELECT Id, Project__c FROM Tasky_Collaborator__c WHERE Id =: collaboratorId];
        Id projId = collaborators[0].Project__c;
        if (Schema.SObjectType.Tasky_Collaborator__c.isDeletable()) {
            delete collaborators[0];
        }
        return finalList(projId);
    }

    /** Returns all the Collaborators working on project with Id PROJECTID. */
    @RemoteAction
    global static Tasky_Collaborator__c[] getProjectCollaborators(Id projectId) {
        Tasky_Collaborator__c[] collaborators = [SELECT Id, Name, User__c, User__r.Email FROM Tasky_Collaborator__c WHERE Project__c =: projectId];
        return collaborators;
    }

    /** Assigns Collaborator with Id COLLABORATORID to TASK. */
    @RemoteAction
    global static Tasky_Task__c assignTask(Tasky_Task__c task, Id collaboratorId) {
        task.Assignee__c = collaboratorId;
        if (Schema.SObjectType.Tasky_Task__c.isUpdateable()) {
            update task;
        }
        return task;
    }

    /** Gets a list of urls for the chatter photoes of users with id USERIDS. */    
    @RemoteAction
    global static String[] getPhotoes(Id[] userIds) {
        String[] imageUrls = new List<String>();
        for (Id userId: userIds) {
            imageUrls.add(ConnectApi.ChatterUsers.getPhoto(null, userId).smallPhotoUrl);
        }
        return imageUrls;
    }

}