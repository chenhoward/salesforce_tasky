/** The test for ProjectController. */
@isTest
public class ProjectControllerTest{

    static void setup() {
        Tasky_Project__c project = new Tasky_Project__c();
        project.Name = 'project';
        project.Description__c = 'description';
        Tasky_Project__c project2 = new Tasky_Project__c();
        project.Name = 'project 2';
        project.Description__c = 'description 2';
        insert project;
        insert project2;
    }
    
    static testmethod void testGetProjects() {
        setup();
        Tasky_Project__c[] projects = ProjectController.getProjects();
        System.assertEquals(2, projects.size());
    }
}