trigger Collaborator_Create on Tasky_Project__c (after insert) {
    if (trigger.isInsert) {
        for (Tasky_Project__c proj: trigger.new){
            Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
            collaborator.Project__c = proj.Id;
            collaborator.User__c = UserInfo.getUserId();
            collaborator.Name = UserInfo.getName();
            insert collaborator;
        }
    }
}

trigger Task_Complete on Tasky_Task__c (before insert, before update) {
    if (trigger.isInsert || trigger.isUpdate) {
        for (Tasky_Task__c task: trigger.new){
            if (task.Status__c == 'Done'){
                task.Completed_Date__c = System.today();
            }
        }
    }
}

trigger Task_Assign on Tasky_Task__c (before insert, before update) {
    if (trigger.isInsert || trigger.isUpdate) {
        for (Tasky_Task__c task: trigger.new){
            Id owner = task.Owner__c;
            Tasky_Collaborator__c[] collaborators = [SELECT Name FROM Tasky_Collaborator__c WHERE Project__c =: task.Project__c AND User__c =: task.Owner__c];
            if (collaborators.size() == 0 && owner != null) {
                task.addError('The User is not a collaborator in this project');
            }        
        }
    }
}