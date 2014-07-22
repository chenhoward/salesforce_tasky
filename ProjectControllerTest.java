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
        Tasky_Task__c[] tasks = ProjectController.getTasks(setup());
        System.assertEquals(1, tasks.size());
    }
}