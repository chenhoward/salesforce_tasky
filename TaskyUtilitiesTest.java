/** A test for the TaskyUtilities . */
@isTest
public class TaskyUtilitiesTest{

    static testmethod void testCreatProject() {
        String name = 'Proj';
        String description = 'Desc';
        TaskyUtilities.createProject(name, description)
        Tasky_Project__c[]  projs = [Select Name, Description__c From Tasky_Project__c];
        Tasky_Project__c  proj = projs[0];
        System.assertEquals(name, proj.Name);
    }
}