/** The test for ProjectController. */
@isTest
public class ProjectControllerTest{

    static Id setup() {
        Tasky_Project__c project = new Tasky_Project__c();
        project.Name = 'project';
        project.Description__c = 'description';
        Tasky_Project__c project2 = new Tasky_Project__c();
        project.Name = 'project 2';
        project.Description__c = 'description 2';
        insert project;
        insert project2;
        Tasky_Task__c task = new Tasky_Task__c();
        task.Name = 'Task 1';
        task.Project__c = project.Id;
        insert task;
        return project.Id;
    }
    
    static testmethod void testGetProjects() {
        setup();
        Tasky_Project__c[] projects = ProjectController.getProjects();
        System.assertEquals(2, projects.size());
    }
    
    static testmethod void testGetTasks() {
        Id projectId = setup();
        Tasky_Task__c[] tasks = ProjectController.getTasks(projectId);
        System.assertEquals(1, tasks.size());
        System.assertEquals('Task 1', tasks[0].Name);
        System.assertEquals('Pending', tasks[0].Status__c);
        Tasky_Task__c task = tasks[0];
        ProjectController.updateTaskStatus(task, 'Done');
        tasks = ProjectController.getTasks(projectId);
        System.assertEquals('Done', tasks[0].Status__c);
    }
    
    static testmethod void testAddCollaborator() {
        Id projectId = setup();
        Tasky_Collaborator__c collaborator = [SELECT Name FROM Tasky_Collaborator__c WHERE Project__c =: projectId];
        System.assertEquals(UserInfo.getName(), collaborator.Name);
        ProjectController.removeCollaborator(collaborator.Id);
        Tasky_Collaborator__c[] collaborators = [SELECT Name FROM Tasky_Collaborator__c WHERE Project__c =: projectId];
        System.assertEquals(0, collaborators.size());
        ProjectController.addCollaborator(projectId, UserInfo.getUserId());
        collaborator = [SELECT Name FROM Tasky_Collaborator__c WHERE Project__c =: projectId];
        System.assertEquals(UserInfo.getName(), collaborator.Name);
    }
    
    static testmethod void testAssignTask() {
        Id projectId = setup();
        Tasky_Task__c[] tasks = ProjectController.getTasks(projectId);
        System.assertEquals(null, tasks[0].Assignee__c);
        Tasky_Collaborator__c collaborator = [SELECT Name, User__c FROM Tasky_Collaborator__c WHERE Project__c =: projectId];
        ProjectController.assignTask(tasks[0], collaborator.User__c);
        tasks = ProjectController.getTasks(projectId);
        System.assertEquals(UserInfo.getUserId(), tasks[0].Assignee__c);
    }
    
    static testmethod void testDeleteTask() {
        Id projectId = setup();
        Tasky_Task__c[] tasks = ProjectController.getTasks(projectId);
        ProjectController.deleteTask(tasks[0]);
        tasks = ProjectController.getTasks(projectId);
        System.assertEquals(0, tasks.size());
    }
    
    static testmethod void testFlipPriority() {
        Id projectId = setup();
        Tasky_Task__c[] tasks = ProjectController.getTasks(projectId);
        System.assertEquals(false, tasks[0].Priority__c);
        tasks[0].Priority__c = true;
        ProjectController.flipPriority(tasks[0]);
        tasks = ProjectController.getTasks(projectId);
        System.assertEquals(true, tasks[0].Priority__c);
    }
}